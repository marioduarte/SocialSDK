/*
 * � Copyright IBM Corp. 2012
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
package com.ibm.sbt.jslibrary.servlet;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.commons.Platform;
import com.ibm.commons.runtime.util.UrlUtil;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.jslibrary.SBTEnvironment;
import com.ibm.sbt.jslibrary.SBTEnvironmentFactory;

/**
 * Wrapper object to encapsulate a request to the library servlet.
 * 
 * @author priand
 * @author mwallace
 */
public class LibraryRequest {

	private boolean inited;
	
	protected HttpServletRequest httpRequest;
	protected HttpServletResponse httpResponse;

	protected String 	jsLib;
	protected String 	jsVersion;
	protected String 	toolkitUrl;
	protected String 	toolkitJsUrl;
	protected String 	serviceUrl;
	protected String 	iframeUrl;
	protected Context   context;

	protected SBTEnvironment environment;
	
	// List of the default endpoints being added by default.
	public static final String DEFAULT_JSLIB 		= "dojo";
	public static final String DEFAULT_VERSION 		= "1.4";
	
	// Definition of the servlet parameters (query string)
	public static final String PARAM_ENDPOINTS 			= "endpoints";	// List of endpoints to generate
	public static final String PARAM_CLIENT_PROPERTIES	= "props";	// List of client proeprties to generate
	public static final String PARAM_JSLIB 				= "lib";		// Underlying library to be used, default is 'dojo'
	public static final String PARAM_JSVERSION			= "ver";		// Version of the library to use, default is '1.4'
	public static final String PARAM_ENVIRONMENT		= "env";		// app (default), portlet, gadget, iwidget
	public static final String PARAM_AGGREGATOR			= "aggregator";	// true (default)/false. Enables/Disables the aggregator
	public static final String PARAM_MODULE				= "modules";	// List of modules/layers to load, '*' means everything
	
	/**
	 * The context in which the library is being loaded into.
	 * This is an optional param on the request.  Currently the only valid value is
	 * <code>gadget</code>.
	 * @see GADGET_CONTEXT
	 */
	public static final String PARAM_CONTEXT            = "context";

	static final String sourceClass = LibraryRequest.class.getName();
	static final Logger logger = Logger.getLogger(sourceClass);

	/**
	 * 
	 * @param req
	 * @param resp
	 */
	public LibraryRequest(HttpServletRequest req, HttpServletResponse resp) {
		this.httpRequest = req;
		this.httpResponse = resp;
		this.context = Context.DEFAULT;
	}	
	
