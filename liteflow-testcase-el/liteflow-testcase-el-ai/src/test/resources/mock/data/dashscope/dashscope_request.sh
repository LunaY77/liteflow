#!/bin/bash

# ===================================================================================
# 功能：向阿里云 DashScope (通义千问) API 发送请求
#
# 支持多种请求类型（流式/阻塞式文本、工具调用、结构化输出）。
#
# 使用方法:
# 1. 设置 API Key:
#    export DASHSCOPE_API_KEY='您的API密钥'
#
# 2. 赋予执行权限:
#    chmod +x dashscope_request.sh
#
# 3. 运行脚本:
#    ./dashscope_request.sh <request_type>
#
# 示例:
# ./dashscope_request.sh streaming_text
# ./dashscope_request.sh blocking_tool_call
# ./dashscope_request.sh classify
# ===================================================================================

# --- DashScope 配置 ---
API_URL="https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions"
DEFAULT_MODEL="qwen-plus"
# 'qwen-turbo' 或更新的模型（如 'qwen-max'）对工具调用的支持和响应速度更佳
TOOL_MODEL="qwen-turbo"

# --- 函数定义区 ---

# 函数：打印使用说明
usage() {
    echo "Usage: $0 <request_type>"
    echo ""
    echo "支持的请求类型 (request_type):"
    echo "  streaming_text         - 流式输出文本"
    echo "  blocking_text          - 阻塞式输出文本"
    echo "  blocking_tool_call     - 阻塞式调用工具"
    echo "  streaming_tool_call    - 流式调用工具"
    echo "  blocking_structured    - 阻塞式结构化输出 (JSON)"
    echo "  streaming_structured   - 流式结构化输出 (JSON)"
    echo "  classify               - 阻塞式单标签分类"
    echo "  classify_multi         - 阻塞式多标签JSON分类"
    echo ""
    echo "运行前，请确保已设置环境变量: export DASHSCOPE_API_KEY='您的API密钥'"
    exit 1
}

# 函数：执行API请求的核心逻辑
# 参数: $1: output_file, $2: json_payload, $3: use_stream, $4: model_to_use
execute_request() {
    local output_file="$1"
    local json_payload="$2"
    local use_stream="$3"
    local model_to_use="$4"

    # 从JSON中提取用户问题内容用于显示
    # 注意：这里只提取了第一个user角色的内容
    local user_content=$(echo "$json_payload" | jq -r '(.messages[] | select(.role=="user") | .content) | first')

    echo "=================================================="
    echo "厂商: DashScope (通义千问)"
    echo "类型: $REQUEST_TYPE"
    echo "模型: $model_to_use"
    echo "问题: $user_content"
    echo "--------------------------------------------------"
    echo "正在发送请求..."

    # 对于流式请求，使用 --no-buffer 选项可以立即看到输出
    local curl_options=""
    if [ "$use_stream" = "true" ]; then
        curl_options="--no-buffer"
    fi

    # -s: 静默模式, -X POST: 指定请求方法
    curl $curl_options -s -X POST "$API_URL" \
         -H "Authorization: Bearer $DASHSCOPE_API_KEY" \
         -H "Content-Type: application/json" \
         -d "$json_payload" > "$output_file"

    echo "请求完成！"
    echo "原始输出已完整保存到 '$output_file' 文件中。"
    echo "您可以使用 'cat $output_file' 或 'less $output_file' 查看结果。"
    echo "=================================================="
}

# --- 主逻辑 ---

# 1. 检查 API Key
if [ -z "$DASHSCOPE_API_KEY" ]; then
    echo "错误: 请设置环境变量 DASHSCOPE_API_KEY"
    usage
fi

# 2. 解析命令行参数
REQUEST_TYPE=$1
if [ -z "$REQUEST_TYPE" ]; then
    usage
fi

# 3. 根据请求类型构造 JSON 请求体并执行
OUTPUT_FILE="output_dashscope_${REQUEST_TYPE}.log"
JSON_PAYLOAD=""
USE_STREAM=false
MODEL=$DEFAULT_MODEL

