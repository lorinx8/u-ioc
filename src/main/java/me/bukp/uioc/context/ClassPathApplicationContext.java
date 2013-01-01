package me.bukp.uioc.context;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import me.bukp.uioc.common.Constants;
import me.bukp.uioc.common.IocTools;

public class ClassPathApplicationContext extends AbstractApplicationContext {
	
	public ClassPathApplicationContext() {
		InitBeanElements(getClassPathXmlFiles());
		initBeans();
	}

	private void initBeans() {
		Set<String> ids = elementHandler.getBeanElements().keySet();
		for (String id : ids) {
			//不是懒加载，直接创建bean
			if (!this.isLazy(id)) {
				this.createBean(id);
			}
		}
	}
	
	private String[] getClassPathXmlFiles() {
		URL classPathUrl = ClassPathApplicationContext.class.getClassLoader().getResource(".");
		String classPath = classPathUrl.getPath();
		File file = new File(classPath);
		List<String> xmlFiles = new ArrayList<>();
		File[] matched = file.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (dir.isDirectory()) {
					return false;
				}
				name = name.toLowerCase();
				if (!IocTools.getPostfix(name).equals(Constants.FILE_XML_EXTEND)) {
					return false;
				}
				if (!name.startsWith(Constants.FILE_XML_PREFIX)) {
					return false;
				}
				return true;
			}
		});
		for (File f : matched) {
			xmlFiles.add(classPath + f.getName());
		}
		return (String[])xmlFiles.toArray();
	}
}
