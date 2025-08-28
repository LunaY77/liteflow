package com.yomahub.liteflow.flow.element.condition;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.enums.ConditionTypeEnum;
import com.yomahub.liteflow.exception.NoSwitchTargetNodeException;
import com.yomahub.liteflow.exception.SwitchTargetCannotBePreOrFinallyException;
import com.yomahub.liteflow.flow.element.Condition;
import com.yomahub.liteflow.flow.element.Executable;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.slot.DataBus;
import com.yomahub.liteflow.slot.Slot;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 选择Condition
 *
 * @author Bryan.Zhang
 * @since 2.8.0
 */
public class SwitchCondition extends Condition {

	private final String TAG_PREFIX = "tag";

	private final String TAG_FLAG = ":";

	private static final String MULTI_TARGET_SPLITTER = ",";

	@Override
	public void executeCondition(Integer slotIndex) throws Exception {
		// 获取switch node
		Node switchNode = this.getSwitchNode();
		// 获取target List
		List<Executable> targetList = this.getTargetList();

		// 提前设置 chainId，避免无法在 isAccess 方法中获取到
		switchNode.setCurrChainId(this.getCurrChainId());

		// 先去判断isAccess方法，如果isAccess方法都返回false，整个SWITCH表达式不执行
		if (!switchNode.isAccess(slotIndex)) {
			return;
		}

		// 先执行switch节点
		switchNode.execute(slotIndex);

		// 拿到switch节点的结果
		String targetId = switchNode.getItemResultMetaValue(slotIndex);

		String[] split = targetId.split(MULTI_TARGET_SPLITTER);
		if (split.length == 1) {
			// 只有一个目标节点
			this.findSwitchTargetAndExecute(slotIndex, targetId, targetList);
		} else {
			// 多个目标节点，执行多路选择
			List<String> targetIds = Arrays.stream(split).map(String::trim).collect(Collectors.toList());
			this.findMultiSwitchTargetsAndExecute(slotIndex, targetIds, targetList);
		}
	}

	private void findSwitchTargetAndExecute(Integer slotIndex, String targetId, List<Executable> targetList) throws Exception {
		Slot slot = DataBus.getSlot(slotIndex);

		Executable targetExecutor = null;
		if (StrUtil.isNotBlank(targetId)) {
			// 这里要判断是否使用tag模式跳转
			if (targetId.contains(TAG_FLAG)) {
				String[] target = targetId.split(TAG_FLAG, 2);
				String _targetId = target[0];
				String _targetTag = target[1];
				targetExecutor = targetList.stream().filter(executable -> (StrUtil.startWith(_targetId, TAG_PREFIX) && ObjectUtil.equal(_targetTag,executable.getTag()))
                        || ((StrUtil.isEmpty(_targetId) || _targetId.equals(executable.getId()))
                        && (StrUtil.isEmpty(_targetTag) || _targetTag.equals(executable.getTag())))).findFirst().orElse(null);
			}
			else {
				targetExecutor = targetList.stream()
						.filter(executable -> ObjectUtil.equal(executable.getId(),targetId) )
						.findFirst()
						.orElse(null);
			}
		}

		if (ObjectUtil.isNull(targetExecutor)) {
			// 没有匹配到执行节点，则走默认的执行节点
			targetExecutor = this.getDefaultExecutor();
		}

		if (ObjectUtil.isNotNull(targetExecutor)) {
			// switch的目标不能是Pre节点或者Finally节点
			if (targetExecutor instanceof PreCondition || targetExecutor instanceof FinallyCondition) {
				String errorInfo = StrUtil.format(
						"[{}]:switch component[{}] error, switch target node cannot be pre or finally",
						slot.getRequestId(), this.getSwitchNode().getInstance().getDisplayName());
				throw new SwitchTargetCannotBePreOrFinallyException(errorInfo);
			}
			targetExecutor.setCurrChainId(this.getCurrChainId());
			targetExecutor.execute(slotIndex);
		}
		else {
			String errorInfo = StrUtil.format("[{}]:no target node find for the component[{}],target str is [{}]",
					slot.getRequestId(), this.getSwitchNode().getInstance().getDisplayName(), targetId);
			throw new NoSwitchTargetNodeException(errorInfo);
		}
	}

