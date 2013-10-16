package lib;

import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;

public class MockService extends ClientService {
	
	private enum MockMode { RECORD, REPLAY, PASSTHRU }
	private MockMode mode;
	private ClientService service;
	private MockSerializer serializer = new MockSerializer();

    public MockService(ClientService svc) {
    	this(svc, "passthru");
    }

    public MockService(ClientService svc, String mockMode) {
    	this.service = svc;
    	this.endpoint = svc.getEndpoint();
    	this.mode = MockMode.valueOf(mockMode.toUpperCase());
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
