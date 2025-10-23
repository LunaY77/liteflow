#!/bin/bash

# ===================================================================================
# 功能：向火山方舟 (Volcano Engine Ark) API 发送兼容 OpenAI 格式的请求
#
# 支持多种请求类型（流式/阻塞式文本、带思考过程的工具调用、高级结构化输出）。
#
# 使用方法:
# 1. 设置 API Key:
#    export OPENAI_API_KEY='您的火山方舟API密钥'
#
# 2. 赋予执行权限:
#    chmod +x openai_request.sh
#
# 3. 运行脚本:
#    ./openai_request.sh <request_type>
#
# 示例:
# ./openai_request.sh streaming_text
# ./openai_request.sh blocking_tool_call
# ./openai_request.sh classify
# ===================================================================================

# --- 火山方舟 (Ark) 配置 ---
API_URL="https://ark.cn-beijing.volces.com/api/v3/chat/completions"
# 通用请求的默认模型
DEFAULT_MODEL="doubao-seed-1-6-250615" # 来自您提供的示例

# --- 函数定义区 ---

# 函数：打印使用说明
usage() {
    echo "Usage: $0 <request_type>"
    echo ""
    echo "支持的请求类型 (request_type):"
    echo "  streaming_text         - 流式输出文本"
    echo "  blocking_text          - 阻塞式输出文本"
    echo "  blocking_tool_call     - 阻塞式调用工具 (开启思考过程)"
    echo "  streaming_tool_call    - 流式调用工具 (开启思考过程)"
    echo "  blocking_structured    - 阻塞式结构化输出 (JSON Schema)"
    echo "  streaming_structured   - 流式结构化输出 (JSON Schema)"
    echo "  classify               - 阻塞式单标签分类"
    echo "  classify_multi         - 阻塞式多标签JSON分类"
    echo ""
    echo "运行前，请确保已设置环境变量: export OPENAI_API_KEY='您的API密钥'"
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
    local user_content=$(echo "$json_payload" | jq -r '.messages[] | select(.role=="user") | .content | if type=="array" then .[].text else . end')


    echo "=================================================="
    echo "厂商: 火山方舟 (Volcano Engine Ark)"
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

    curl $curl_options -s -X POST "$API_URL" \
         -H "Content-Type: application/json" \
         -H "Authorization: Bearer $OPENAI_API_KEY" \
         -d "$json_payload" > "$output_file"

    echo "请求完成！"
    echo "原始输出已完整保存到 '$output_file' 文件中。"
    echo "您可以使用 'cat $output_file' 查看结果。"
    echo "=================================================="
}

# --- 主逻辑 ---

# 1. 检查 API Key
if [ -z "$OPENAI_API_KEY" ]; then
    echo "错误: 请设置环境变量 OPENAI_API_KEY"
    usage
fi

# 2. 解析命令行参数
REQUEST_TYPE=$1
if [ -z "$REQUEST_TYPE" ]; then
    usage
fi

# 3. 根据请求类型构造 JSON 请求体并执行
OUTPUT_FILE="output_openai_${REQUEST_TYPE}.log"
JSON_PAYLOAD=""
USE_STREAM=false
MODEL=$DEFAULT_MODEL