	private void findMultiSwitchTargetsAndExecute(Integer slotIndex, List<String> targetIds, List<Executable> targetList) throws Exception {
		Slot slot = DataBus.getSlot(slotIndex);

		List<Executable> matchedExecutors = null;
		if (CollectionUtil.isNotEmpty(targetIds)) {
			// 存储最终目标执行器的 set 集合
			Set<Executable> resultSet = new HashSet<>();

			// 普通 id
			Set<String> normalIds = new HashSet<>();
			// tag 模式的目标，id & tag
			List<Pair<String, String>> tagTargets = new ArrayList<>();

			// 1. 分离 targetIds 中的普通 ID 和 Tag 模式 ID
			for (String targetId : targetIds) {
				if (StrUtil.isNotBlank(targetId)) {
					if (targetId.contains(TAG_FLAG)) {
						String[] target = targetId.split(TAG_FLAG, 2);
						String _targetId = target[0];
						String _targetTag = target[1];
						tagTargets.add(Pair.of(_targetId, _targetTag));
					} else {
						normalIds.add(targetId);
					}
				}
			}

			// 2. 根据普通 ID 筛选目标
			if (!normalIds.isEmpty()) {
				targetList.stream()
						.filter(executable -> normalIds.contains(executable.getId()))
						.forEach(resultSet::add);
			}

			// 3. 根据 Tag 模式筛选目标
			if (!tagTargets.isEmpty()) {
				for (Pair<String, String> target : tagTargets) {
					String _targetId = target.getKey();
					String _targetTag = target.getValue();
					targetList.stream()
							.filter(executable ->
									(StrUtil.startWith(_targetId, TAG_PREFIX) && ObjectUtil.equal(_targetTag, executable.getTag()))
											|| ((StrUtil.isEmpty(_targetId) || _targetId.equals(executable.getId()))
											&& (StrUtil.isEmpty(_targetTag) || _targetTag.equals(executable.getTag()))))
							.forEach(resultSet::add);
				}
			}
			matchedExecutors = resultSet.stream()
					.sorted(Comparator.comparing(Executable::getId))
					.collect(Collectors.toList());
		}

		if (CollectionUtil.isEmpty(matchedExecutors)) {
			// 未匹配到，则走默认节点
			Executable defaultExecutor = this.getDefaultExecutor();
			matchedExecutors = Optional.ofNullable(defaultExecutor)
					.map(CollectionUtil::newArrayList)
					.orElse(null);
		}

		if (CollectionUtil.isNotEmpty(matchedExecutors)) {
			// TODO 实现并行
			for (Executable targetExecutor : matchedExecutors) {
				// switch的目标不能是Pre节点或者Finally节点
				if (targetExecutor instanceof PreCondition || targetExecutor instanceof FinallyCondition) {
					String errorInfo = StrUtil.format(
							"[{}]:switch component[{}] error, switch target node cannot be pre or finally",
							slot.getRequestId(), this.getSwitchNode().getInstance().getDisplayName());
					throw new SwitchTargetCannotBePreOrFinallyException(errorInfo);
				}
				targetExecutor.setCurrChainId(this.getCurrChainId());
				targetExecutor.execute(slotIndex);
			}
		} else {
			String errorInfo = StrUtil.format("[{}]:no target node find for the component[{}],targetIds are {}",
					slot.getRequestId(), this.getSwitchNode().getInstance().getDisplayName(), targetIds);
			throw new NoSwitchTargetNodeException(errorInfo);
		}
	}

		@Override
	public ConditionTypeEnum getConditionType() {
		return ConditionTypeEnum.TYPE_SWITCH;
	}

	public void addTargetItem(Executable executable) {
		this.addExecutable(ConditionKey.SWITCH_TARGET_KEY, executable);
	}

	public List<Executable> getTargetList() {
		return this.getExecutableList(ConditionKey.SWITCH_TARGET_KEY);
	}

	public void setSwitchNode(Node switchNode) {
		this.addExecutable(ConditionKey.SWITCH_KEY, switchNode);
	}

	public Node getSwitchNode() {
		return (Node) this.getExecutableOne(ConditionKey.SWITCH_KEY);
	}

	public Executable getDefaultExecutor() {
		return this.getExecutableOne(ConditionKey.SWITCH_DEFAULT_KEY);
	}

	public void setDefaultExecutor(Executable defaultExecutor) {
		this.addExecutable(ConditionKey.SWITCH_DEFAULT_KEY, defaultExecutor);
	}

}
