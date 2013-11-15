/*
 * � Copyright IBM Corp. 2013
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.w3c.dom.Node;

import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.AtomFeedHandler;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.connections.wikis.serializers.WikiSerializer;
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
	public EntityList<Wiki> getAllWikis(Map<String, String> parameters) 
			throws ClientServicesException {
		String requestUrl = WikiUrls.ALL_WIKIS.format(endpoint);
		return getWikiEntityList(requestUrl, parameters);
	}
	
	/**
	 * This returns a list of wikis to which everyone who can log into the 
	 * Wikis application has access. 
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<Wiki> getPublicWikis(Map<String, String> parameters) 
			throws ClientServicesException {
		String requestUrl = WikiUrls.PUBLIC_WIKIS.format(endpoint);
		return getWikiEntityList(requestUrl, parameters);
	}
	
	/**
	 * This returns a list of wikis of which the authenticated user is a member. 
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<Wiki> getMyWikis(Map<String, String> parameters) 
			throws ClientServicesException {
		String requestUrl = WikiUrls.MY_WIKIS.format(endpoint);
		return getWikiEntityList(requestUrl, parameters);
	}
	
	/**
	 * This returns a list of wikis to which everyone who can log into the Wikis 
	 * application has access. 
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<Wiki> getWikisWithMostComments(Map<String, String> parameters) 
			throws ClientServicesException {
		String requestUrl = WikiUrls.MOST_COMMENTED_WIKIS.format(endpoint);
		return getWikiEntityList(requestUrl, parameters);
	}
	
	/**
	 * This returns a list of wikis to which everyone who can log into the Wikis 
	 * application has access. 
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<Wiki> getWikisWithMostRecommendations(Map<String, String> parameters) 
			throws ClientServicesException {
		String requestUrl = WikiUrls.MOST_RECOMMENDED_WIKIS.format(endpoint);
		return getWikiEntityList(requestUrl, parameters);
	}
	
	/**
	 * This returns a list of wikis to which everyone who can log into the Wikis 
	 * application has access. 
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<Wiki> getMostVisitedWikis(Map<String, String> parameters)  
			throws ClientServicesException {
		String requestUrl = WikiUrls.MOST_VISITED_WIKIS.format(endpoint);
		return getWikiEntityList(requestUrl, parameters);
	}

	/**
	 * Get a feed that lists all of the pages in a specific wiki. 
	 * @param wikiLabel
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<WikiPage> getWikiPages(String wikiLabel, Map<String, String> parameters) 
			throws ClientServicesException {
		String requestUrl = WikiUrls.WIKI_PAGES.format(endpoint, wikiLabel);
		return getWikiPagesEntityList(requestUrl, parameters);
	}
	
	/**
	 * Get a feed that lists all of the pages in a specific wiki that have been added 
	 * or edited by the authenticated user.
	 * @param wikiLabel
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<WikiPage> getMyWikiPages(String wikiLabel, Map<String, String> parameters) 
			throws ClientServicesException {
		String requestUrl = WikiUrls.WIKI_MYPAGES.format(endpoint, wikiLabel);
		return getWikiPagesEntityList(requestUrl, parameters);
	}
	
	/**
	 * Get a feed that lists the pages that have been deleted from wikis and are currently 
	 * stored in the trash.
	 * @param wikiLabelOrId
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<WikiPage> getWikiPagesInTrash(String wikiLabelOrId, 
			Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = WikiUrls.WIKI_PAGES_TRASH.format(endpoint, wikiLabelOrId);
		return getWikiPagesEntityList(requestUrl, parameters);
	}
	
	
	
	/***************************************************************
	 * Working with wikis 
	 ****************************************************************/
	
	/**
	 * Retrieves a wiki
	 * @param wikiLabel
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public Wiki getWiki(String wikiLabel, Map<String, String> parameters) 
			throws ClientServicesException {
		// FIXME authenticated vs not authenticated request?! (WikiUrls.WIKI vs WikiUrls.WIKI_AUTH)
		String requestUrl = WikiUrls.WIKI.format(endpoint, wikiLabel); 
		return getWikiEntity(requestUrl, parameters);
	}
	
	
	public String createWiki(Wiki wiki) throws ClientServicesException {
		String requestUrl = WikiUrls.ALL_WIKIS.format(endpoint);
		Response response = createWiki(requestUrl, wiki);
		return extractWikiLocation(response);
	}
	
	
	/***************************************************************
	 * Working with wikis 
	 ****************************************************************/
	/**
	 * Retrieves a wiki page.
	 * @param wikiLabel
	 * @param pageLabel
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public WikiPage getWikiPage(String wikiLabel, String pageLabel, 
			Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = WikiUrls.WIKI_PAGE.format(endpoint, wikiLabel, pageLabel);
		return getWikiPageEntity(requestUrl, parameters);
	}
	
	
	
	
	/***************************************************************
	 * Utility methods
	 ****************************************************************/
	
	private Response createWiki(String requestUrl, Wiki wiki) throws ClientServicesException {
		try {
			WikiSerializer serializer = new WikiSerializer(wiki);
			serializer.create();
			return createData(requestUrl, null, serializer.serealizeToString());
		}
		catch(ClientServicesException e) {
			throw e;
		}
		catch(Exception e) {
			throw new ClientServicesException(e);
		}
	}
	
	private String extractWikiLocation(Response response){
		Header locationHeader = response.getResponse().getFirstHeader("Location");
		if(locationHeader != null) {
			String location = locationHeader.getValue();
			// FIXME
		}
		return null;
	}
	
	private EntityList<Wiki> getWikiEntityList(String requestUrl, 
			Map<String, String> parameters) throws ClientServicesException {
		try {
			return (EntityList<Wiki>)getEntities(requestUrl, getParameters(parameters), 
					getWikiFeedHandler());
		} catch (IOException e) {
			throw new ClientServicesException(e);
		}
	}
	
	private Wiki getWikiEntity(String requestUrl, Map<String, String> parameters) 
			throws ClientServicesException {
		try {
			return (Wiki)getEntity(requestUrl, parameters, getWikiFeedHandler());
		} catch (IOException e) {
			throw new ClientServicesException(e);
		}
	}
	
	private EntityList<WikiPage> getWikiPagesEntityList(String requestUrl, 
			Map<String, String> parameters) throws ClientServicesException {
		try {
			return (EntityList<WikiPage>)getEntities(requestUrl, 
					getParameters(parameters), getWikiPagesFeedHandler());
		} catch (IOException e) {
			throw new ClientServicesException(e);
		}
	}
	
	private WikiPage getWikiPageEntity(String requestUrl, Map<String, String> parameters) 
			throws ClientServicesException {
		try {
			return (WikiPage)getEntity(requestUrl, parameters, getWikiPagesFeedHandler());
		} catch (IOException e) {
			throw new ClientServicesException(e);
		}
	}
	
	private IFeedHandler<Wiki> getWikiFeedHandler() {
		return new AtomFeedHandler<Wiki>(this) {
			@Override
			protected Wiki newEntity(BaseService service, Node node) {
				return new Wiki(service, node, ConnectionsConstants.nameSpaceCtx, null);
			}
		};
	}
	
	private IFeedHandler<WikiPage> getWikiPagesFeedHandler() {
		return new AtomFeedHandler<WikiPage>(this) {
			@Override
			protected WikiPage newEntity(BaseService service, Node node) {
				return new WikiPage(service, node, ConnectionsConstants.nameSpaceCtx, null);
			}
		};
	}
	
	private Map<String, String> getParameters(Map<String, String> parameters) {
		if(parameters == null) return new HashMap<String, String>();
		else return parameters;
	}
}