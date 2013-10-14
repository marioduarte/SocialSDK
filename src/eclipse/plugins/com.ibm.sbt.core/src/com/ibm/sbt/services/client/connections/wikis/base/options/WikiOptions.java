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

package com.ibm.sbt.services.client.connections.wikis.base.options;

import com.ibm.sbt.services.client.connections.wikis.base.BaseOptions;

/**
 * @author Mario Duarte
 *
 */
public class WikiOptions extends BaseOptions {

	public enum ROLE { EDITOR, MANAGER, READER};
	
	public enum SORT_BY { COMMENTED, CREATED, DESC, DOWNLOADED, MODIFIED, PUBLISHED, RECOMMENDED, TITLE, UPDATED } 
	
	public enum SORT_ORDER { ASC, DESC } 
	
	/**
	 * Specifies whether or not the permissions for each user should be displayed for each entry in 
	 * the returned Atom document. This parameter takes a Boolean value of either true or false. 
	 * By default, the permission information is not returned.
	 * @param showAcls
	 * @return
	 */
	public WikiOptions acls(boolean retrieveAcls) {
		return setParam("acls", retrieveAcls);
	}
	
	/**
	 * Specifies whether or not the tags that are displayed on the wiki welcome page are included in 
	 * the returned Atom document. This parameter takes a Boolean value of either true or false. 
	 * By default, the tags are not returned.
	 * @param showAcls
	 * @return
	 */
	public WikiOptions includeTags(boolean showAcls) {
		return setParam("acls", showAcls);
	}
	
	/**
	 * 	Limits the wikis returned to only those that the authenticated user has the specified role in. 
	 * Options are: 
	 * <ul>
	 * <li>editor
	 * <li>manager
	 * <li>reader
	 * </ul>
	 * The default value is reader.
	 * @param role
	 * @return
	 */
	public WikiOptions role(ROLE role) {
		return setParam("role", role);
	}
	
	/**
	 * Page number. Specifies the page to be returned. The default value is 1, which returns the first page.
	 * @param pageNumber
	 * @return
	 */
	public WikiOptions page(int pageNumber) {
		if(pageNumber < 1) {
			throw new IllegalArgumentException("The page number has to be bigger than zero.");
		}
		return setParam("page", pageNumber);
	}
	
	/**
	 * Page size. Specifies the number of entries to return per page. The default value is 10. 
	 * The maximum value you can specify is 500.
	 * @param pageSize
	 * @return
	 */
	public WikiOptions pageSize(int pageSize) {
		if(pageSize < 1 || pageSize > 500) {
			throw new IllegalArgumentException(
					"The page size has to be bigger than zero and smaller than 500.");
		}
		return setParam("ps", pageSize);
	}
	
	/**
	 * Specifies what to sort the returned entries by. Options are:
	 * <ul>
	 * <li>commented - Sorts the entries by the number of comments or replies an item has received.
	 * <li>created - Sorts the entries by the date the item was created.
	 * <li>desc - Sorts the entries alphabetically by description.
	 * <li>downloaded - Sorts the entries by the number of times the item was downloaded. In the context of a wiki, this means the number of times a wiki page was viewed.
	 * <li>modified - Sorts the entries by the last modified date.
	 * <li>published - Sorts the entries by the date the item was published (usually related to atom:published element).
	 * <li>recommended - Sorts the entries by the number of times the item was recommended.
	 * <li>title - Sorts the entries alphabetically by title. The title used is the text that is displayed in the &lt;title&gt; element of each entry in the feed.
	 * <li>updated - Sorts the entries by the last time the item was updated.
	 * </ul>
	 * @param sortBy
	 * @return
	 */
	public WikiOptions sortBy(SORT_BY sortBy) {
		return setParam("sortBy", sortBy);
	}
	
	/**
	 * Specifies the order in which to sort the results. The options are:
	 * <ul>
	 * <li>asc - Sorts the results in ascending order.
	 * <li>desc - Sorts the results in descending order.
	 * </ul>
	 * Descending is the default value.
	 * @param sortOrder
	 * @return
	 */
	public WikiOptions sortOder(SORT_ORDER sortOrder) {
		return setParam("sortOrder", sortOrder);
	}
}
