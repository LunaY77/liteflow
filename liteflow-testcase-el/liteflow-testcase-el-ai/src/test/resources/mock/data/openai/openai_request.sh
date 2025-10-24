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
# ./openai_request.sh blocking_tool_call_2
# ./openai_request.sh classify
# ===================================================================================

# --- 火山方舟 (Ark) 配置 ---
API_URL="https://ark.cn-beijing.volces.com/api/v3/chat/completions"
# 通用请求的默认模型
DEFAULT_MODEL="doubao-seed-1-6-251015" # 来自您提供的示例

# --- 函数定义区 ---

# 函数：打印使用说明
usage() {
    echo "Usage: $0 <request_type>"
    echo ""
    echo "支持的请求类型 (request_type):"
    echo "  streaming_text         - 流式输出文本"
    echo "  blocking_text          - 阻塞式输出文本"
    echo "  blocking_tool_call     - 阻塞式调用工具 (第1次调用)"
    echo "  streaming_tool_call    - 流式调用工具 (第1次调用)"
    echo "  blocking_tool_call_2   - 阻塞式调用工具 (第2次, 含工具结果)"
    echo "  streaming_tool_call_2  - 流式调用工具 (第2次, 含工具结果)"
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
    # 这个jq命令可以同时处理 "content": "string" 和 "content": [{"type": "text", "text": "string"}] 两种格式
    local user_content=$(echo "$json_payload" | jq -r '(.messages[] | select(.role=="user") | .content) | if type=="array" then .[].text else . end | first')


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
        MODEL="doubao-seed-1-6-250615"
        USE_STREAM=false
        # 使用单引号包裹静态JSON字符串
        JSON_PAYLOAD='{
          "model" : "doubao-seed-1-6-250615",
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
          "thinking" : {
            "type" : "disabled"
          }
        }'
        ;;

    streaming_tool_call)
        MODEL="doubao-seed-1-6-250615"
        USE_STREAM=true
        # 使用单引号包裹静态JSON字符串
        JSON_PAYLOAD='{
          "model" : "doubao-seed-1-6-250615",
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
          "thinking" : {
            "type" : "enabled"
          }
        }'
        ;;

    blocking_tool_call_2)
        MODEL="doubao-seed-1-6-250615"
        USE_STREAM=false
        JSON_PAYLOAD='{
          "model" : "doubao-seed-1-6-250615",
          "messages" : [ {
            "role" : "user",
            "content" : "北京今天天气怎么样"
          }, {
            "role" : "assistant",
            "content" : "",
            "tool_calls" : [ {
              "id" : "call_8ydot326j82hqgfauttl3tqs",
              "type" : "function",
              "name" : "weather_tool",
              "arguments" : "{\"content\":\"北京今天天气\"}",
              "function" : {
                "name" : "weather_tool",
                "arguments" : "{\"content\":\"北京今天天气\"}"
              }
            } ]
          }, {
            "role" : "tool",
            "content" : "\"The weather in 北京今天天气 is sunny, 25°C.\"",
            "tool_call_id" : "call_8ydot326j82hqgfauttl3tqs",
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
          "thinking" : {
            "type" : "disabled"
          }
        }'
        ;;

    streaming_tool_call_2)
        MODEL="doubao-seed-1-6-250615"
        USE_STREAM=true
        JSON_PAYLOAD='{
          "model" : "doubao-seed-1-6-250615",
          "messages" : [ {
            "role" : "user",
            "content" : "调用工具组装 QQ 和 微信"
          }, {
            "role" : "assistant",
            "content" : "<think>\n我现在需要处理用户的请求：“调用工具组装 QQ 和微信”。首先，我得仔细看看用户提供的工具信息。用户提到的工具列表里只有一个工具，名字是assemble_tool，它的功能是将a和b组装成答案，参数需要a和b两个字符串，且都是必填项。接下来，用户的问题是要组装QQ和微信。这里的“组装”应该是指把这两个应用名称组合起来。根据assemble_tool的参数要求，我需要将QQ作为a，微信作为b，或者反过来？不过用户没有指定顺序，可能默认顺序就是QQ在前，微信在后。不过工具的描述只是说“组装成答案”，没有说明具体的格式，可能直接拼接字符串即可。然后检查参数是否符合要求。a和b都是字符串类型，QQ和微信都是字符串，所以没问题。必填项a和b都提供了，没有遗漏。additionalProperties是false，所以不能添加其他参数。因此，正确的调用应该是使用assemble_tool，参数a为\"QQ\"，参数b为\"微信\"。需要确保JSON格式正确，使用双引号，逗号分隔，没有语法错误。同时，因为是单工具调用，所以数组里只有一个对象，不需要id字段。最后，按照用户要求的格式，用<|FunctionCallBegin|>和<|FunctionCallEnd|>包裹这个JSON数组。确认没有其他需要注意的地方，比如工具名称是否正确，参数是否正确对应。看起来一切都符合要求，所以可以生成调用指令了。</think>\n",
            "tool_calls" : [ {
              "id" : "call_gcp2mh3fmtn72slfepu1cnc9",
              "type" : "function",
              "name" : "assemble_tool",
              "arguments" : "{\"a\":\"QQ\",\"b\":\"微信\"}",
              "function" : {
                "name" : "assemble_tool",
                "arguments" : "{\"a\":\"QQ\",\"b\":\"微信\"}"
              }
            } ]
          }, {
            "role" : "tool",
            "content" : "\"Assembled result: QQ and 微信\"",
            "tool_call_id" : "call_gcp2mh3fmtn72slfepu1cnc9",
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
          "thinking" : {
            "type" : "enabled"
          }
        }'
        ;;

    blocking_structured)
        MODEL="doubao-seed-1-6-250615"
        USE_STREAM=false
        # 使用单引号包裹静态JSON字符串
        JSON_PAYLOAD='{
          "model" : "doubao-seed-1-6-250615",
          "messages" : [ {
            "role" : "system",
            "content" : "你是一位数学辅导老师"
          }, {
            "role" : "user",
            "content" : "使用中文解题: 8x + 9 = 32 and x + y = 1"
          } ],
          "stream" : false,
          "thinking" : {
            "type" : "enabled"
          },
          "response_format" : {
            "type" : "json_schema",
            "json_schema" : {
              "name" : "com.yomahub.liteflow.test.ai.core.structure.output.MathReasoning",
              "schema" : {
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
          }
        }'
        ;;

    streaming_structured)
        MODEL="doubao-seed-1-6-251015"
        USE_STREAM=true
        # 使用单引号包裹静态JSON字符串
        JSON_PAYLOAD='{
          "model" : "doubao-seed-1-6-250615",
          "messages" : [ {
            "role" : "system",
            "content" : "你是一位数学辅导老师"
          }, {
            "role" : "user",
            "content" : "使用中文解题: 8x + 9 = 32 and x + y = 1"
          } ],
          "stream" : true,
          "thinking" : {
            "type" : "disabled"
          },
          "response_format" : {
            "type" : "json_schema",
            "json_schema" : {
              "name" : "com.yomahub.liteflow.test.ai.core.structure.output.MathReasoning",
              "schema" : {
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
          }
        }'
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