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

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Node;

import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.connections.wikis.base.AtomFeedHandler;
import com.ibm.sbt.services.endpoints.Endpoint;

/**
 * @author Mario Duarte
 *
 */
public class WikiService extends BaseService {
	
	public WikiService() {
		super();
	}
	
	public WikiService(String endpoint) {
		super(endpoint);
	}
	
	public WikiService(Endpoint endpoint) {
		super(endpoint);
	}
	
	/**
	 * This returns a list of wikis to which the authenticated user has access. 
	 * @param parameters 
	 * @return 
	 * @throws ClientServicesException
	 */
	public EntityList<Wiki> getAllWikis(Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = WikiUrls.ALL_WIKIS.format(endpoint);
		return getEntityList(requestUrl, parameters);
	}
	
	/**
	 * This returns a list of wikis to which everyone who can log into the Wikis application has access. 
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<Wiki> getPublicWikis(Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = WikiUrls.PUBLIC_WIKIS.format(endpoint);
		return getEntityList(requestUrl, parameters);
	}
	
	/**
	 * This returns a list of wikis of which the authenticated user is a member. 
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<Wiki> getMyWikis(Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = WikiUrls.MY_WIKIS.format(endpoint);
		return getEntityList(requestUrl, parameters);
	}
	
	/**
	 * This returns a list of wikis to which everyone who can log into the Wikis application has access. 
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<Wiki> getWikisWithMostComments(Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = WikiUrls.MOST_COMMENTED_WIKIS.format(endpoint);
		return getEntityList(requestUrl, parameters);
	}
	
	/**
	 * This returns a list of wikis to which everyone who can log into the Wikis application has access. 
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<Wiki> getWikisWithMostRecommendations(Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = WikiUrls.MOST_RECOMMENDED_WIKIS.format(endpoint);
		return getEntityList(requestUrl, parameters);
	}
	
	/**
	 * This returns a list of wikis to which everyone who can log into the Wikis application has access. 
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<Wiki> getMostVisitedWikis(Map<String, String> parameters) 
			throws ClientServicesException {
		String requestUrl = WikiUrls.MOST_VISITED_WIKIS.format(endpoint);
		return getEntityList(requestUrl, parameters);
	}

	
	
	@SuppressWarnings("unchecked")
	private EntityList<Wiki> getEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return (EntityList<Wiki>)getEntities(requestUrl, getParameters(parameters), getWikiFeedHandler());
	}
	
	private IFeedHandler<Wiki> getWikiFeedHandler() {
		return new AtomFeedHandler<Wiki>(this) {
			@Override
			protected Wiki newEntity(BaseService service, Node node) {
				return new Wiki(service, node, ConnectionsConstants.nameSpaceCtx, null);
			}
		};
	}
	
	private Map<String, String> getParameters(Map<String, String> parameters) {
		if(parameters == null) return new HashMap<String, String>();
		else return parameters;
	}
}