	/**
	 * Initialise the LibraryRequest
	 * 
	 * @param defaultEnvironment
	 * @param toolkitUrl
	 * @param toolkitJsUrl
	 * @param serviceUrl
	 * @param iframeUrl
	 * @throws ServletException
	 * @throws IOException
	 */
	public void init(SBTEnvironment defaultEnvironment, String toolkitUrl, String toolkitJsUrl, String serviceUrl, String iframeUrl) throws ServletException, IOException {
		this.toolkitUrl = StringUtil.replace(toolkitUrl, "%local_server%", UrlUtil.getServerUrl(httpRequest));
		this.toolkitJsUrl = StringUtil.replace(toolkitJsUrl, "%local_server%", UrlUtil.getServerUrl(httpRequest));
		this.serviceUrl = StringUtil.replace(serviceUrl, "%local_application%", UrlUtil.getContextUrl(httpRequest));
		this.iframeUrl = StringUtil.replace(iframeUrl, "%local_server%", UrlUtil.getServerUrl(httpRequest));
		
		readFromRequest(httpRequest, defaultEnvironment);
		
		inited = true;
		
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("Created library request: "+toString());
		}
	}
	
	/**
	 * @return the inited
	 */
	public boolean isInited() {
		return inited;
	}
	
	/**
	 * @return the httpRequest
	 */
	public HttpServletRequest getHttpRequest() {
		return httpRequest;
	}

	/**
	 * @return the httpResponse
	 */
	public HttpServletResponse getHttpResponse() {
		return httpResponse;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isProxyDomain() {
		return true;
	}
	
	/**
	 * 
	 * @return
	 */
	//public boolean useProxy() {
	//	return !StringUtil.isEmpty(serviceUrl);
	//}

	/**
	 * 
	 * @return
	 */
	public String getServiceUrl() {
		return serviceUrl;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean useIFrame() {
		return false;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getIFrameUrl() {
		return iframeUrl;
	}
	
	/**
	 * Indicates whether we should use the gadget transport.
	 * @return True if we should use the gadget transport false otherwise.
	 */
	//public boolean useGadgetTransport() {
	//    return this.context == Context.GADGET;
	//}

	/**
	 * @return the jsLib
	 */
	public String getJsLib() {
		return jsLib;
	}

	/**
	 * @return the jsVersion
	 */
	public String getJsVersion() {
		return jsVersion;
	}

	/**
	 * @return the toolkit URL
	 */
	public String getToolkitUrl() {
		return toolkitUrl;
	}

	/**
	 * @return the toolkit JavaScript URL
	 */
	public String getToolkitJsUrl() {
		return toolkitJsUrl;
	}

	/**
	 * @return the environment
	 */
	public SBTEnvironment getEnvironment() {
		return environment;
	}
	
	/**
	 * Return the specified header
	 * 
	 * @return
	 */
	public String getHeader(String name) {
		return httpRequest.getHeader(name);
	}
	
	/**
	 * Sets the context.
	 * @param context The context parameter from the request.
	 */
	private void setContext(String context) {
	    if(context == null || "".equalsIgnoreCase(context)) {
            this.context = Context.DEFAULT;
        } else if(context.equalsIgnoreCase(Context.GADGET.toString())) {
            this.context = Context.GADGET;
        }
	}
	
	/**
	 * 
	 * @param req
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void readFromRequest(HttpServletRequest req, SBTEnvironment defaultEnvironment) throws ServletException, IOException {
		jsLib = readString(req, PARAM_JSLIB, DEFAULT_JSLIB);
		jsVersion = readString(req, PARAM_JSVERSION, "dojo".equals(jsLib) ? DEFAULT_VERSION : "");
		setContext(readString(req, PARAM_CONTEXT, ""));
		String environmentName = req.getParameter(PARAM_ENVIRONMENT);
		if (!StringUtil.isEmpty(environmentName)) {
			SBTEnvironment parentEnvironment = SBTEnvironmentFactory.get(environmentName);
			if (parentEnvironment == null) {
				String message = MessageFormat.format("Unable to load environment: {0}", environmentName);
				throw new ServletException(message);
			}
			environment = new RequestEnvironment(parentEnvironment);
		}
		if (environment == null) {
			environment = new RequestEnvironment(defaultEnvironment);
		}
	}
	
	/**
	 * Read a parameter from the request and return the value if it's a valid value
	 * i.e. not null and not an empty string. Otherwise return the default value.
	 * 
	 * @param req
	 * @param paramName
	 * @param defValue
	 * 
	 * @return
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	private String readString(HttpServletRequest req, String paramName, String defaultValue) throws ServletException, IOException {
    	String val = req.getParameter(paramName);
    	if(val != null && val.length() > 0) {
    		return val;
    	}
    	return defaultValue;
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("jsLib=").append(jsLib);
		sb.append(";jsVersion=").append(jsVersion);
		sb.append(";environment=").append(environment);
		sb.append(";toolkitUrl=").append(toolkitUrl);
		sb.append(";toolkitJsUrl=").append(toolkitJsUrl);
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Class representing the SBTEnvironment for this request
	 * 
	 * @author mwallace
	 */
	class RequestEnvironment extends SBTEnvironment {
		
		private SBTEnvironment parent;
		
		/**
		 * 
		 * @param parent
		 */
		RequestEnvironment(SBTEnvironment parent) {
			this.parent = parent;
			
			String endpointsStr = httpRequest.getParameter(PARAM_ENDPOINTS);
			if (!StringUtil.isEmpty(endpointsStr)) {
				Endpoint[] requestEndpoints = parseEndpoints(endpointsStr);
				if (requestEndpoints != null && requestEndpoints.length > 0) {
					Endpoint[] defaultEndpoints = parent.getEndpointsArray();
					List<Endpoint> endpoints = new ArrayList<Endpoint>();
					for (int i=0;i<requestEndpoints.length; i++) {
						endpoints.add(requestEndpoints[i]);
					}
					for (int i=0;i<defaultEndpoints.length; i++) {
						endpoints.add(defaultEndpoints[i]);
					}
					setEndpointsArray(endpoints.toArray(new Endpoint[endpoints.size()]));
				}
			}
			
			String propertiesStr = httpRequest.getParameter(PARAM_CLIENT_PROPERTIES);
			if (!StringUtil.isEmpty(propertiesStr)) {
				try {
					Property[] requestProperties = parseProperties(propertiesStr);
					if (requestProperties != null && requestProperties.length > 0) {
						Property[] defaultProperties = parent.getPropertiesArray();
						List<Property> properties = new ArrayList<Property>();
						for (int i=0;i<requestProperties.length; i++) {
							properties.add(requestProperties[i]);
						}
						for (int i=0;i<defaultProperties.length; i++) {
							properties.add(defaultProperties[i]);
						}
						setPropertiesArray(properties.toArray(new Property[properties.size()]));
					}
				} catch(IOException ex) {
					Platform.getInstance().log(ex);
				}
			}
		}
		
		/* (non-Javadoc)
		 * @see com.ibm.sbt.jslibrary.SBTEnvironment#getName()
		 */
		@Override
		public String getName() {
			return parent.getName();
		}

		/* (non-Javadoc)
		 * @see com.ibm.sbt.jslibrary.SBTEnvironment#getEndpointsArray()
		 */
		@Override
		public Endpoint[] getEndpointsArray() {
			Endpoint[] endpoints = super.getEndpointsArray();
			if (endpoints != null) {
				return endpoints;
			}
			return parent.getEndpointsArray();
		}
		
		/* (non-Javadoc)
		 * @see com.ibm.sbt.jslibrary.SBTEnvironment#getPropertiesArray()
		 */
		@Override
		public Property[] getPropertiesArray() {
			Property[] properties = super.getPropertiesArray();
			if (properties != null) {
				return properties;
			}
			return parent.getPropertiesArray();
		}
		
		/* (non-Javadoc)
		 * @see com.ibm.sbt.jslibrary.SBTEnvironment#getPropertyByName(java.lang.String)
		 */
		@Override
		public Property getPropertyByName(String name) {
			Property property = super.getPropertyByName(name);
			if (property != null) {
				return property;
			}
			return parent.getPropertyByName(name);
		}
		
		/* (non-Javadoc)
		 * @see com.ibm.sbt.jslibrary.SBTEnvironment#getPropertyValueByName(java.lang.String)
		 */
		@Override
		public String getPropertyValueByName(String name) {
			String value = super.getPropertyValueByName(name);
			if (value != null) {
				return value;
			}
			return parent.getPropertyValueByName(name);
		}
		
	}
	
	/**
	 * Indicates what context the toolkit is being loaded into.
	 *
	 */
	public enum Context {
	    /**
	     * No particular special context, ie a normal web page.
	     */
	    DEFAULT("default"), 
	    
	    /**
	     * Indicates the toolkit is being loaded into a gadget.
	     */
	    GADGET("gadget");
	    
	    private final String name;
	    
	    /**
	     * Construct a context.
	     * @param name The name of the context
	     */
	    private Context(String name){
	        this.name = name;
	    }
	    
	    @Override
        public String toString() {
	        return name;
	    }
	}

}
