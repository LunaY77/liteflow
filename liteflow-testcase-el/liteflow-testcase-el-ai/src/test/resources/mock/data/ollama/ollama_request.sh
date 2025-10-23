#!/bin/bash

# ===================================================================================
# 功能：向本地 Ollama API 发送请求
#
# 支持多种请求类型（流式/阻塞式文本、工具调用、结构化JSON输出）。
#
# 使用方法:
# 1. 确保 Ollama 服务正在本地运行。
# 2. 赋予执行权限:
#    chmod +x ollama_request.sh
#
# 3. 运行脚本:
#    ./ollama_request.sh <request_type>
#
# 示例:
# ./ollama_request.sh streaming_text
# ./ollama_request.sh blocking_tool_call
# ./ollama_request.sh blocking_structured
# ===================================================================================

# --- Ollama 配置 ---
API_URL="http://localhost:11434/api/chat"
DEFAULT_MODEL="qwen3:32b"

# --- 函数定义区 ---

# 函数：打印使用说明
usage() {
    echo "Usage: $0 <request_type>"
    echo ""
    echo "支持的请求类型 (request_type):"
    echo "  streaming_text         - 流式输出文本"
    echo "  blocking_text          - 阻塞式输出文本"
    echo "  streaming_tool_call    - 流式调用工具"
    echo "  blocking_tool_call     - 阻塞式调用工具"
    echo "  blocking_structured    - 阻塞式结构化输出 (JSON)"
    echo "  streaming_structured   - 流式结构化输出 (JSON)"
    echo ""
    echo "运行前，请确保本地 Ollama 服务已启动。"
    exit 1
}

# 函数：执行API请求的核心逻辑
# 参数: $1: output_file, $2: json_payload, $3: use_stream, $4: model_to_use
execute_request() {
    local output_file="$1"
    local json_payload="$2"
    local use_stream="$3"
    local model_to_use="$4"

    # 从JSON中提取用户问题以用于显示
    local user_content=$(echo "$json_payload" | jq -r '.messages[0].content')

    echo "=================================================="
    echo "服务: Ollama"
    echo "类型: $REQUEST_TYPE"
    echo "模型: $model_to_use"
    echo "问题: $user_content"
    echo "--------------------------------------------------"
    echo "正在发送请求至 $API_URL ..."
    echo "请求体 (Payload):"
    echo "$json_payload" | jq . # 美化后打印JSON体
    echo "--------------------------------------------------"


    # 对于流式请求，使用 --no-buffer 选项可以立即看到输出，并直接打印到终端
    if [ "$use_stream" = "true" ]; then
        echo "流式响应:"
        curl --no-buffer -s -X POST "$API_URL" \
             -H "Content-Type: application/json" \
             -d "$json_payload" | tee "$output_file"
        echo "" # 在流式输出结束后换行
    else
        echo "阻塞式响应:"
        curl -s -X POST "$API_URL" \
             -H "Content-Type: application/json" \
             -d "$json_payload" > "$output_file"

        # 美化并打印非流式请求的输出
        if jq -e . "$output_file" > /dev/null 2>&1; then
             cat "$output_file" | jq .
        else
             cat "$output_file"
        fi
    fi

    echo "" # 增加一个换行符，使输出更清晰
    echo "=================================================="
    echo "请求完成！"
    echo "原始输出已完整保存到 '$output_file' 文件中。"
    echo "您可以使用 'cat $output_file' 查看原始结果。"
    echo "=================================================="
}

# --- 主逻辑 ---

# 1. 检查命令行参数
REQUEST_TYPE=$1
if [ -z "$REQUEST_TYPE" ]; then
    usage
fi

# 2. 根据请求类型构造 JSON 请求体
OUTPUT_FILE="output_ollama_${REQUEST_TYPE}.log"
JSON_PAYLOAD=""
USE_STREAM=false
MODEL=""

case "$REQUEST_TYPE" in
    streaming_text)
        USE_STREAM=true
        MODEL=$DEFAULT_MODEL
        JSON_PAYLOAD=$(jq -n \
          --arg model "$MODEL" \
          --arg content "why is the sky blue?" \
          '{
            model: $model,
            messages: [{"role": "user", "content": $content}],
            stream: true
          }')
        ;;

    blocking_text)
        USE_STREAM=false
        MODEL=$DEFAULT_MODEL
        JSON_PAYLOAD=$(jq -n \
          --arg model "$MODEL" \
          --arg content "why is the sky blue?" \
          '{
            model: $model,
            messages: [{"role": "user", "content": $content}],
            stream: false
          }')
        ;;

    streaming_tool_call)
        USE_STREAM=true
        MODEL=$DEFAULT_MODEL
        JSON_PAYLOAD='{
          "model": "'$MODEL'",
          "messages": [
            {
              "role": "user",
              "content": "what is the weather in tokyo?"
            }
          ],
          "tools": [
            {
              "type": "function",
              "function": {
                "name": "get_weather",
                "description": "Get the weather in a given city",
                "parameters": {
                  "type": "object",
                  "properties": {
                    "city": {
                      "type": "string",
                      "description": "The city to get the weather for"
                    }
                  },
                  "required": ["city"]
                }
              }
            }
          ],
          "stream": true
        }'
        ;;

    blocking_tool_call)
        USE_STREAM=false
        MODEL=$DEFAULT_MODEL
        JSON_PAYLOAD='{
          "model": "'$MODEL'",
          "messages": [
            {
              "role": "user",
              "content": "what is the weather in tokyo?"
            }
          ],
          "tools": [
            {
              "type": "function",
              "function": {
                "name": "get_weather",
                "description": "Get the weather in a given city",
                "parameters": {
                  "type": "object",
                  "properties": {
                    "city": {
                      "type": "string",
                      "description": "The city to get the weather for"
                    }
                  },
                  "required": ["city"]
                }
              }
            }
          ],
          "stream": false
        }'
        ;;

    blocking_structured)
        USE_STREAM=false
        MODEL=$DEFAULT_MODEL
        JSON_PAYLOAD=$(cat <<EOF
        {
          "model": "$MODEL",
          "messages": [{"role": "user", "content": "Ollama is 22 years old and busy saving the world. Return a JSON object with the age and availability."}],
          "stream": false,
          "format": {
            "type": "object",
            "properties": {
              "age": { "type": "integer" },
              "available": { "type": "boolean" }
            },
            "required": [ "age", "available" ]
          },
          "options": { "temperature": 0 }
        }
        EOF
        )
      ;;

    streaming_structured)
        USE_STREAM=true
        MODEL=$DEFAULT_MODEL
        JSON_PAYLOAD=$(cat <<EOF
        {
          "model": "$MODEL",
          "messages": [{"role": "user", "content": "Ollama is 22 years old and busy saving the world. Return a JSON object with the age and availability."}],
          "stream": true,
          "format": {
            "type": "object",
            "properties": {
              "age": { "type": "integer" },
              "available": { "type": "boolean" }
            },
            "required": [ "age", "available" ]
          },
          "options": { "temperature": 0 }
        }
        EOF
        )
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

# 3. 执行请求
execute_request "$OUTPUT_FILE" "$JSON_PAYLOAD" "$USE_STREAM" "$MODEL"