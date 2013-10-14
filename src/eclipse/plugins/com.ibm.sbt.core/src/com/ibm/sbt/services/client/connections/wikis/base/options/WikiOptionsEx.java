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

/**
 * @author Mario Duarte
 *
 */
public class WikiOptionsEx extends WikiOptions {
	
	/**
	 * Specify a string to use in a wiki search by title. The string length must be greater than 1.
	 * @param searchString
	 * @return
	 */
	protected WikiOptions search(String searchString) {
		if(searchString.length() < 2) {
			throw new IllegalArgumentException(
					"The string length must be greater than 1.");
		}
		return setParam("ps", searchString);
	}
}
