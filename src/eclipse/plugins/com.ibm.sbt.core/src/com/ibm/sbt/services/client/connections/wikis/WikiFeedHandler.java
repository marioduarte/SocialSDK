package com.ibm.sbt.services.client.connections.wikis;

import org.w3c.dom.Node;

import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.connections.wikis.base.FeedHandler;

public class WikiFeedHandler extends FeedHandler<Wiki> {

	public WikiFeedHandler(BaseService service) {
		super(service);
	}

	@Override
	protected Wiki newEntity(BaseService service, Node node) {
		return new Wiki(service, node, ConnectionsConstants.nameSpaceCtx, null);
	}
}
