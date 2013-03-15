u-ioc
========

## 介绍

仿照Spring实现的一个简易的IOC容器。完成了控制反转、依赖注入的基本功能。

## 功能

* 使用xml文件进行bean配置，xml文件结构与spring bean配置文件相似
* 实现了最基本的bean容器功能
* 实现构造注入和属性注入功能
* 实现ByName形式的自动注入功能
* 实现懒加载功能

## 使用方法

1. 编写bean定义文件，放置于classpath路径中
2. 在程序代码中通过 `ApplicationContext context = new ClassPathApplicationContext();` 得到上下文环境
3. 使用ApplicationContext提供的getBean()方法即可获得相应的对象
4. Have fun！

## 示例

以下示例展示带引用的构造注入

bean定义文件如下
```xml
<bean class="me.bukp.uioc.test.object.ClassRoom" id="classroom">
  	<constructor-arg>
			<value type="java.lang.String">moon</value>
		</constructor-arg>
		<constructor-arg>
			<ref bean="teacher" />
		</constructor-arg>
</bean>
```

测试代码
```java
@Test
public void test() {
	ClassRoom classroom = (ClassRoom)context.getBean("classroom");
	assertEquals("moon", classroom.getRoomId());
	assertEquals(new Integer(1), classroom.getTeacher().getId());
	assertEquals("lorin", classroom.getTeacher().getName());
}
```
