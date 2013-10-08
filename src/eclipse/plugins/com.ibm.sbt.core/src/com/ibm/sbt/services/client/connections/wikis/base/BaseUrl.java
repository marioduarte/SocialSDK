package com.ibm.sbt.services.client.connections.wikis.base;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.util.AuthUtil;

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
