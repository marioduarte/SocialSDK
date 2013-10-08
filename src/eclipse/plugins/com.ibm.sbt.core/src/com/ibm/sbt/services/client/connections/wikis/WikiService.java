package com.ibm.sbt.services.client.connections.wikis;

import java.util.Map;

import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.connections.wikis.base.BaseService;
import com.ibm.sbt.services.client.connections.wikis.base.EntityList;
import com.ibm.sbt.services.endpoints.Endpoint;


public class WikiService extends BaseService<Wiki> {
	
	public WikiService() {
		super();
	}
	
	public WikiService(String endpoint) {
		super(endpoint);
	}
	
	public WikiService(Endpoint endpoint) {
		super(endpoint);
	}
	
	public EntityList<Wiki> getAllWikis(Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = new WikiUrl(endpoint).wikis().feed().build();
		return getEntityList(requestUrl, parameters);
	}
	
	public EntityList<Wiki> getPublicWikis(Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = new WikiUrl(endpoint).wikis().publicx().build();
		return getEntityList(requestUrl, parameters);
	}
	
	public EntityList<Wiki> getMyWikis(Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = new WikiUrl(endpoint).myWikis().feed().build();
		return getEntityList(requestUrl, parameters);
	}
	
	public EntityList<Wiki> getWikisWithMostComments(Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = new WikiUrl(endpoint).anonymous().wikis().mostCommented().build();
		return getEntityList(requestUrl, parameters);
	}
	
	public EntityList<Wiki> getWikisWithMostRecommendations(Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = new WikiUrl(endpoint).anonymous().wikis().mostRecommended().build();
		return getEntityList(requestUrl, parameters);
	}
	
	public EntityList<Wiki> getMostVisitedWikis(Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = new WikiUrl(endpoint).anonymous().wikis().mostVisited().build();
		return getEntityList(requestUrl, parameters);
	}

	@Override
	protected WikiFeedHandler getFeedHandler() {
		return new WikiFeedHandler(this);
	}
}
