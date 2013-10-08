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

package com.ibm.sbt.services.client.connections.wikis.base;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.util.AuthUtil;

/**
 * @author Mario Duarte
 *
 */
public abstract class BaseUrl {
	protected static char SEPARATOR = ConnectionsConstants.SEPARATOR;
	
	private Endpoint endpoint;
	
	public BaseUrl(Endpoint endpoint) {
		this.endpoint = endpoint;
	}
	
	protected String getAuth() {
		if (null != endpoint && AuthUtil.INSTANCE.getAuthValue(endpoint)
				.equalsIgnoreCase(ConnectionsConstants.OAUTH)) {
			return ConnectionsConstants.OAUTH;
		}
		else {
			return "basic";
		}
	}
	
	public abstract String build();
	
	protected String concatUrlPath(String base, String... pathElements) {
		StringBuilder sb = new StringBuilder(base==null?"":base);
		for(String pathElement : pathElements) {
			if(!StringUtil.isEmpty(pathElement)) {
				sb.append(SEPARATOR).append(pathElement);
			}
		}
		return sb.toString();
	}
}
