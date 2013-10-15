package lib;

import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.endpoints.Endpoint;

public class MockService extends ClientService {
	
	private enum MockMode { RECORD, REPLAY, PASSTHRU }
	//private MockMode mode = MockMode.PASSTHRU;
	private MockMode mode = MockMode.REPLAY;
	private ClientService service;
	private MockSerializer serializer = new MockSerializer();


    public MockService(ClientService svc) {
    	this.service = svc;
    }

    public MockService(Endpoint endpoint) {
        super(endpoint);
    }

    public MockService(String endpointName) {
        super(endpointName);
    }
    
    @Override
	public Response xhr(String method, Args args, Object content) throws ClientServicesException {
    	Response response = null;
    	switch(this.mode){
	    	case REPLAY:
	    		return serializer.replayResponse();
	    	case PASSTHRU:
	    		return service.xhr(method, args, content);
	    	case RECORD:
	    		try {
	    		response = service.xhr(method, args, content);
	    		} catch (ClientServicesException e) {
	    			serializer.recordResponse(e);
	    			throw e;
	    		}
	    		catch (RuntimeException e) {
	    			serializer.recordResponse(e);
	    			throw e;
	    		} 
	    		serializer.recordResponse(response);
	    		break;
    	}
    	return response;
	}
}
