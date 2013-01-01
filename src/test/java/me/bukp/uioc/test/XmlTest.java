package me.bukp.uioc.test;

import static org.junit.Assert.*;

import java.net.URL;

import me.bukp.uioc.xml.BeanElementHandler;

import org.dom4j.Element;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class XmlTest {
	
	BeanElementHandler elementHandler;
	
	@Before
	public void setUp() throws Exception {
		URL url = XmlTest.class.getClassLoader().getResource(".");
		String xmlfile = url.getPath() + "ioctest.xml";
		elementHandler = new BeanElementHandler(xmlfile);
	}

	@After
	public void tearDown() throws Exception {
		
	}

	@Test
	public void elementInitTest() {
		assertEquals(8, elementHandler.getBeanElements().keySet().size());
	}
	
	@Test
	public void getElementTest() {
		Element e = elementHandler.getBeanElement("test1");
		assertEquals("bean", e.getName());
		assertEquals("test1", e.attributeValue("id"));
		assertEquals("me.bukp.test.Object1", elementHandler.getBeanClassName(e));
	}
	
	@Test
	public void lazyTest() {
		Element e1 = elementHandler.getBeanElement("lazytest1");
		Element e2 = elementHandler.getBeanElement("lazytest2");
		Element e3 = elementHandler.getBeanElement("lazytest3");
		assertEquals(true, elementHandler.isLazy(e1));
		assertEquals(false, elementHandler.isLazy(e2));
		assertEquals(false, elementHandler.isLazy(e3));
	}
	
	@Test
	public void singletonTest() {
		Element e1 = elementHandler.getBeanElement("singletontest1");
		Element e2 = elementHandler.getBeanElement("singletontest2");
		Element e3 = elementHandler.getBeanElement("singletontest3");
		assertEquals(true, elementHandler.isSingleton(e1));
		assertEquals(false, elementHandler.isSingleton(e2));
		assertEquals(true, elementHandler.isSingleton(e3));
	}
}
