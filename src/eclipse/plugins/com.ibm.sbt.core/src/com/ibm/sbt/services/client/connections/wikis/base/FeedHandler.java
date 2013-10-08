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

import org.w3c.dom.Node;

import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.IFeedHandler;

/**
 * @author Mario Duarte
 *
 */
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
