package com.ibm.sbt.services.client.connections.wikis;

import com.ibm.sbt.services.client.connections.wikis.base.BaseUrl;
import com.ibm.sbt.services.endpoints.Endpoint;

public class WikiUrl extends BaseUrl {
	private boolean anonymous;
	private String wikiId;
	private String wikiFilter;
	
	public WikiUrl(Endpoint endpoint) {
		super(endpoint);
	}

	private String getAnonymous() {
		return anonymous?"anonymous":"";
	}
	
	public WikiUrl anonymous() {
		anonymous = true;
		return this;
	}
	
	public WikiUrl wikis() {
		wikiId = "wikis";
		return this;
	}
	
	public WikiUrl myWikis() {
		wikiId = "mywikis";
		return this;
	}
	
	public WikiUrl wiki(String wikiLabelOrId) {
		wikiId = "wiki" + SEPARATOR + wikiLabelOrId;
		return this;
	}
	
	public WikiUrl feed() {
		wikiFilter = "feed";
		return this;
	}
	
	public WikiUrl publicx() {
		wikiFilter = "public";
		return this;
	}
	
	public WikiUrl mostCommented() {
		wikiFilter = "mostcommented";
		return this;
	}
	
	public WikiUrl mostRecommended() {
		wikiFilter = "mostrecommended";
		return this;
	}
	
	public WikiUrl mostVisited() {
		wikiFilter = "mostvisited";
		return this;
	}
	
	public WikiUrl myPages() {
		wikiFilter = "mypages";
		return this;
	}
	
	@Override
	public String build() {
		return concatUrlPath("wikis", getAuth(), getAnonymous(), "api", wikiId, wikiFilter);
	}
}
