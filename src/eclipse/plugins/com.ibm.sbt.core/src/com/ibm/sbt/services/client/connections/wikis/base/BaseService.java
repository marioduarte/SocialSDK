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

import java.util.HashMap;
import java.util.Map;

import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.endpoints.Endpoint;

/**
 * @author Mario Duarte
 *
 */
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
	
	protected Map<String, String> getParameters(Map<String, String> parameters) {
		if(parameters == null) return new HashMap<String, String>();
		else return parameters;
	}
}
