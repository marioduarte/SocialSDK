package lib;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.StringWriter;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.conn.EofSensorInputStream;

import com.ibm.commons.util.io.base64.Base64InputStream;
import com.ibm.commons.util.io.base64.Base64OutputStream;
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
	    		try {
	    		response = service.xhr(method, args, content);
	    		} catch (ClientServicesException e) {
	    			recordResponse(e);
	    			throw e;
	    		}
	    		catch (RuntimeException e) {
	    			recordResponse(e);
	    			throw e;
	    		} 
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
	        out.write("<data type='record'>");
	        out.write(serialize(response.getData()));
	        out.write("</data>\n");
	        out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }
    
    private void recordResponse(Throwable response) {
    	String mockFilepath = createPath();
    	FileWriter fstream;
		try {
			File file = new File(mockFilepath);
			File parentFolder = new File(file.getParent());
			parentFolder.mkdirs(); 
			file.createNewFile();
			fstream = new FileWriter(file, true);
	        BufferedWriter out = new BufferedWriter(fstream);
	        out.write("<data type='throwable'>");
	        out.write(serialize(response));
	        out.write("</data>");
	        out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }

    String serialize(EofSensorInputStream is) throws IOException {    	

	
    	ByteArrayOutputStream w = new ByteArrayOutputStream();
    	
    	Base64OutputStream base64OutputStream = new Base64OutputStream(w);
		IOUtils.copy(is,base64OutputStream);
		base64OutputStream.flush();base64OutputStream.close();
    	System.out.println("CONVERTED OBJECT TO "+w.toString("UTF-8"));
    	return w.toString("UTF-8");
    }

    
    
    String serialize(Object o) throws IOException {    	
    	if (o instanceof EofSensorInputStream) return serialize((EofSensorInputStream) o);
    	ByteArrayOutputStream w = new ByteArrayOutputStream();
    	ObjectOutputStream os = new ObjectOutputStream(new Base64OutputStream(w));
    	os.writeObject(o);
    	os.flush();os.close();
    	System.out.println("CONVERTED OBJECT TO "+w.toString("UTF-8"));
    	return w.toString("UTF-8");
    }

    
    Object  deserialize(String o) throws IOException {
    	ObjectInputStream is = new ObjectInputStream(new Base64InputStream(new ByteArrayInputStream(o.getBytes())));
    	try {
			return is.readObject();
		} catch (ClassNotFoundException e) {
			throw new IOException(e);
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
		String path = new StringBuilder(basePath).append(File.separator).append("test").append(File.separator).append(className).append(File.separator).append(methodName).append(".mock").toString();

		return path;
	}

}
