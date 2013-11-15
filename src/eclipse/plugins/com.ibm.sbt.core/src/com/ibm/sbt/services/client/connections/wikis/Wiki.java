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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Node;

import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.AtomEntity;
import com.ibm.sbt.services.client.base.BaseService;

/**
 * @author Mario Duarte
 *
 */
public class Wiki extends AtomEntity {

	public Wiki(BaseService service, Node node, NamespaceContext namespaceCtx, 
			XPathExpression xpathExpression) {
		super(service, node, namespaceCtx, xpathExpression);
	}
	
	/**
	 * Unique identifier of a wiki.
	 * @return wikiUuid
	 */
	public String getUuid() {
		return getAsString(WikiXPath.uuid);
	}
	
	/**
	 * Label of a wiki.
	 * @return wikiUuid
	 */
	public String getLabel() {
		return getAsString(WikiXPath.label);
	}
	
	/**
	 * 
	 * @param label
	 */
	public void setLabel(String label) {
		setAsString(WikiXPath.label, label);
	}
	
	/**
	 * Community to which the wiki belongs to
	 * @return communityUuid
	 */
	public String getCommunityUuid() {
		return getAsString(WikiXPath.communityUuid);
	}
	
	/**
	 * 
	 * @return The list of tags of the wiki.
	 */
	public Set<String> getTags() {
		return new HashSet<String>(Arrays.asList(getCategoryTerms()));
	}
	
	/**
	 * 
	 * @param tags
	 */
	public void setTags(Collection<String> tags) {
		super.setCategoryTerms((String[])tags.toArray());
	}
	
	/**
	 * 
	 * @return boolean indication whether or not this wiki is a community wiki.
	 */
	public boolean isCommunityWiki() {
		return getCommunityUuid() != null;
	}
	
	

}
