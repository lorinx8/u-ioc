package me.bukp.uioc.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import me.bukp.uioc.context.BeanCreator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BeanCreateTest {
	
	private final String classname = "me.bukp.uioc.test.User";

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void createTest() {
		Object obj = BeanCreator.createBean(classname);
		assertNotNull(obj);
		assertTrue((User)obj instanceof User);
		System.out.println(obj);
	}
	
	@Test
	public void createWithArgsTest() {
		List<Object> args = new ArrayList<>();
		args.add(1);
		args.add("lorin");
		Object obj = BeanCreator.createBean(classname, args);
		
		assertNotNull(obj);
		assertTrue((User)obj instanceof User);
		User user = (User)obj;
		assertTrue(user.getId().equals(1));
		assertEquals("lorin", user.getName());
		System.out.println(obj);
	}
}
