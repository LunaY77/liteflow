package com.yomahub.liteflow.test.ai.core.proxy;

/**
 * TODO
 *
 * @author 苍镜月
 * @since TODO
 */

public class TTMPTest {

//    static class Output {
//        private String content;
//    }
//
//    @Test
//    public void test3() throws Exception {
//        Class<?> clazz = createLiteFlowAIAssistant(String.class);
//        Object assitant = AiServices.builder(clazz)
//                .chatModel(null)
//                .build();
//
//    }
//
//    @Test
//    public void test2() throws Exception {
//        // --- 准备阶段 ---
//        // 我们期望的动态类型是 String.class
//        Class<?> expectedDynamicType = Output.class;
//        System.out.println("期望的泛型参数: " + expectedDynamicType.getName());
//        System.out.println("------------------------------------------");
//
//        // --- 执行阶段 ---
//        // 动态创建接口，并传入 String.class
//        Class<?> dynamicInterface = createLiteFlowAIAssistant(expectedDynamicType);
//
//        // --- 验证阶段 ---
//        System.out.println("开始验证动态生成的接口: " + dynamicInterface.getName());
//
//        // 1. 获取 chat(String, String) 方法的 Method 对象
//        Method chatMethod = dynamicInterface.getMethod("chat", String.class, String.class);
//        System.out.println("\n成功获取到方法: " + chatMethod.getName());
//
//        // 2. 获取方法的泛型返回类型
//        Type genericReturnType = chatMethod.getGenericReturnType();
//        System.out.println("方法的 getGenericReturnType() 返回: " + genericReturnType.getTypeName());
//
//        // 3. 检查返回类型是否是参数化类型 (例如 Result<String> 而不是 Result)
//        if (genericReturnType instanceof ParameterizedType) {
//            System.out.println("✅ 类型检查通过: 返回类型是 ParameterizedType");
//
//            // 4. 将其转换为 ParameterizedType
//            ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;
//
//            // 5. 验证原始类型是否为 Result.class
//            Type rawType = parameterizedType.getRawType();
//            System.out.println("  - 原始类型 (Raw Type): " + rawType.getTypeName());
//            if (rawType.equals(Result.class)) {
//                System.out.println("  - ✅ 原始类型验证成功: 是 Result.class");
//            } else {
//                System.out.println("  - ❌ 原始类型验证失败: 不是 Result.class");
//            }
//
//            // 6. 获取所有泛型参数 (例如 <String, Integer> 中的 String 和 Integer)
//            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
//            System.out.println("  - 泛型参数列表 (Actual Type Arguments): " + Arrays.toString(actualTypeArguments));
//
//            // 7. 验证泛型参数的数量和类型
//            if (actualTypeArguments.length == 1) {
//                System.out.println("  - ✅ 泛型参数数量验证成功: 数量为 1");
//                Type actualTypeArgument = actualTypeArguments[0];
//
//                if (actualTypeArgument.equals(expectedDynamicType)) {
//                    System.out.println("  - ✅✅✅ 最终验证成功: 泛型参数与期望的 " + expectedDynamicType.getName() + " 完全匹配！");
//                } else {
//                    System.out.println("  - ❌❌❌ 最终验证失败: 泛型参数是 " + actualTypeArgument.getTypeName() + " 而不是期望的 " + expectedDynamicType.getName());
//                }
//            } else {
//                System.out.println("  - ❌ 泛型参数数量验证失败: 数量不为 1");
//            }
//
//        } else {
//            System.out.println("❌ 类型检查失败: 返回类型不是 ParameterizedType，无法进行泛型验证。");
//        }
//    }
//
//    @Test
//    public void test1() throws Exception {
//        // --- 准备阶段 ---
//        // 我们期望的动态类型是 String.class
//        Class<?> expectedDynamicType = String.class;
//        System.out.println("期望的泛型参数: " + expectedDynamicType.getName());
//        System.out.println("------------------------------------------");
//
//        // --- 执行阶段 ---
//        // 动态创建接口，并传入 String.class
//        Class<?> dynamicInterface = createLiteFlowAIAssistant(expectedDynamicType);
//
//        // --- 验证阶段 ---
//        System.out.println("开始验证动态生成的接口: " + dynamicInterface.getName());
//
//        // 1. 获取 chat(String, String) 方法的 Method 对象
//        Method chatMethod = dynamicInterface.getMethod("chat", String.class, String.class);
//        System.out.println("\n成功获取到方法: " + chatMethod.getName());
//
//        // 2. 获取方法的泛型返回类型
//        Type genericReturnType = chatMethod.getGenericReturnType();
//        System.out.println("方法的 getGenericReturnType() 返回: " + genericReturnType.getTypeName());
//
//        // 3. 检查返回类型是否是参数化类型 (例如 Result<String> 而不是 Result)
//        if (genericReturnType instanceof ParameterizedType) {
//            System.out.println("✅ 类型检查通过: 返回类型是 ParameterizedType");
//
//            // 4. 将其转换为 ParameterizedType
//            ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;
//
//            // 5. 验证原始类型是否为 Result.class
//            Type rawType = parameterizedType.getRawType();
//            System.out.println("  - 原始类型 (Raw Type): " + rawType.getTypeName());
//            if (rawType.equals(Result.class)) {
//                System.out.println("  - ✅ 原始类型验证成功: 是 Result.class");
//            } else {
//                System.out.println("  - ❌ 原始类型验证失败: 不是 Result.class");
//            }
//
//            // 6. 获取所有泛型参数 (例如 <String, Integer> 中的 String 和 Integer)
//            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
//            System.out.println("  - 泛型参数列表 (Actual Type Arguments): " + Arrays.toString(actualTypeArguments));
//
//            // 7. 验证泛型参数的数量和类型
//            if (actualTypeArguments.length == 1) {
//                System.out.println("  - ✅ 泛型参数数量验证成功: 数量为 1");
//                Type actualTypeArgument = actualTypeArguments[0];
//
//                if (actualTypeArgument.equals(expectedDynamicType)) {
//                    System.out.println("  - ✅✅✅ 最终验证成功: 泛型参数与期望的 " + expectedDynamicType.getName() + " 完全匹配！");
//                } else {
//                    System.out.println("  - ❌❌❌ 最终验证失败: 泛型参数是 " + actualTypeArgument.getTypeName() + " 而不是期望的 " + expectedDynamicType.getName());
//                }
//            } else {
//                System.out.println("  - ❌ 泛型参数数量验证失败: 数量不为 1");
//            }
//
//        } else {
//            System.out.println("❌ 类型检查失败: 返回类型不是 ParameterizedType，无法进行泛型验证。");
//        }
//    }
//
//
//    @Test
//    public void test() throws Exception {
//        Class<?> liteFlowAIAssistant = createLiteFlowAIAssistant(Output.class);
//        System.out.println(liteFlowAIAssistant);
//    }
//
//    /**
//     * 动态创建一个 LiteFlowAIAssistant 接口的实现。
//     *
//     * @param dynamicResultType 动态指定的 chat 方法返回结果类型，例如 String.class
//     * @return 动态生成的接口 Class 对象
//     * @throws Exception 如果创建失败
//     */
//    public static Class<?> createLiteFlowAIAssistant(Class<?> dynamicResultType) throws Exception {
//
//        // 1. 使用 TypeDescription.Generic.Builder 来创建参数化的返回类型
//        // 这是处理泛型的关键步骤，我们构建一个表示 Result<dynamicResultType> 的类型
//        TypeDescription.Generic genericResultType = TypeDescription.Generic.Builder
//                .parameterizedType(Result.class, dynamicResultType)
//                .build();
//
//        // 2. 使用 ByteBuddy 开始创建接口
//        DynamicType.Unloaded<?> dynamicType = new ByteBuddy()
//                // 明确指定我们要创建一个接口
//                .makeInterface()
//                // 为接口命名
//                .name("com.example.generated.DynamicLiteFlowAIAssistant")
//
//                // --- 定义第一个方法: chat ---
//                .defineMethod("chat", genericResultType, Visibility.PUBLIC)
//                // 为此方法定义一个泛型参数 <T>
//                // 注意：即使我们已经将返回类型具体化为 Result<String>，
//                // 原始接口的方法签名中仍有 <T>，我们在这里进行保留。
//                // 如果你的目标是完全擦除方法签名中的 <T>，可以省略此行。
////                .withTypeVariable("T")
//                // 添加第一个参数: String userMessage
//                .withParameter(String.class, "userMessage")
//                // 为第一个参数添加注解: @V("userMessage")
//                .annotateParameter(AnnotationDescription.Builder.ofType(V.class)
//                        .define("value", "userMessage")
//                        .build())
//                // 添加第二个参数: String systemMessage
//                .withParameter(String.class, "systemMessage")
//                // 为第二个参数添加注解: @V("systemMessage")
//                .annotateParameter(AnnotationDescription.Builder.ofType(V.class)
//                        .define("value", "systemMessage")
//                        .build())
//                .withoutCode()
//                // 为 chat 方法本身添加注解
//                .annotateMethod(
//                        // @SystemMessage("{{systemMessage}}")
//                        AnnotationDescription.Builder.ofType(SystemMessage.class)
//                                .defineArray("value", "{{systemMessage}}")
//                                .build(),
//                        // @UserMessage("{{userMessage}}")
//                        AnnotationDescription.Builder.ofType(UserMessage.class)
//                                .defineArray("value", "{{userMessage}}")
//                                .build()
//                )
//
//                // --- 定义第二个方法: chatStream ---
//                .defineMethod("chatStream", TokenStream.class, Visibility.PUBLIC)
//                // 添加参数和注解，逻辑同上
//                .withParameter(String.class, "userMessage")
//                .annotateParameter(AnnotationDescription.Builder.ofType(V.class)
//                        .define("value", "userMessage")
//                        .build())
//                .withParameter(String.class, "systemMessage")
//                .annotateParameter(AnnotationDescription.Builder.ofType(V.class)
//                        .define("value", "systemMessage")
//                        .build())
//                .withoutCode()
//                // 为 chatStream 方法添加注解
//                .annotateMethod(
//                        AnnotationDescription.Builder.ofType(SystemMessage.class)
//                                .defineArray("value", "{{systemMessage}}")
//                                .build(),
//                        AnnotationDescription.Builder.ofType(UserMessage.class)
//                                .defineArray("value", "{{userMessage}}")
//                                .build()
//                )
//
//                // 3. 完成创建
//                .make();
//
//        // 4. 加载生成的类并返回 Class 对象
//        return dynamicType
//                .load(AiServiceFactory.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
//                .getLoaded();
//    }
}
