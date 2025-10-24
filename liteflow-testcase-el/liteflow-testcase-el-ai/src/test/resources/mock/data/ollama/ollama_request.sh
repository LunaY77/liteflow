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
# ./ollama_request.sh blocking_tool_call_2
# ./ollama_request.sh classify
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
    echo "  streaming_tool_call    - 流式调用工具 (第1轮)"
    echo "  streaming_tool_call_2  - 流式调用工具 (第2轮, 带工具结果)"
    echo "  blocking_tool_call     - 阻塞式调用工具 (第1轮)"
    echo "  blocking_tool_call_2   - 阻塞式调用工具 (第2轮, 带工具结果)"
    echo "  blocking_structured    - 阻塞式结构化输出 (JSON)"
    echo "  streaming_structured   - 流式结构化输出 (JSON)"
    echo "  classify               - 阻塞式单标签分类"
    echo "  classify_multi         - 阻塞式多标签JSON分类"
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

    local user_content=$(echo "$json_payload" | jq -r '(.messages[] | select(.role=="user") | .content) | first')

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
        MODEL="qwen3:32b"
        JSON_PAYLOAD=$(cat <<'EOF'
        {
          "model" : "qwen3:32b",
          "messages" : [ {
            "role" : "user",
            "content" : "调用工具组装 QQ 和 微信"
          } ],
          "stream" : true,
          "tools" : [ {
            "type" : "function",
            "function" : {
              "name" : "assemble_tool",
              "description" : "组装工具\n将 a 和 b 组装成答案",
              "parameters" : {
                "type" : "object",
                "properties" : {
                  "a" : {
                    "type" : "string"
                  },
                  "b" : {
                    "type" : "string"
                  }
                },
                "required" : [ "a", "b" ],
                "additionalProperties" : false
              }
            }
          } ],
          "think" : true
        }
EOF
        )
        ;;

    streaming_tool_call_2)
        USE_STREAM=true
        MODEL="qwen3:32b"
        JSON_PAYLOAD=$(cat <<'EOF'
        {
          "model" : "qwen3:32b",
          "messages" : [ {
            "role" : "user",
            "content" : "调用工具组装 QQ 和 微信"
          }, {
            "role" : "assistant",
            "content" : "<think>\n好的，用户让我调用工具组装QQ和微信。首先，我需要确认用户提供的工具是什么。根据之前的工具描述，有一个assemble_tool函数，它需要两个参数a和b，都是字符串类型。用户提到的QQ和微信应该作为这两个参数的值。接下来，我需要按照函数的要求，将QQ和微信作为参数传递给assemble_tool。要确保参数的顺序正确，可能用户希望将QQ作为a，微信作为b，或者可能没有特定顺序要求。但根据用户指令中的顺序，应该是先QQ后微信。因此，正确的调用应该是a是QQ，b是微信。然后生成相应的JSON对象放在tool_call标签中。检查一下是否符合格式要求，确保没有语法错误，比如引号是否正确，逗号的位置等。确认无误后，返回结果。\n</think>\n",
            "tool_calls" : [ {
              "id" : null,
              "type" : "function",
              "name" : "assemble_tool",
              "arguments" : {
                "a" : "QQ",
                "b" : "微信"
              },
              "function" : {
                "name" : "assemble_tool",
                "arguments" : {
                  "a" : "QQ",
                  "b" : "微信"
                }
              }
            } ]
          }, {
            "role" : "tool",
            "content" : "\"Assembled result: QQ and 微信\"",
            "tool_call_id" : null,
            "tool_name" : "assemble_tool"
          } ],
          "stream" : true,
          "tools" : [ {
            "type" : "function",
            "function" : {
              "name" : "assemble_tool",
              "description" : "组装工具\n将 a 和 b 组装成答案",
              "parameters" : {
                "type" : "object",
                "properties" : {
                  "a" : {
                    "type" : "string"
                  },
                  "b" : {
                    "type" : "string"
                  }
                },
                "required" : [ "a", "b" ],
                "additionalProperties" : false
              }
            }
          } ],
          "think" : true
        }
EOF
        )
        ;;

    blocking_tool_call)
        USE_STREAM=false
        MODEL="qwen3:32b"
        JSON_PAYLOAD=$(cat <<'EOF'
        {
          "model" : "qwen3:32b",
          "messages" : [ {
            "role" : "user",
            "content" : "北京今天天气怎么样"
          } ],
          "stream" : false,
          "tools" : [ {
            "type" : "function",
            "function" : {
              "name" : "weather_tool",
              "description" : "查询天气\n获取指定位置的天气信息",
              "parameters" : {
                "type" : "object",
                "properties" : {
                  "content" : {
                    "type" : "string"
                  }
                },
                "required" : [ "content" ],
                "additionalProperties" : false
              }
            }
          } ],
          "think" : false
        }
EOF
        )
        ;;

    blocking_tool_call_2)
        USE_STREAM=false
        MODEL="qwen3:32b"
        JSON_PAYLOAD=$(cat <<'EOF'
        {
          "model" : "qwen3:32b",
          "messages" : [ {
            "role" : "user",
            "content" : "北京今天天气怎么样"
          }, {
            "role" : "assistant",
            "content" : "",
            "tool_calls" : [ {
              "id" : null,
              "type" : "function",
              "name" : "weather_tool",
              "arguments" : {
                "content" : "北京"
              },
              "function" : {
                "name" : "weather_tool",
                "arguments" : {
                  "content" : "北京"
                }
              }
            } ]
          }, {
            "role" : "tool",
            "content" : "\"The weather in 北京 is sunny, 25°C.\"",
            "tool_call_id" : null,
            "tool_name" : "weather_tool"
          } ],
          "stream" : false,
          "tools" : [ {
            "type" : "function",
            "function" : {
              "name" : "weather_tool",
              "description" : "查询天气\n获取指定位置的天气信息",
              "parameters" : {
                "type" : "object",
                "properties" : {
                  "content" : {
                    "type" : "string"
                  }
                },
                "required" : [ "content" ],
                "additionalProperties" : false
              }
            }
          } ],
          "think" : false
        }
EOF
        )
        ;;

    blocking_structured)
        USE_STREAM=false
        MODEL="qwen3:32b" # Model is specified in the JSON
        JSON_PAYLOAD=$(cat <<'EOF'
        {
          "model" : "qwen3:32b",
          "messages" : [ {
            "role" : "system",
            "content" : "你是一位数学辅导老师"
          }, {
            "role" : "user",
            "content" : "使用中文解题: 8x + 9 = 32 and x + y = 1"
          } ],
          "stream" : false,
          "think" : false,
          "format" : {
            "type" : "object",
            "properties" : {
              "final_answer" : {
                "type" : "string"
              },
              "steps" : {
                "type" : "array",
                "items" : {
                  "type" : "object",
                  "properties" : {
                    "explanation" : {
                      "type" : "string"
                    },
                    "output" : {
                      "type" : "string"
                    }
                  },
                  "required" : [ "explanation", "output" ],
                  "additionalProperties" : false
                }
              }
            },
            "required" : [ "final_answer", "steps" ],
            "additionalProperties" : false
          }
        }
EOF
        )
      ;;

    streaming_structured)
        USE_STREAM=true
        MODEL="qwen3:32b" # Model is specified in the JSON
        JSON_PAYLOAD=$(cat <<'EOF'
        {
          "model" : "qwen3:32b",
          "messages" : [ {
            "role" : "system",
            "content" : "你是一位数学辅导老师"
          }, {
            "role" : "user",
            "content" : "使用中文解题: 8x + 9 = 32 and x + y = 1"
          } ],
          "stream" : true,
          "think" : true,
          "format" : {
            "type" : "object",
            "properties" : {
              "final_answer" : {
                "type" : "string"
              },
              "steps" : {
                "type" : "array",
                "items" : {
                  "type" : "object",
                  "properties" : {
                    "explanation" : {
                      "type" : "string"
                    },
                    "output" : {
                      "type" : "string"
                    }
                  },
                  "required" : [ "explanation", "output" ],
                  "additionalProperties" : false
                }
              }
            },
            "required" : [ "final_answer", "steps" ],
            "additionalProperties" : false
          }
        }
EOF
        )
      ;;

    classify)
        USE_STREAM=false
        MODEL=$DEFAULT_MODEL # 使用默认模型 (qwen3:32b)
        JSON_PAYLOAD=$(cat <<EOF
        {
          "model": "$MODEL",
          "messages": [
            {
              "role": "system",
              "content": "You are an expert intent classifier.\nYour task is to analyze the user's query and classify it based on the predefined categories.\n\nAvailable categories are:\n- java\n- python\n\nFollow these rules strictly:\n1. You must select only ONE category that best matches the user's query.\n2. Your response MUST be only the name of that single category.\n3. For example: category1\n4. Do NOT provide any explanations, introductions, or any text other than the category name(s) in the specified format."
            },
            {
              "role": "user",
              "content": "请帮我写一段Java代码"
            }
          ],
          "stream": false,
          "think": false
        }
EOF
        )
        ;;

    classify_multi)
        USE_STREAM=false
        MODEL=$DEFAULT_MODEL # 使用默认模型 (qwen3:32b)
        JSON_PAYLOAD=$(cat <<EOF
        {
          "model": "$MODEL",
          "messages": [
            {
              "role": "system",
              "content": "You are an expert intent classifier.\nYour task is to analyze the user's query and classify it based on the predefined categories.\n\nAvailable categories are:\n- java\n- python\n\nFollow these rules strictly:\n1. You may select one or more categories that are relevant to the user's query.\n2. Your response MUST be a valid JSON array of strings, containing only the names of the selected categories.\n3. For example: [\"category1\", \"category2\"]\n4. Do NOT provide any explanations, introductions, or any text other than the category name(s) in the specified format."
            },
            {
              "role": "user",
              "content": "请帮我写一段Java代码, 同时给出 Python 代码"
            }
          ],
          "stream": false,
          "think": false,
          "format": {
            "type": "array",
            "items": {
              "type": "string"
            }
          }
        }
EOF
        )
        ;;

    *)
        echo "错误: 不支持的请求类型 '$REQUEST_TYPE'"
        usage
        ;;
esac

# S: 检查JSON是否成功生成
if [ -z "$JSON_PAYLOAD" ]; then
    echo "错误: 未能为请求类型 '$REQUEST_TYPE' 生成有效的JSON请求体。"
    exit 1
fi

# 3. 执行请求
execute_request "$OUTPUT_FILE" "$JSON_PAYLOAD" "$USE_STREAM" "$MODEL"