case "$REQUEST_TYPE" in
    streaming_text)
        JSON_PAYLOAD=$(jq -n \
          --arg model "$MODEL" \
          --arg content "请给我讲一个关于未来城市的短故事" \
          '{model: $model, messages: [{"role": "system", "content": "你是豆包，是由字节跳动开发的 AI 人工智能助手。"},{"role": "user", "content": $content}], stream: true}')
        ;;

    blocking_text)
        JSON_PAYLOAD=$(jq -n \
          --arg model "$MODEL" \
          --arg content "请解释一下什么是“人工智能”" \
          '{model: $model, messages: [{"role": "system", "content": "你是豆包，是由字节跳动开发的 AI 人工智能助手。"},{"role": "user", "content": $content}], stream: false}')
        ;;

    blocking_tool_call)
        JSON_PAYLOAD=$(jq -n \
          --arg model "$MODEL" \
          --arg content "请帮我查询一下北京的天气，并告诉我字节跳动（ByteDance）的当前股价" \
          '{model: $model, messages: [{"role": "user", "content": $content}], tools: [{"type": "function", "function": {"name": "get_current_weather", "description": "获取指定城市的当前天气", "parameters": {"type": "object", "properties": { "location": { "type": "string", "description": "城市名称, e.g. 北京市" }}, "required": ["location"]}}}, {"type": "function", "function": {"name": "get_stock_price", "description": "获取指定公司的当前股票价格", "parameters": {"type": "object", "properties": { "company_name": { "type": "string", "description": "公司名称, e.g. ByteDance" }}, "required": ["company_name"]}}}], thought: true, stream: false}')
        ;;

    streaming_tool_call)
        USE_STREAM=true
        JSON_PAYLOAD=$(jq -n \
          --arg model "$MODEL" \
          --arg content "上海的天气怎么样？" \
          '{model: $model, messages: [{"role": "user", "content": $content}], tools: [{"type": "function", "function": {"name": "get_current_weather", "description": "获取指定城市的当前天气", "parameters": {"type": "object", "properties": { "location": { "type": "string", "description": "城市名称, e.g. 上海市" }}, "required": ["location"]}}}], thought: true, stream: true}')
        ;;

    blocking_structured|streaming_structured)
        # 为两种结构化请求设置共同参数
        MODEL=$DEFAULT_MODEL
        if [ "$REQUEST_TYPE" = "streaming_structured" ]; then
            USE_STREAM=true
        fi

        # 定义 JSON Schema 结构
        SCHEMA='{
          "name": "math_reasoning",
          "schema": {
            "type": "object",
            "properties": {
              "steps": {
                "type": "array",
                "items": {
                  "type": "object",
                  "properties": { "explanation": { "type": "string" }, "output": { "type": "string" } },
                  "required": [ "explanation", "output" ], "additionalProperties": false
                }
              },
              "final_answer": { "type": "string" }
            },
            "required": [ "steps", "final_answer" ], "additionalProperties": false
          },
          "strict": true
        }'

        JSON_PAYLOAD=$(jq -n \
          --arg model "$MODEL" \
          --arg content_text "使用中文解题: 8x + 9 = 32 and x + y = 1" \
          --argjson schema_obj "$SCHEMA" \
          --argjson use_stream_bool "$USE_STREAM" \
          '{
            model: $model,
            messages: [
              {"role": "system", "content": "你是一位数学辅导老师。"},
              {"role": "user", "content": [{"type": "text", "text": $content_text}]}
            ],
            response_format: {
              "type": "json_schema",
              "json_schema": $schema_obj
            },
            thinking: {"type": "disabled"},
            stream: $use_stream_bool
          }')
        ;;

    classify)
        USE_STREAM=false
        SYSTEM_CONTENT="You are an expert intent classifier.\nYour task is to analyze the user's query and classify it based on the predefined categories.\n\nAvailable categories are:\n- java\n- python\n\nFollow these rules strictly:\n1. You must select only ONE category that best matches the user's query.\n2. Your response MUST be only the name of that single category.\n3. For example: category1\n4. Do NOT provide any explanations, introductions, or any text other than the category name(s) in the specified format."
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
            thinking: { "type": "enabled" }
          }')
        ;;

    classify_multi)
        USE_STREAM=false
        SYSTEM_CONTENT="You are an expert intent classifier.\nYour task is to analyze the user's query and classify it based on the predefined categories.\n\nAvailable categories are:\n- java\n- python\n\nFollow these rules strictly:\n1. You may select one or more categories that are relevant to the user's query.\n2. Your response MUST be a valid JSON array of strings, containing only the names of the selected categories.\n3. For example: [\"category1\", \"category2\"]\n4. Do NOT provide any explanations, introductions, or any text other than the category name(s) in the specified format."
        USER_CONTENT="请帮我写一段Java代码, 同时给出 Python 代码"

        # 定义 response_format 所需的 schema
        SCHEMA='{
          "type": "json_schema",
          "json_schema": {
            "name": "java.util.List<java.lang.String>",
            "schema": {
              "type": "array",
              "items": { "type": "string" }
            }
          }
        }'

        JSON_PAYLOAD=$(jq -n \
          --arg model "$MODEL" \
          --arg sys_content "$SYSTEM_CONTENT" \
          --arg user_content "$USER_CONTENT" \
          --argjson schema_obj "$SCHEMA" \
          '{
            model: $model,
            messages: [
              {"role": "system", "content": $sys_content},
              {"role": "user", "content": $user_content}
            ],
            stream: false,
            thinking: { "type": "enabled" },
            response_format: $schema_obj
          }')
        ;;

    *)
        echo "错误: 不支持的请求类型 '$REQUEST_TYPE'"
        usage
        ;;
esac

# 检查JSON是否成功生成
if [ -z "$JSON_PAYLOAD" ]; then
    echo "错误: 未能为请求类型 '$REQUEST_TYPE' 生成有效的JSON请求体。"
    exit 1
fi

# 4. 执行请求
execute_request "$OUTPUT_FILE" "$JSON_PAYLOAD" "$USE_STREAM" "$MODEL"