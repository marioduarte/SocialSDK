package lib;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.endpoints.Endpoint;

public class MockService extends ClientService {
	
	private enum MockMode { RECORD, REPLAY, PASSTHRU }
	//private MockMode mode = MockMode.PASSTHRU;
	private MockMode mode = MockMode.RECORD;
	private ClientService service;

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
	    		response = replayResponse();
	    	case PASSTHRU:
	    		response = service.xhr(method, args, content);
	    	case RECORD:
	    		response = service.xhr(method, args, content);
	    		recordResponse(response);
	    		break;
    	}
    	return response;
	}
    
    private void recordResponse(Response response) {
    	String mockFilepath = createPath();
    	FileWriter fstream;
		try {
			File file = new File(mockFilepath);
			File parentFolder = new File(file.getParent());
			parentFolder.mkdirs(); 
			file.createNewFile();
			fstream = new FileWriter(file, true);
	        BufferedWriter out = new BufferedWriter(fstream);
	        out.write("<data>");
	        out.write(response.getData().toString());
	        out.write("</data>");
	        out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }

    private Response replayResponse() {
    	Response response = null;
    	String mockFilepath = createPath();
    	return response;
    }

	private StackTraceElement getStackTraceElement() {
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		for (StackTraceElement trace : stackTraceElements) {
			if (trace.getClassName().endsWith("Test")) {
				return trace;
			}
		}
		return null;
	}
	
	private String createPath() {
		StackTraceElement trace = getStackTraceElement();
		String basePath = System.getProperty("user.dir");
		String className = trace.getClassName().replace(".", File.separator);
		String methodName = trace.getMethodName();
		return new StringBuilder(basePath).append(File.separator).append("test").append(File.separator).append(className).append(File.separator).append(methodName).append(".mock").toString();
	}

}
