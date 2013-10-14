package lib;

import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.endpoints.BasicEndpoint;
import com.ibm.sbt.services.endpoints.Endpoint;

public class MockEndpoint extends BasicEndpoint {
	private Endpoint endpoint;
	
	public MockEndpoint(Endpoint endpoint) {
		this.endpoint = endpoint;
	}
	
    @Override
	public ClientService getClientService() throws ClientServicesException {
    	return new MockService(endpoint.getClientService());
    }

}
