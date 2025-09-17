/**
 * Description 注解测试类
 *
 * @author 苍镜月
 * @since 2.12.1
 */
package com.yomahub.liteflow.test.ai.engine.model.output.structure;

import com.yomahub.liteflow.ai.engine.model.output.structure.Description;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class DescriptionTest {

    /**
     * 测试类级别的Description注解
     */
    @Description(value = { "测试用户类", "包含用户基本信息" }, required = true)
    static class TestUser {
        @Description(value = { "用户名" }, required = true)
        private String name;

        @Description(value = { "用户年龄", "可选字段" }, required = false)
        private Integer age;

        @Description(value = { "用户邮箱" })
        private String email;

        private String phone; // 没有Description注解
    }

    /**
     * 测试注解的基本功能
     */
    @Test
    void testAnnotationBasics() {
        // 测试类级别注解
        Description classAnnotation = TestUser.class.getAnnotation(Description.class);
        assertNotNull(classAnnotation);

        String[] values = classAnnotation.value();
        assertEquals(2, values.length);
        assertEquals("测试用户类", values[0]);
        assertEquals("包含用户基本信息", values[1]);
        assertTrue(classAnnotation.required());
    }

    /**
     * 测试字段级别的注解
     */
    @Test
    void testFieldAnnotations() throws NoSuchFieldException {
        // 测试name字段
        Field nameField = TestUser.class.getDeclaredField("name");
        Description nameAnnotation = nameField.getAnnotation(Description.class);
        assertNotNull(nameAnnotation);
        assertEquals(1, nameAnnotation.value().length);
        assertEquals("用户名", nameAnnotation.value()[0]);
        assertTrue(nameAnnotation.required());

        // 测试age字段
        Field ageField = TestUser.class.getDeclaredField("age");
        Description ageAnnotation = ageField.getAnnotation(Description.class);
        assertNotNull(ageAnnotation);
        assertEquals(2, ageAnnotation.value().length);
        assertEquals("用户年龄", ageAnnotation.value()[0]);
        assertEquals("可选字段", ageAnnotation.value()[1]);
        assertFalse(ageAnnotation.required());

        // 测试email字段（使用默认required=true）
        Field emailField = TestUser.class.getDeclaredField("email");
        Description emailAnnotation = emailField.getAnnotation(Description.class);
        assertNotNull(emailAnnotation);
        assertEquals(1, emailAnnotation.value().length);
        assertEquals("用户邮箱", emailAnnotation.value()[0]);
        assertTrue(emailAnnotation.required()); // 默认值
    }

    /**
     * 测试没有注解的字段
     */
    @Test
    void testFieldWithoutAnnotation() throws NoSuchFieldException {
        Field phoneField = TestUser.class.getDeclaredField("phone");
        Description phoneAnnotation = phoneField.getAnnotation(Description.class);
        assertNull(phoneAnnotation);
    }

    /**
     * 测试注解的Target和Retention
     */
    @Test
    void testAnnotationMetadata() {
        Description annotation = TestUser.class.getAnnotation(Description.class);
        assertNotNull(annotation);

        // 验证注解类型
        Class<? extends Annotation> annotationType = annotation.annotationType();
        assertEquals(Description.class, annotationType);

        // 验证注解可以在运行时获取（RUNTIME retention）
        assertTrue(TestUser.class.isAnnotationPresent(Description.class));
    }

    /**
     * 测试注解的默认值
     */
    @Test
    void testDefaultValues() throws NoSuchFieldException {
        Field emailField = TestUser.class.getDeclaredField("email");
        Description emailAnnotation = emailField.getAnnotation(Description.class);

        // required的默认值应该是true
        assertTrue(emailAnnotation.required());
    }

    /**
     * 测试空值处理
     */
    @Description(value = {})
    static class EmptyDescriptionClass {
        @Description(value = { "" })
        private String emptyDescription;
    }

    @Test
    void testEmptyDescription() throws NoSuchFieldException {
        // 测试空数组
        Description classAnnotation = EmptyDescriptionClass.class.getAnnotation(Description.class);
        assertNotNull(classAnnotation);
        assertEquals(0, classAnnotation.value().length);
        assertTrue(classAnnotation.required()); // 默认值

        // 测试空字符串
        Field field = EmptyDescriptionClass.class.getDeclaredField("emptyDescription");
        Description fieldAnnotation = field.getAnnotation(Description.class);
        assertNotNull(fieldAnnotation);
        assertEquals(1, fieldAnnotation.value().length);
        assertEquals("", fieldAnnotation.value()[0]);
    }

    /**
     * 测试多个描述值
     */
    @Description(value = { "第一个描述", "第二个描述", "第三个描述" })
    static class MultipleDescriptionClass {
    }

    @Test
    void testMultipleDescriptions() {
        Description annotation = MultipleDescriptionClass.class.getAnnotation(Description.class);
        assertNotNull(annotation);

        String[] values = annotation.value();
        assertEquals(3, values.length);
        assertEquals("第一个描述", values[0]);
        assertEquals("第二个描述", values[1]);
        assertEquals("第三个描述", values[2]);
    }
}
