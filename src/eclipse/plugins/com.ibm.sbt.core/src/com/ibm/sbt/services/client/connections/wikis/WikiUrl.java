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

import com.ibm.sbt.services.client.connections.wikis.base.BaseUrl;
import com.ibm.sbt.services.endpoints.Endpoint;

/**
 * @author Mario Duarte
 *
 */
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
