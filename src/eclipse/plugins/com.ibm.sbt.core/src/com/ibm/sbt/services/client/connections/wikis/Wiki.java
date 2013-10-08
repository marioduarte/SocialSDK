package com.ibm.sbt.services.client.connections.wikis;

import org.w3c.dom.Node;

import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.AtomEntity;
import com.ibm.sbt.services.client.base.BaseService;

public class Wiki extends AtomEntity {

	public Wiki(BaseService service, Node node, NamespaceContext namespaceCtx, 
			XPathExpression xpathExpression) {
		super(service, node, namespaceCtx, xpathExpression);
	}
	
	public String getUuid() {
		return getAsString(WikiXPath.uuid);
	}
	
	public String getCommunityUuid() {
		return getAsString(WikiXPath.communityUuid);
	}
}
