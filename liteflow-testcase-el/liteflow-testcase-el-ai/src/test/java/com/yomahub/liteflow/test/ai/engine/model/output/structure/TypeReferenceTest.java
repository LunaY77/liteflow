/**
 * TypeReference 类型引用测试类
 *
 * @author 苍镜月
 * @since 2.12.1
 */
package com.yomahub.liteflow.test.ai.engine.model.output.structure;

import com.yomahub.liteflow.ai.engine.model.output.structure.TypeReference;
import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TypeReferenceTest {

    /**
     * 测试基本泛型类型获取
     */
    @Test
    void testBasicGenericType() {
        TypeReference<String> stringRef = new TypeReference<String>() {
        };
        Type type = stringRef.getType();

        assertNotNull(type);
        assertEquals(String.class, type);
    }

    /**
     * 测试List泛型类型
     */
    @Test
    void testListGenericType() {
        TypeReference<List<String>> listRef = new TypeReference<List<String>>() {
        };
        Type type = listRef.getType();

        assertNotNull(type);
        assertTrue(type instanceof ParameterizedType);

        ParameterizedType paramType = (ParameterizedType) type;
        assertEquals(List.class, paramType.getRawType());

        Type[] actualTypes = paramType.getActualTypeArguments();
        assertEquals(1, actualTypes.length);
        assertEquals(String.class, actualTypes[0]);
    }

    /**
     * 测试Map泛型类型
     */
    @Test
    void testMapGenericType() {
        TypeReference<Map<String, Integer>> mapRef = new TypeReference<Map<String, Integer>>() {
        };
        Type type = mapRef.getType();

        assertNotNull(type);
        assertTrue(type instanceof ParameterizedType);

        ParameterizedType paramType = (ParameterizedType) type;
        assertEquals(Map.class, paramType.getRawType());

        Type[] actualTypes = paramType.getActualTypeArguments();
        assertEquals(2, actualTypes.length);
        assertEquals(String.class, actualTypes[0]);
        assertEquals(Integer.class, actualTypes[1]);
    }

    /**
     * 测试嵌套泛型类型
     */
    @Test
    void testNestedGenericType() {
        TypeReference<List<Map<String, Integer>>> nestedRef = new TypeReference<List<Map<String, Integer>>>() {
        };
        Type type = nestedRef.getType();

        assertNotNull(type);
        assertTrue(type instanceof ParameterizedType);

        ParameterizedType listType = (ParameterizedType) type;
        assertEquals(List.class, listType.getRawType());

        Type[] listActualTypes = listType.getActualTypeArguments();
        assertEquals(1, listActualTypes.length);
        assertTrue(listActualTypes[0] instanceof ParameterizedType);

        ParameterizedType mapType = (ParameterizedType) listActualTypes[0];
        assertEquals(Map.class, mapType.getRawType());

        Type[] mapActualTypes = mapType.getActualTypeArguments();
        assertEquals(2, mapActualTypes.length);
        assertEquals(String.class, mapActualTypes[0]);
        assertEquals(Integer.class, mapActualTypes[1]);
    }

    /**
     * 测试使用字符串构造函数
     */
    @Test
    void testStringConstructor() {
        TypeReference<String> stringRef = new TypeReference("java.lang.String"){};
        Type type = stringRef.getType();

        assertNotNull(type);
        // 注意：这里的行为取决于JsonSchemaGenerator.typeFromString的实现
        // 暂时只验证不为null
    }

    /**
     * 测试自定义类型
     */
    @Test
    void testCustomType() {
        TypeReference<TestClass> customRef = new TypeReference<TestClass>() {
        };
        Type type = customRef.getType();

        assertNotNull(type);
        assertEquals(TestClass.class, type);
    }

    /**
     * 测试泛型自定义类型
     */
    @Test
    void testGenericCustomType() {
        TypeReference<GenericTestClass<String>> genericRef = new TypeReference<GenericTestClass<String>>() {
        };
        Type type = genericRef.getType();

        assertNotNull(type);
        assertTrue(type instanceof ParameterizedType);

        ParameterizedType paramType = (ParameterizedType) type;
        assertEquals(GenericTestClass.class, paramType.getRawType());

        Type[] actualTypes = paramType.getActualTypeArguments();
        assertEquals(1, actualTypes.length);
        assertEquals(String.class, actualTypes[0]);
    }

    /**
     * 测试原始类型（非泛型）应该抛出异常
     */
    @Test
    void testRawTypeException() {
        // 这个测试验证如果不使用泛型，应该抛出RuntimeException
        assertThrows(RuntimeException.class, () -> {
            @SuppressWarnings("rawtypes")
            TypeReference rawRef = new TypeReference() {
            };
            rawRef.getType();
        });
    }

    /**
     * 测试数组类型
     */
    @Test
    void testArrayType() {
        TypeReference<String[]> arrayRef = new TypeReference<String[]>() {
        };
        Type type = arrayRef.getType();

        assertNotNull(type);
        assertEquals(String[].class, type);
    }

    /**
     * 测试基本类型
     */
    @Test
    void testPrimitiveTypes() {
        TypeReference<Integer> intRef = new TypeReference<Integer>() {
        };
        assertEquals(Integer.class, intRef.getType());

        TypeReference<Boolean> boolRef = new TypeReference<Boolean>() {
        };
        assertEquals(Boolean.class, boolRef.getType());

        TypeReference<Double> doubleRef = new TypeReference<Double>() {
        };
        assertEquals(Double.class, doubleRef.getType());
    }

    /**
     * 测试通配符类型
     */
    @Test
    void testWildcardType() {
        // 这个测试比较复杂，因为通配符类型在编译时会被处理
        TypeReference<List<?>> wildcardRef = new TypeReference<List<?>>() {
        };
        Type type = wildcardRef.getType();

        assertNotNull(type);
        assertTrue(type instanceof ParameterizedType);

        ParameterizedType paramType = (ParameterizedType) type;
        assertEquals(List.class, paramType.getRawType());
    }

    /**
     * 测试相同类型的TypeReference实例
     */
    @Test
    void testSameTypeReferences() {
        TypeReference<String> ref1 = new TypeReference<String>() {
        };
        TypeReference<String> ref2 = new TypeReference<String>() {
        };

        assertEquals(ref1.getType(), ref2.getType());
    }

    /**
     * 测试不同类型的TypeReference实例
     */
    @Test
    void testDifferentTypeReferences() {
        TypeReference<String> stringRef = new TypeReference<String>() {
        };
        TypeReference<Integer> integerRef = new TypeReference<Integer>() {
        };

        assertNotEquals(stringRef.getType(), integerRef.getType());
    }

    /**
     * 用于测试的内部类
     */
    static class TestClass {
        private String value;

        public TestClass(String value) {
            this.value = value;
        }
    }

    /**
     * 用于测试的泛型内部类
     */
    static class GenericTestClass<T> {
        private T value;

        public GenericTestClass(T value) {
            this.value = value;
        }

        public T getValue() {
            return value;
        }
    }
}
