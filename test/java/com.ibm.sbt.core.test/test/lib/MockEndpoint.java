package lib;

import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.endpoints.BasicEndpoint;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;

public class MockEndpoint extends BasicEndpoint {
	private Endpoint endpoint;
	private String innerEndpoint;
	private String mockMode;

	public MockEndpoint() {
		super();
	}

	public MockEndpoint(Endpoint endpoint) {
		this.endpoint = endpoint;
	}
	
    @Override
	public ClientService getClientService() throws ClientServicesException {
    	MockService mockService;
    	if (endpoint!=null) {
    		mockService = new MockService(endpoint.getClientService());
    	} else {
    		mockService = new MockService(EndpointFactory.getEndpoint(innerEndpoint).getClientService(), mockMode);
    	}
    	return mockService;
    }

	public String getInnerEndpoint() {
		return innerEndpoint;
	}

	public void setInnerEndpoint(String innerEndpoint) {
		this.innerEndpoint = innerEndpoint;
	}

	public String getMockMode() {
		return mockMode;
	}

	public void setMockMode(String mockMode) {
		this.mockMode = mockMode;
	}

}
