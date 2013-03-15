package me.bukp.uioc.test;

import static org.junit.Assert.*;

import me.bukp.uioc.context.ApplicationContext;
import me.bukp.uioc.context.ClassPathApplicationContext;
import me.bukp.uioc.test.object.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ApplicationContextTest {
	
	ApplicationContext context;

	@Before
	public void setUp() throws Exception {
		context = new ClassPathApplicationContext();
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * 最基本的测试，得到容器中一个对象，其类只含一个默认构造函数
	 */
	@Test
	public void test1() {
		Object obj = context.getBean("test1");
		assertTrue(obj instanceof Test1);
	}
	
	/**
	 * 测试单例及非单例
	 */
	@Test
	public void test2() {
		Object obj1 = context.getBean("test1");
		Object obj2 = context.getBean("test1");
		assertTrue(obj1 == obj2);
		
		Object obj3 = context.getBean("test2");
		Object obj4 = context.getBean("test2");
		assertTrue(obj3 != obj4);
	}
	
	/**
	 * 简单构造注入
	 */
	@Test
	public void test3() {
		Teacher teacher = (Teacher)context.getBean("teacher");
		assertEquals(new Integer(1), teacher.getId());
		assertEquals("lorin", teacher.getName());
	}
	
	/**
	 * 带引用的构造注入
	 */
	@Test
	public void test4() {
		ClassRoom classroom = (ClassRoom)context.getBean("classroom");
		assertEquals("moon", classroom.getRoomId());
		assertEquals(new Integer(1), classroom.getTeacher().getId());
		assertEquals("lorin", classroom.getTeacher().getName());
	}
	
	/**
	 * 自动装配
	 */
	@Test
	public void test5() {
		ClassRoom autoroom = (ClassRoom)context.getBean("autoroom");
		assertEquals(new Integer(1), autoroom.getTeacher().getId());
		assertEquals("lorin", autoroom.getTeacher().getName());
	}
	
	/**
	 * 懒加载
	 */
	@Test
	public void test6() {
		Teacher teacher = (Teacher)context.getBean("lazyteacher");
		assertNull(teacher);
		teacher = (Teacher)context.getBean("lazyteacher", true);
		assertNotNull(teacher);
	}
	
	/**
	 * 属性注入
	 */
	@Test
	public void test7() {
		Teacher teacher = (Teacher)context.getBean("propteacher");
		assertEquals(new Integer(2), teacher.getId());
		assertEquals("cheerup", teacher.getName());
	}
}
