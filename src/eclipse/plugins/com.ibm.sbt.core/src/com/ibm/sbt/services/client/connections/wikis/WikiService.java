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

import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.connections.wikis.base.options.WikiOptions;
import com.ibm.sbt.services.client.connections.wikis.base.options.WikiOptionsEx;
import com.ibm.sbt.services.client.connections.wikis.exceptions.RestServiceException;
import com.ibm.sbt.services.client.connections.wikis.exceptions.UnauthenticatedException;
import com.ibm.sbt.services.client.connections.wikis.exceptions.UnauthorizedException;
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
	 * @param options Optional parameters. null means no optional parameters.
	 * @return 
	 * @throws UnauthenticatedException 
	 * @throws UnauthorizedException 
	 */
	public EntityList<Wiki> getAllWikis(WikiOptionsEx options) 
			throws UnauthorizedException, UnauthenticatedException {
		String requestUrl = new WikiUrl(endpoint).wikis().feed().build();
		return getEntityList(requestUrl, options);
	}
	
	/**
	 * This returns a list of wikis to which everyone who can log into the Wikis application has access. 
	 * @param options Optional parameters. null means no optional parameters.
	 * @return
	 * @throws UnauthenticatedException 
	 * @throws UnauthorizedException 
	 */
	public EntityList<Wiki> getPublicWikis(WikiOptions options) 
			throws UnauthorizedException, UnauthenticatedException {
		String requestUrl = new WikiUrl(endpoint).wikis().publicx().build();
		return getEntityList(requestUrl, options);
	}
	
	/**
	 * This returns a list of wikis of which the authenticated user is a member. 
	 * @param options Optional parameters. null means no optional parameters.
	 * @return
	 * @throws UnauthenticatedException 
	 * @throws UnauthorizedException 
	 */
	public EntityList<Wiki> getMyWikis(WikiOptions options) 
			throws UnauthorizedException, UnauthenticatedException {
		String requestUrl = new WikiUrl(endpoint).myWikis().feed().build();
		return getEntityList(requestUrl, options);
	}
	
	/**
	 * This returns a list of wikis to which everyone who can log into the Wikis application has access. 
	 * @param options Optional parameters. null means no optional parameters.
	 * @return
	 * @throws UnauthenticatedException 
	 * @throws UnauthorizedException 
	 */
	public EntityList<Wiki> getWikisWithMostComments(WikiOptions options) 
				throws UnauthorizedException, UnauthenticatedException {
		String requestUrl = new WikiUrl(endpoint).anonymous().wikis().mostCommented().build();
		return getEntityList(requestUrl, options);
	}
	
	/**
	 * This returns a list of wikis to which everyone who can log into the Wikis application has access. 
	 * @param options Optional parameters. null means no optional parameters.
	 * @return
	 * @throws UnauthenticatedException 
	 * @throws UnauthorizedException 
	 */
	public EntityList<Wiki> getWikisWithMostRecommendations(WikiOptions options) 
				throws UnauthorizedException, UnauthenticatedException {
		String requestUrl = new WikiUrl(endpoint).anonymous().wikis().mostRecommended().build();
		return getEntityList(requestUrl, options);
	}
	
	/**
	 * This returns a list of wikis to which everyone who can log into the Wikis application has access. 
	 * @param options Optional parameters. null means no optional parameters.
	 * @return
	 * @throws UnauthenticatedException 
	 * @throws UnauthorizedException 
	 */
	public EntityList<Wiki> getMostVisitedWikis(WikiOptions options) 
				throws UnauthorizedException, UnauthenticatedException {
		String requestUrl = new WikiUrl(endpoint).anonymous().wikis().mostVisited().build();
		return getEntityList(requestUrl, options);
	}
	
	@SuppressWarnings("unchecked")
	protected EntityList<Wiki> getEntityList(String requestUrl, WikiOptions options) 
			throws RestServiceException {
		try {
			return (EntityList<Wiki>)getEntities(requestUrl, convertOptions(options), getWikiFeedHandler());
		}
		catch(ClientServicesException e) {
			throw RestServiceException.newInstance(e);
		}
	}

	private Map<String, String> convertOptions(WikiOptions options) {
		if(options == null) return new HashMap<String, String>();
		else return options.toMap();
	}

	protected IFeedHandler getWikiFeedHandler() {
		return new WikiFeedHandler(this);
	}
}
