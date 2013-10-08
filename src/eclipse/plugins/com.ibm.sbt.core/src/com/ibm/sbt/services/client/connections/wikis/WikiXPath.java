package com.ibm.sbt.services.client.connections.wikis;

import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.datahandlers.FieldEntry;

public enum WikiXPath implements FieldEntry {
	entry("/a:entry"),
	uuid("./td:uuid"),
	label("./td:label"),
	title("./a:title"),
	published("./a:published"),
	updated("./a:updated"),
	created("./td:created"),
	modified("./td:modified"),
	authorUid("./a:author/snx:userid"),
	authorName("./a:author/a:name"),
	authorEmail("./a:author/a:email"),
	modifierUid("./td:modifier/snx:userid"),
	modifierName("./td:modifier/a:name"),
	modifierEmail("./td:modifier/a:email"),
	summary("./a:summary[@type='text']"),
	content("./a:content[@type='text/html']"),
	permissions("./td:permissions"),
	communityUuid("./snx:communityUuid")
	;
	
	private final XPathExpression path;
	
	private WikiXPath(String xpath) {
		XPathExpression xpathExpr = null;
		try {
			xpathExpr = DOMUtil.createXPath(xpath);
		} catch (XMLException e) {
			e.printStackTrace();
		}
		this.path = xpathExpr;
	}
	
	@Override
	public XPathExpression getPath() {
		return path;
	}
	
	@Override
	public String getName() {
		return this.name();
	}
}
