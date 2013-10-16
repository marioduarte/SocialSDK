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

package com.ibm.sbt.services.client.connections.wikis.exceptions;

import java.lang.reflect.Constructor;

import com.ibm.commons.util.AbstractException;
import com.ibm.sbt.services.client.ClientServicesException;



/**
 * @author Mario Duarte
 *
 */
public class RestServiceException extends Exception {

	private int statusCode;
	private String message;
	
	protected RestServiceException() {
	}

	public static RestServiceException newInstance(ClientServicesException e) {
		int responseStatusCode = e.getResponseStatusCode();
		return newInstance(responseStatusCode);
	}

	private static RestServiceException newInstance(int statusCode) {
		Class<? extends RestServiceException> exceptionClass;
		switch(statusCode) {
			case 401 : exceptionClass = UnauthenticatedException.class; break;
			case 403 : exceptionClass = UnauthorizedException.class; break;
			case 404 : exceptionClass = ResourceNotFoundException.class; break;
			default  : exceptionClass = RestServiceException.class; break;
		}
		Constructor<? extends RestServiceException> constructor;
		try {
			constructor = exceptionClass.getConstructor();
			return constructor.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	/**
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return statusCode;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return super.getMessage();
	}
	
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
