/*
 * © Copyright IBM Corp. 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package com.ibm.sbt.services.client.connections.wikis;

import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.datahandlers.FieldEntry;

/**
 * @author Mario Duarte
 *
 */
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
