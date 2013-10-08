package com.ibm.sbt.services.client.connections.wikis.base;

import org.w3c.dom.Node;

import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.IFeedHandler;

public abstract class FeedHandler<T extends BaseEntity> implements IFeedHandler{

	private BaseService service;
	
	public FeedHandler(BaseService service) {
		this.service = service;
	}
	
	@Override
	public T createEntity(Response dataHolder) {
		Node data = (Node)dataHolder.getData();
		return createEntityFromData(data);
	}

	@Override
	public T createEntityFromData(Object data) {
		return newEntity(service, (Node)data);
	}

	@Override
	public EntityList<T> createEntityList(Response dataHolder) {
		return new EntityList<T>(dataHolder, this);
	}

	@Override
	public BaseService getService() {
		return service;
	}

	protected abstract T newEntity(BaseService service, Node node);

}
