package com.ibm.sbt.services.client.connections.wikis.base;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.ConnectionsFeedXpath;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.activity.Activity;
import com.ibm.sbt.services.client.connections.activity.model.ActivityXPath;

public class EntityList<T extends BaseEntity> extends com.ibm.sbt.services.client.base.datahandlers.EntityList<T> {

	public EntityList(Response requestData, IFeedHandler feedHandler) {
		super(requestData, feedHandler);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected ArrayList<T> createEntities() {
		ArrayList<T> entries = new ArrayList<T>();
		List<Node> nodeEntries = getDataHandler().getEntries(ConnectionsFeedXpath.Entry);
		for (Node node: nodeEntries) {
			entries.add((T)super.getEntity(node));
		}
		return entries;
	}
	
	@Override
	public Document getData(){
		return (Document)super.getData();
	}

	protected XmlDataHandler getDataHandler() {
		return new XmlDataHandler(getData(), ConnectionsConstants.nameSpaceCtx);
	}
	
	@Override
	public int getTotalResults() {
		return getDataHandler().getAsInt(ConnectionsFeedXpath.TotalResults);
	}

	@Override
	public int getStartIndex() {
		return getDataHandler().getAsInt(ConnectionsFeedXpath.StartIndex);
	}

	@Override
	public int getItemsPerPage() {
		return getDataHandler().getAsInt(ConnectionsFeedXpath.ItemsPerPage);
	}

	@Override
	public int getCurrentPage() {
		return getDataHandler().getAsInt(ConnectionsFeedXpath.CurrentPage);
	}
}
