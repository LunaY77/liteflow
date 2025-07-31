package com.yomahub.liteflow.test.ai.util;

import com.yomahub.liteflow.ai.util.SetUtil;
import com.yomahub.liteflow.ai.util.TriState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * SetUtil 工具类测试
 *
 * @author 苍镜月
 * @since TODO
 */

@DisplayName("SetUtil 工具类测试")
class SetUtilTest {

    // --- 为参数化测试提供数据 ---

    private static final String NOT_PRESENT_METHOD_SOURCE = "com.yomahub.liteflow.test.ai.util.SetUtilTest#provideNotPresentValues";
    private static final String PRESENT_METHOD_SOURCE = "com.yomahub.liteflow.test.ai.util.SetUtilTest#providePresentValues";

    /**
     * 提供被认为是 "not present" (不存在/为空/为默认值) 的各种值
     */
    static Stream<Arguments> provideNotPresentValues() {
        return Stream.of(
                arguments((Object) null),
                arguments(""),
                arguments("   "),
                arguments(new ArrayList<>()),
                arguments(Collections.emptyMap()),
                arguments((Object) new String[0]),
                arguments((Object) new int[0]),
                arguments(TriState.UNSET),
                arguments(-1),
                arguments(-1L),
                arguments(-1.0),
                arguments(-1.0f)
        );
    }


    /**
     * 提供被认为是 "present" (存在/有效) 的各种值
     */
    static Stream<Arguments> providePresentValues() {
        Map<String, Integer> presentMap = new HashMap<>();
        presentMap.put("key", 1);

        return Stream.of(
                arguments("hello"),
                arguments(" a "),
                arguments(Arrays.asList(1, 2)),
                arguments(presentMap),
                arguments((Object) new String[]{"a"}),
                arguments((Object) new int[]{1}),
                arguments(new Object()),
                arguments(TriState.TRUE),
                arguments(0),
                arguments(1),
                arguments(0L),
                arguments(0.0),
                arguments(0.0f)
        );
    }


    @Nested
    @DisplayName("核心判断方法：isPresent 和 isNotPresent")
    class IsPresentAndNotPresentTests {

        @ParameterizedTest
        @MethodSource(NOT_PRESENT_METHOD_SOURCE)
        @DisplayName("isNotPresent: 对于 null、空集合或默认值应返回 true")
        void testIsNotPresent_shouldReturnTrue_forEmptyOrDefaultValues(Object value) {
            assertTrue(SetUtil.isNotPresent(value), "值 " + value + " 应被视为 not present");
        }

        @ParameterizedTest
        @MethodSource(PRESENT_METHOD_SOURCE)
        @DisplayName("isNotPresent: 对于有效值应返回 false")
        void testIsNotPresent_shouldReturnFalse_forPresentValues(Object value) {
            assertFalse(SetUtil.isNotPresent(value), "值 " + value + " 应被视为 present");
        }

        @ParameterizedTest
        @MethodSource(PRESENT_METHOD_SOURCE)
        @DisplayName("isPresent: 对于有效值应返回 true")
        void testIsPresent_shouldReturnTrue_forPresentValues(Object value) {
            assertTrue(SetUtil.isPresent(value), "值 " + value + " 应被视为 present");
        }

        @ParameterizedTest
        @MethodSource(NOT_PRESENT_METHOD_SOURCE)
        @DisplayName("isPresent: 对于 null、空集合或默认值应返回 false")
        void testIsPresent_shouldReturnFalse_forEmptyOrDefaultValues(Object value) {
            assertFalse(SetUtil.isPresent(value), "值 " + value + " 应被视为 not present");
        }
    }

    @Nested
    @DisplayName("消费方法: setIfPresent")
    class SetIfPresentTests {

        @ParameterizedTest
        @MethodSource(PRESENT_METHOD_SOURCE)
        @DisplayName("当值存在时，应执行 Consumer")
        void shouldExecuteConsumer_whenValueIsPresent(Object presentValue) {
            AtomicBoolean executed = new AtomicBoolean(false);
            SetUtil.setIfPresent(v -> executed.set(true), presentValue);
            assertTrue(executed.get(), "当值为 " + presentValue + " 时，Consumer 应该被执行");
        }

        @ParameterizedTest
        @MethodSource(NOT_PRESENT_METHOD_SOURCE)
        @DisplayName("当值不存在时，不应执行 Consumer")
        void shouldNotExecuteConsumer_whenValueIsNotPresent(Object notPresentValue) {
            AtomicBoolean executed = new AtomicBoolean(false);
            SetUtil.setIfPresent(v -> executed.set(true), notPresentValue);
            assertFalse(executed.get(), "当值为 " + notPresentValue + " 时，Consumer 不应该被执行");
        }
    }

    @Nested
    @DisplayName("消费方法: setIfNotPresent")
    class SetIfNotPresentTests {

        @ParameterizedTest
        @MethodSource(NOT_PRESENT_METHOD_SOURCE)
        @DisplayName("当值不存在时，应执行 Consumer")
        void shouldExecuteConsumer_whenValueIsNotPresent(Object notPresentValue) {
            AtomicBoolean executed = new AtomicBoolean(false);
            SetUtil.setIfNotPresent(v -> executed.set(true), notPresentValue);
            assertTrue(executed.get(), "当值为 " + notPresentValue + " 时，Consumer 应该被执行");
        }

        @ParameterizedTest
        @MethodSource(PRESENT_METHOD_SOURCE)
        @DisplayName("当值存在时，不应执行 Consumer")
        void shouldNotExecuteConsumer_whenValueIsPresent(Object presentValue) {
            AtomicBoolean executed = new AtomicBoolean(false);
            SetUtil.setIfNotPresent(v -> executed.set(true), presentValue);
            assertFalse(executed.get(), "当值为 " + presentValue + " 时，Consumer 不应该被执行");
        }
    }
}
