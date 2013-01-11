package me.bukp.uioc.context;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import me.bukp.uioc.common.Constants;
import me.bukp.uioc.common.IocTools;

public class ClassPathApplicationContext extends AbstractApplicationContext {
	
	public ClassPathApplicationContext() {
		InitBeanElements(this.getClassPathXmlFiles());
		initBeans();
	}

	/**
	 * 初始化非懒加载的bean
	 */
	private void initBeans() {
		Set<String> ids = elementHandler.getBeanElements().keySet();
		for (String id : ids) {
			//不是懒加载，直接创建bean
			if (!this.isLazy(id)) {
				this.createBean(id);
			}
		}
	}
	
	/**
	 * 得到classpath中的xml文件
	 * @return xml文件全路径字符串数组
	 */
	private String[] getClassPathXmlFiles() {
		URL classPathUrl = ClassPathApplicationContext.class.getClassLoader().getResource(".");
		String classPath = classPathUrl.getPath();
		File file = new File(classPath);
		List<String> xmlFiles = new ArrayList<>();
		File[] matched = file.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if (pathname.isDirectory()) {
					return false;
				}
				String filename  = pathname.getName().toLowerCase();
				if (!IocTools.getPostfix(filename).equals(Constants.FILE_XML_EXTEND)) {
					return false;
				}
				if (!filename.startsWith(Constants.FILE_XML_PREFIX)) {
					return false;
				}
				return true;
			}
		});
		for (File f : matched) {
			xmlFiles.add(classPath + f.getName());
		}
		String[] paths = new String[xmlFiles.size()];
		return xmlFiles.toArray(paths);
	}
}
