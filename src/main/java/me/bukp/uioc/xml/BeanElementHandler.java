package me.bukp.uioc.xml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.dom4j.Document;
import org.dom4j.Element;

public class BeanElementHandler extends ElementHandler {
	
	protected Map<String, Element> beanElmentsMap = new HashMap<>();
	
	public BeanElementHandler() {
	}
	
	public BeanElementHandler(String... filePaths) {
		List<Document> docs = getDocument(filePaths);
		for (int i = 0; i < docs.size(); i++) {
			addBeanElements(docs.get(i));
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private void addBeanElements(Document doc) {
		List<Element> es = doc.getRootElement().elements();
		for (Element e : es) {
			beanElmentsMap.put(getElementAttributeValue(e, "id"), e);
		}
	}
	
	public boolean isLazy(Element e) {
		String lazy = getElementAttributeValue(e, "lazy-init");	
		Element p = e.getParent();
		String pLzay = getElementAttributeValue(p, "default-lazy-init");
		if (lazy.equals("true")) {
			return true;
		}
		
		if (pLzay.equals("true")) {
			return true;
		}	
		return false;
	}
	
	
	
	

	
	
}
