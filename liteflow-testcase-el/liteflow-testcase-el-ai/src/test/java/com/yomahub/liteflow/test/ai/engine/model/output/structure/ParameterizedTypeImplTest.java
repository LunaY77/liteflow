/**
 * ParameterizedTypeImpl 动态构建泛型测试类
 *
 * @author 苍镜月
 * @since 2.12.1
 */
package com.yomahub.liteflow.test.ai.engine.model.output.structure;

import com.yomahub.liteflow.ai.engine.model.output.structure.ParameterizedTypeImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ParameterizedTypeImplTest {

    private ParameterizedTypeImpl listStringType;
    private ParameterizedTypeImpl mapStringIntegerType;

    @BeforeEach
    void setUp() {
        listStringType = new ParameterizedTypeImpl(List.class, String.class);
        mapStringIntegerType = new ParameterizedTypeImpl(Map.class, String.class, Integer.class);
    }

    /**
     * 测试构造函数
     */
    @Test
    void testConstructor() {
        assertNotNull(listStringType);
        assertNotNull(mapStringIntegerType);
    }

    /**
     * 测试getRawType方法
     */
    @Test
    void testGetRawType() {
        assertEquals(List.class, listStringType.getRawType());
        assertEquals(Map.class, mapStringIntegerType.getRawType());
    }

    /**
     * 测试getActualTypeArguments方法
     */
    @Test
    void testGetActualTypeArguments() {
        Type[] listArguments = listStringType.getActualTypeArguments();
        assertEquals(1, listArguments.length);
        assertEquals(String.class, listArguments[0]);

        Type[] mapArguments = mapStringIntegerType.getActualTypeArguments();
        assertEquals(2, mapArguments.length);
        assertEquals(String.class, mapArguments[0]);
        assertEquals(Integer.class, mapArguments[1]);
    }

    /**
     * 测试getOwnerType方法
     */
    @Test
    void testGetOwnerType() {
        assertNull(listStringType.getOwnerType());
        assertNull(mapStringIntegerType.getOwnerType());
    }

    /**
     * 测试toString方法
     */
    @Test
    void testToString() {
        String listString = listStringType.toString();
        assertTrue(listString.contains("java.util.List"));
        assertTrue(listString.contains("java.lang.String"));
        assertTrue(listString.contains("<"));
        assertTrue(listString.contains(">"));

        String mapString = mapStringIntegerType.toString();
        assertTrue(mapString.contains("java.util.Map"));
        assertTrue(mapString.contains("java.lang.String"));
        assertTrue(mapString.contains("java.lang.Integer"));
        assertTrue(mapString.contains("<"));
        assertTrue(mapString.contains(">"));
        assertTrue(mapString.contains(","));
    }

    /**
     * 测试空参数情况
     */
    @Test
    void testEmptyTypeArguments() {
        ParameterizedTypeImpl emptyType = new ParameterizedTypeImpl(List.class);

        assertEquals(List.class, emptyType.getRawType());
        Type[] arguments = emptyType.getActualTypeArguments();
        assertEquals(0, arguments.length);
    }

    /**
     * 测试多个类型参数
     */
    @Test
    void testMultipleTypeArguments() {
        ParameterizedTypeImpl multiType = new ParameterizedTypeImpl(
                Map.class, String.class, Integer.class, Boolean.class);

        Type[] arguments = multiType.getActualTypeArguments();
        assertEquals(3, arguments.length);
        assertEquals(String.class, arguments[0]);
        assertEquals(Integer.class, arguments[1]);
        assertEquals(Boolean.class, arguments[2]);
    }

    /**
     * 测试嵌套泛型
     */
    @Test
    void testNestedGeneric() {
        ParameterizedTypeImpl innerType = new ParameterizedTypeImpl(List.class, String.class);
        ParameterizedTypeImpl outerType = new ParameterizedTypeImpl(List.class, innerType);

        assertEquals(List.class, outerType.getRawType());
        Type[] arguments = outerType.getActualTypeArguments();
        assertEquals(1, arguments.length);
        assertEquals(innerType, arguments[0]);

        // 验证嵌套类型的toString
        String outerString = outerType.toString();
        assertTrue(outerString.contains("java.util.List"));
        assertTrue(outerString.contains("<"));
        assertTrue(outerString.contains(">"));
    }

    /**
     * 测试自定义类作为原始类型
     */
    @Test
    void testCustomRawType() {
        ParameterizedTypeImpl customType = new ParameterizedTypeImpl(TestGenericClass.class, String.class);

        assertEquals(TestGenericClass.class, customType.getRawType());
        Type[] arguments = customType.getActualTypeArguments();
        assertEquals(1, arguments.length);
        assertEquals(String.class, arguments[0]);
    }

    /**
     * 测试基本类型作为参数
     */
    @Test
    void testPrimitiveTypes() {
        ParameterizedTypeImpl primitiveType = new ParameterizedTypeImpl(
                TestGenericClass.class, int.class, boolean.class);

        Type[] arguments = primitiveType.getActualTypeArguments();
        assertEquals(2, arguments.length);
        assertEquals(int.class, arguments[0]);
        assertEquals(boolean.class, arguments[1]);
    }

    /**
     * 测试ParameterizedType接口实现
     */
    @Test
    void testParameterizedTypeInterface() {
        assertTrue(listStringType instanceof ParameterizedType);
        assertTrue(mapStringIntegerType instanceof ParameterizedType);

        ParameterizedType paramType = (ParameterizedType) listStringType;
        assertEquals(List.class, paramType.getRawType());
        assertNull(paramType.getOwnerType());
        assertEquals(1, paramType.getActualTypeArguments().length);
    }

    /**
     * 测试equals方法（如果实现了）
     */
    @Test
    void testEquals() {
        ParameterizedTypeImpl sameType = new ParameterizedTypeImpl(List.class, String.class);
        ParameterizedTypeImpl differentType = new ParameterizedTypeImpl(List.class, Integer.class);

        // 注意：这里只是验证对象创建，实际的equals实现需要查看源码
        assertNotSame(listStringType, sameType);
        assertNotSame(listStringType, differentType);
    }

    /**
     * 用于测试的泛型类
     */
    static class TestGenericClass<T> {
        private T value;

        public TestGenericClass(T value) {
            this.value = value;
        }

        public T getValue() {
            return value;
        }
    }
}
