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

/**
 * @author Mario Duarte
 *
 */
public abstract class BaseOptions {
	private Map<String,String> params;
	
	public BaseOptions() {
		this.params = new HashMap<String, String>();
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends BaseOptions> T setParam(String key, Object value) {
		params.put(key, value.toString());
		return (T)this;
	}
	
	/**
	 * @return the params
	 */
	public Map<String, String> toMap() {
		return params;
	}
	
	/**
	 * 
	 * @param params
	 */
	public void loadMap(Map<String,String> params) {
		this.params = params;
	}
}
