package com.ibm.sbt.services.client.connections.wikis.base;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.endpoints.Endpoint;


public abstract class BaseService<T extends BaseEntity> extends com.ibm.sbt.services.client.base.BaseService {

	public BaseService() {
		super();
	}
	
	public BaseService(String endpoint) {
		super(endpoint);
	}
	
	public BaseService(Endpoint endpoint) {
		super(endpoint);
	}
	
	@SuppressWarnings("unchecked")
	protected EntityList<T> getEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return (EntityList<T>)getEntities(requestUrl, getParameters(parameters), getFeedHandler());
	}
	
	protected abstract FeedHandler<T> getFeedHandler();

	private Map<String, String> getParameters(Map<String, String> parameters) {
		if(parameters == null) return new HashMap<String, String>();
		else return parameters;
	}
}
