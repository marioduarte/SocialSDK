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

package lib;

import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.conn.EofSensorInputStream;
import org.junit.Test;
import org.w3c.dom.Node;

import com.ibm.commons.runtime.Context;
import com.ibm.commons.util.io.base64.Base64InputStream;
import com.ibm.commons.util.io.base64.Base64OutputStream;
import com.ibm.sbt.jslibrary.SBTEnvironment;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;

/**
 * 
 * @author Lorenzo Boccaccia
 * @author Carlos Manias
 *
 */
public class MockSerializer {

	//used to know when to append and when to reset the mock file; //TODO will not work if we test with multiple endpoints in the same test run
	private static final HashSet<String> seen = new HashSet<String>();
	private static final HashMap<String, BufferedReader> replyStream = new HashMap<String, BufferedReader>();

	public BufferedWriter getWriter() throws IOException{
		File file = getFile(true);
		FileWriter fstream;

		fstream = new FileWriter(file, true);
		BufferedWriter out = new BufferedWriter(fstream);
		return out;
	}

	private BufferedReader getReader() throws IOException {
		String path = getPath();
		if (replyStream.containsKey(path)){
			return replyStream.get(path);
		}
		BufferedReader ret = new BufferedReader(new FileReader(new File(path)));
		replyStream.put(path,ret);
		return ret;
	}

	public void recordResponse(Response response) {
		try {
			BufferedWriter out = getWriter();
			if (Node.class.isAssignableFrom(response.getData().getClass()))
				out.write("xml:");
			else if (EofSensorInputStream.class.isAssignableFrom(response.getData().getClass()))
				out.write("eofstream:");
			else	
			out.write("json:");
				
			out.write(String.valueOf( response.getResponse().getStatusLine().getStatusCode() ).trim());
			out.write(":");
			out.write(serialize(response.getResponseHeaders()));
			out.write(":");
			out.write(serialize(response.getData()));
			out.write(":\n");
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void recordResponse(Throwable response) {
		try {
			BufferedWriter out = getWriter();
	        out.write("throwable:");
	        out.write(serialize(response));
	        out.write(":\n");
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
    	Base64OutputStream base64OutputStream = new Base64OutputStream(w);
		ObjectOutputStream os = new ObjectOutputStream(base64OutputStream);
    	os.writeObject(o);
    	os.flush();os.close();
    	base64OutputStream.flush();base64OutputStream.close();
    	System.out.println("CONVERTED OBJECT TO "+w.toString("UTF-8"));
    	return w.toString("UTF-8");
    }
    
    Object deserialize(String o) throws IOException {
    	ObjectInputStream is = new ObjectInputStream(new Base64InputStream(new ByteArrayInputStream(o.getBytes())));
    	try {
			return is.readObject();
		} catch (ClassNotFoundException e) {
			throw new IOException(e);
		} finally {
			is.close();
		}
    }

	public Response replayResponse() throws ClientServicesException {
		try {
			BufferedReader r = getReader();
			String line = r.readLine();
			String[] parts = line.split(":");
			if (parts[0].equals("throwable")) {
				Object o = deserialize(parts[1]);
				if (o instanceof ClientServicesException) {
					throw (ClientServicesException) o;
				}
				if (o instanceof RuntimeException) {
					throw (RuntimeException) o;
				}
				throw new ClientServicesException((Throwable) o);
			}
			
			if (parts[0].equals("eofstream")) {		
				EofSensorInputStream st = null;
				if (parts.length==4)
				st = new EofSensorInputStream(
						new Base64InputStream(new ByteArrayInputStream(parts[3].getBytes())),null);
				else
					st = new EofSensorInputStream(new ByteArrayInputStream("".getBytes()),null);
				Response ret = new Response(st);
				ret.setHeaders((Header[])deserialize(parts[2]));
				
				//TODO: set status code
				return ret;
			}

			Response ret = new Response(deserialize(parts[3]));
			ret.setHeaders((Header[])deserialize(parts[2]));
			//TODO: set status code
			return ret;
			
		} catch (IOException e) {
			throw new ClientServicesException(e);
		}
	}

	private StackTraceElement getStackTraceElement() {
		StackTraceElement last = null;
		StackTraceElement[] stackTraceElements = Thread.currentThread()
				.getStackTrace();
		for (StackTraceElement trace : stackTraceElements) {
			try {
				if (Class.forName(trace.getClassName())
					.getMethod(trace.getMethodName())
					.isAnnotationPresent(Test.class))
				last = trace;
			} catch (Exception e) {
			}
		}
		return last;
	}

	private File getFile(boolean write) throws IOException {
		String path = getPath();
		boolean reset = !seen.contains(path);
		seen.add(path);
		
		File file = new File(path);
		File parentFolder = new File(file.getParent());
		parentFolder.mkdirs();
		if (write && reset && file.exists())
			file.delete();
		if (!file.exists())
			file.createNewFile();
		return file;
	}

	
	private String getEndpointName(){
		String endpointName="";
    	Context context = Context.getUnchecked();
    	if (context == null) {
    		return null;
    	}
        String environment = context.getProperty("environment");
        if(environment != null) {
            SBTEnvironment env = (SBTEnvironment) context.getBean(environment);
            SBTEnvironment.Endpoint[] endpointsArray = env.getEndpointsArray();
            for(SBTEnvironment.Endpoint endpoint : endpointsArray){
                endpointName = endpoint.getAlias();
            }
        }
        return endpointName;
    }
	
	private String getPath() {
		StackTraceElement trace = getStackTraceElement();
		String basePath = System.getProperty("user.dir");
		String fullClassName = trace.getClassName().replace(".", File.separator);
		String className = fullClassName.substring(fullClassName.lastIndexOf(File.separatorChar));
		String packageName = fullClassName.substring(0, fullClassName.lastIndexOf(File.separatorChar));
		String methodName = trace.getMethodName();
    	String endpointName = getEndpointName();
		String path = new StringBuilder(basePath).append(File.separator).
				append("test").append(File.separator).append(packageName).append(File.separator).
				append("mockData").append(File.separator).
				append(endpointName).append(File.separator).
				append(className).append("_").append(methodName).append(".mock").
				toString();
		return path;
	}
}