case "$REQUEST_TYPE" in
    streaming_text)
        USE_STREAM=true
        JSON_PAYLOAD=$(jq -n \
          --arg model "$MODEL" \
          --arg content "请给我讲一个关于宇宙探索的短故事" \
          '{
            "model": $model,
            "messages": [
              {"role": "system", "content": "You are a helpful assistant."},
              {"role": "user", "content": $content}
            ],
            "stream": true,
            "enable_thinking": true
          }')
        ;;

    blocking_text)
        JSON_PAYLOAD=$(jq -n \
          --arg model "$MODEL" \
          --arg content "请解释一下什么是黑洞" \
          '{
            "model": $model,
            "messages": [
              {"role": "system", "content": "You are a helpful assistant."},
              {"role": "user", "content": $content}
            ],
            "stream": false,
            "enable_thinking": true
          }')
        ;;

    blocking_tool_call)
        MODEL=$TOOL_MODEL
        JSON_PAYLOAD=$(jq -n \
          --arg model "$MODEL" \
          --arg content "查询北京和上海今天的天气" \
          '{
            "model": $model,
            "messages": [
              {"role": "system", "content": "You are a helpful assistant."},
              {"role": "user", "content": $content}
            ],
            "tools": [
              {
                "type": "function",
                "function": {
                  "name": "get_current_weather",
                  "description": "获取一个城市当前的实时天气",
                  "parameters": {
                    "type": "object",
                    "properties": {
                      "location": {
                        "type": "string",
                        "description": "城市名称, e.g. 北京"
                      }
                    },
                    "required": ["location"]
                  }
                }
              }
            ],
            "stream": false,
            "enable_thinking": true
          }')
        ;;

    streaming_tool_call)
        USE_STREAM=true
        MODEL=$TOOL_MODEL
        JSON_PAYLOAD=$(jq -n \
          --arg model "$MODEL" \
          --arg content "查询广州今天的天气以及现在的时间" \
          '{
            "model": $model,
            "messages": [
              {"role": "system", "content": "You are a helpful assistant."},
              {"role": "user", "content": $content}
            ],
            "tools": [
              {
                "type": "function",
                "function": {
                  "name": "get_current_weather",
                  "description": "当你想查询指定城市的天气时非常有用。",
                  "parameters": {
                    "type": "object",
                    "properties": { "location":{ "type": "string", "description": "城市或县区"}},
                    "required": ["location"]
                  }
                }
              },
              {
                "type": "function",
                "function": { "name": "get_current_time", "description": "当你想知道现在的时间时非常有用。", "parameters": {} }
              }
            ],
            "stream": true,
            "enable_thinking": true
          }')
        ;;

    blocking_structured)
        JSON_PAYLOAD=$(jq -n \
          --arg model "$MODEL" \
          --arg content "请用JSON格式输出一个包含三个虚构人物（姓名、职业、年龄）的列表" \
          '{
            "model": $model,
            "messages": [
              {"role": "system", "content": "You are a helpful assistant designed to output JSON."},
              {"role": "user", "content": $content}
            ],
            "response_format": {"type": "json_object"},
            "stream": false,
          }')
        ;;

    streaming_structured)
        USE_STREAM=true
        JSON_PAYLOAD=$(jq -n \
          --arg model "$MODEL" \
          --arg content "请用JSON格式流式输出一个包含三个虚构行星（名称、星系、发现年份）的列表" \
          '{
            "model": $model,
            "messages": [
              {"role": "system", "content": "You are a helpful assistant designed to output JSON."},
              {"role": "user", "content": $content}
            ],
            "response_format": {"type": "json_object"},
            "stream": true,
          }')
        ;;

    classify)
        USE_STREAM=false
        MODEL="qwen-flash"

        # 使用 $'...' 语法来保留 \n 换行符
        SYSTEM_CONTENT=$'You are an expert intent classifier.\nYour task is to analyze the user\'s query and classify it based on the predefined categories.\n\nAvailable categories are:\n- java\n- python\n\nFollow these rules strictly:\n1. You must select only ONE category that best matches the user\'s query.\n2. Your response MUST be only the name of that single category.\n3. For example: category1\n4. Do NOT provide any explanations, introductions, or any text other than the category name(s) in the specified format.'
        USER_CONTENT="请帮我写一段Java代码"

        JSON_PAYLOAD=$(jq -n \
          --arg model "$MODEL" \
          --arg sys_content "$SYSTEM_CONTENT" \
          --arg user_content "$USER_CONTENT" \
          '{
            model: $model,
            messages: [
              {"role": "system", "content": $sys_content},
              {"role": "user", "content": $user_content}
            ],
            stream: false,
            enable_thinking: true,
            response_format: { "type": "text" }
          }')
        ;;

    classify_multi)
        USE_STREAM=false
        MODEL="qwen-flash"

        SYSTEM_CONTENT=$'You are an expert intent classifier.\nYour task is to analyze the user\'s query and classify it based on the predefined categories.\n\nAvailable categories are:\n- java\n- python\n\nFollow these rules strictly:\n1. You may select one or more categories that are relevant to the user\'s query.\n2. Your response MUST be a valid JSON array of strings, containing only the names of the selected categories.\n3. For example: ["category1", "category2"]\n4. Do NOT provide any explanations, introductions, or any text other than the category name(s) in the specified format.'
        USER_CONTENT_1="请帮我写一段Java代码, 同时给出 Python 代码"
        USER_CONTENT_2=$'Your response should be in JSON format.\nDo not include any explanations, only provide a RFC8259 compliant JSON response following this format without deviation.\nDo not include markdown code blocks in your response.\nRemove the ```json markdown from the output.\nHere is the JSON Schema instance your output must adhere to:\n```\n{\n  "type" : "array",\n  "items" : {\n    "type" : "string"\n  }\n}\n```'

        JSON_PAYLOAD=$(jq -n \
          --arg model "$MODEL" \
          --arg sys_content "$SYSTEM_CONTENT" \
          --arg user_content_1 "$USER_CONTENT_1" \
          --arg user_content_2 "$USER_CONTENT_2" \
          '{
            model: $model,
            messages: [
              {"role": "system", "content": $sys_content},
              {"role": "user", "content": $user_content_1},
              {"role": "user", "content": $user_content_2}
            ],
            stream: false,
            enable_thinking: false,
            response_format: { "type": "json_object" }
          }')
        ;;

    *)
        echo "错误: 未知的请求类型 '$REQUEST_TYPE'"
        usage
        ;;
esac

if [ -z "$JSON_PAYLOAD" ]; then
    echo "错误: 未能为请求类型 '$REQUEST_TYPE' 生成有效的JSON请求体。"
    exit 1
fi

# 4. 执行请求
execute_request "$OUTPUT_FILE" "$JSON_PAYLOAD" "$USE_STREAM" "$MODEL"