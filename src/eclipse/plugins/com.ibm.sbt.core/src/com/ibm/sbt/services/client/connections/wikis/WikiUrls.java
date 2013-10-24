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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.util.AuthUtil;

/**
 * @author Mario Duarte
 *
 */
public enum WikiUrls {
	ALL_WIKIS("wikis/{0}/api/wikis/feed"),
	PUBLIC_WIKIS("wikis/{0}/api/wikis/public"),
	MY_WIKIS("wikis/{0}/api/mywikis/feed"),
	MOST_COMMENTED_WIKIS("wikis/{0}/anonymous/api/wikis/mostcommented"),
	MOST_RECOMMENDED_WIKIS("wikis/{0}/anonymous/api/wikis/mostrecommended"),
	MOST_VISITED_WIKIS("wikis/{0}/anonymous/api/wikis/mostvisited"),
	WIKI_PAGES("wikis/{0}/anonymous/api/wiki/{1}/feed");
	
	private String urlPattern;
	
	private WikiUrls(String urlPattern) {
		this.urlPattern = urlPattern;
	}
	
	public String format(String... args) {
		return formatPattern((Object[])args);
	}
	
	public String format(Endpoint endpoint, String... args) {
		return formatPattern(getAuth(endpoint), args);
	}
	
	private String formatPattern(Object... args) {
		for(int i=0; i<args.length; i++) {
			try {
				args[i] = URLEncoder.encode(args[i].toString(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}
		return StringUtil.format(urlPattern, (Object[])args);
	}
	
	private String getAuth(Endpoint endpoint) {
		 return AuthUtil.INSTANCE.getAuthValue(endpoint);
	}
}
