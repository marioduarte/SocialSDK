package com.ibm.sbt.services.client.connections.wikis;

import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.connections.wikis.base.options.WikiOptions;
import com.ibm.sbt.services.client.connections.wikis.base.options.WikiOptions.SORT_BY;
import com.ibm.sbt.services.client.connections.wikis.exceptions.ResourceNotFoundException;
import com.ibm.sbt.services.client.connections.wikis.exceptions.RestServiceException;
import com.ibm.sbt.services.client.connections.wikis.exceptions.UnauthorizedException;
import com.ibm.sbt.services.endpoints.BasicEndpoint;
import com.ibm.sbt.services.endpoints.ConnectionsBasicEndpoint;

public class WikisServiceApp {

	private WikiService wikiService;
	
	 public WikisServiceApp(String url, String user, String password) {
	        this.wikiService = new WikiService();
	        this.wikiService.setEndpoint(createEndpoint(url, user, password));
	    }
	
	private BasicEndpoint createEndpoint(String url, String user, String password) {
    	BasicEndpoint endpoint = new ConnectionsBasicEndpoint();
    	endpoint.setUrl(url);
    	endpoint.setUser(user);
    	endpoint.setPassword(password);
    	endpoint.setForceTrustSSLCertificate(true);
    	return endpoint;
    }
	
	private WikiService getService() {
		return wikiService;
	}
	
	public static void main(String[] args) throws RestServiceException {
		if (args.length < 3) {
			System.out.println("Usage: ServiceApp url user password");
			return;
		}
		String url = args[0];
		String user = args[1];
		String password = args[2];
		
		WikisServiceApp app = new WikisServiceApp(url, user, password);
		
		try {
			WikiOptions options = new WikiOptions().includeTags(true).sortBy(SORT_BY.DESC);
			EntityList<Wiki> wikis = app.getService().getPublicWikis(options);
			
			for(Wiki wiki : wikis) {
				System.out.println(wiki.getTitle());
			}
		}
		catch(UnauthorizedException e) {
			// TODO do something
		}
	}
}
