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

import org.w3c.dom.Node;

import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.connections.wikis.base.FeedHandler;

/**
 * @author Mario Duarte
 *
 */
public class WikiFeedHandler extends FeedHandler<Wiki> {

	public WikiFeedHandler(BaseService service) {
		super(service);
	}

	@Override
	protected Wiki newEntity(BaseService service, Node node) {
		return new Wiki(service, node, ConnectionsConstants.nameSpaceCtx, null);
	}
}
