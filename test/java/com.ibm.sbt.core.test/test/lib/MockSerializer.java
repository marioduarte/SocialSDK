/*
 * � Copyright IBM Corp. 2013
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.conn.EofSensorInputStream;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.junit.Test;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.ibm.commons.runtime.Context;
import com.ibm.commons.util.io.base64.Base64OutputStream;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.jslibrary.SBTEnvironment;
import com.ibm.sbt.services.client.ClientServicesException;

/**
 * 
 * @author Lorenzo Boccaccia
 * @author Carlos Manias
 * 
 */
public class MockSerializer {

	// used to know when to append and when to reset the mock file; //TODO will
	// not work if we test with multiple endpoints in the same test run
	private static final HashSet<String> seen = new HashSet<String>();
	private static final HashMap<String, Iterator<Node>> replyStream = new HashMap<String, Iterator<Node>>();

	public void writeData(String data) throws IOException {
		File file = getFile(true);
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		// Seek to end of file
		raf.seek((file.length() - "\n</responses>".length()));

		raf.write(data.getBytes("UTF-8"));
		raf.write("\n</responses>".getBytes("UTF-8"));

	}

	private Iterator<Node> getReader() throws IOException {
		String path = getPath();
		if (replyStream.containsKey(path)) {
			return replyStream.get(path);
		}
		Document doc;
		try {
			doc = DOMUtil.createDocument(new FileInputStream(new File(path)),
					false);

			Iterator<Node> nodeIt = DOMUtil.evaluateXPath(doc, "//response")
					.getNodeIterator();
			replyStream.put(path, nodeIt);
			return nodeIt;

		} catch (XMLException e) {
			throw new IOException(e);
		}

	}

	public HttpResponse recordResponse(HttpResponse response) {
		try {

			StringWriter out = new StringWriter();
			out.write("\n<response ");

			out.write("statusCode=\"");
			int statusCode = response.getStatusLine().getStatusCode();
			out.write(String.valueOf(statusCode).trim());
			out.write("\" ");
			out.write("statusReason=\"");
			String reasonPhrase = response.getStatusLine().getReasonPhrase();
			out.write(String.valueOf(reasonPhrase).trim());
			out.write("\">\n");
			out.write("<headers>");
			Header[] allHeaders = response.getAllHeaders();
			out.write(serialize(allHeaders));
			out.write("</headers>\n<data><![CDATA[");
			String serializedEntity = serialize(response.getEntity()
					.getContent());

			out.write(serializedEntity);
			out.write("]]></data>\n</response>");
			out.flush();
			out.close();
			writeData(out.toString());

			return buildResponse(allHeaders, statusCode, reasonPhrase,
					serializedEntity);

		} catch (IOException e) {
			throw new UnsupportedOperationException(e);
		}
	}

	private HttpResponse buildResponse(Header[] allHeaders, int statusCode,
			String reasonPhrase, String serializedEntity) {
		BasicHttpResponse r = new BasicHttpResponse(new ProtocolVersion("HTTP",
				1, 0), statusCode, reasonPhrase);
		r.setHeaders(allHeaders);
		BasicHttpEntity e = new BasicHttpEntity();
		// TODO: use content-encoding header
		try {
			e.setContent(new ByteArrayInputStream(serializedEntity
					.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e1) {
			throw new UnsupportedOperationException(e1);
		}
		for (Header h : allHeaders) {
			if (h.getName().equals("Content-Type")) {
				e.setContentType(h.getValue());
			}
		}
		e.setContentLength(serializedEntity.length());
		r.setEntity(e);
		return r;
	}

	String serialize(InputStream is) throws IOException {
		ByteArrayOutputStream w = new ByteArrayOutputStream();
		IOUtils.copy(is, w);
		return w.toString("UTF-8");
	}

	String serialize(Node o) throws IOException {
		ByteArrayOutputStream w = new ByteArrayOutputStream();
		try {
			DOMUtil.serialize(w, o, null);
		} catch (XMLException e) {
			throw new IOException(e);
		}
		System.out.println("CONVERTED NODE TO " + w.toString("UTF-8"));
		return w.toString("UTF-8");

	}

	String serialize(Header[] o) {
		StringWriter w = new StringWriter();
		for (Header h : o) {
			w.write("\n    <header>\n        <name><![CDATA[");
			w.write(h.getName());
			w.write("]]></name>\n        <value><![CDATA[");
			w.write(h.getValue());
			w.write("]]></value>\n    </header>");

		}

		return w.toString();
	}

	String serialize(Object o) throws IOException {
		if (o instanceof EofSensorInputStream)
			return serialize((EofSensorInputStream) o);
		if (o instanceof Node)
			return serialize((Node) o);
		if (o instanceof Header[])
			return serialize((Header[]) o);
		ByteArrayOutputStream w = new ByteArrayOutputStream();
		Base64OutputStream base64OutputStream = new Base64OutputStream(w);
		ObjectOutputStream os = new ObjectOutputStream(base64OutputStream);
		os.writeObject(o);
		os.flush();
		os.close();
		base64OutputStream.flush();
		base64OutputStream.close();
		System.out.println("CONVERTED OBJECT TO " + w.toString("UTF-8"));
		return w.toString("UTF-8");
	}

	Object deserialize(String o) throws IOException {
		ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(
				o.getBytes()));
		try {
			return is.readObject();
		} catch (ClassNotFoundException e) {
			throw new IOException(e);
		} finally {
			is.close();
		}
	}

	public HttpResponse replayResponse() throws ClientServicesException {
		try {
			Node r = getReader().next();

			NamedNodeMap nnm = r.getAttributes();

			String code = nnm.getNamedItem("statusCode").getTextContent();
			String reason = nnm.getNamedItem("statusReason").getTextContent();

			Node headers = (Node) DOMUtil.evaluateXPath(r, "./headers")
					.getSingleNode();
			Node data = (Node) DOMUtil.evaluateXPath(r, "./data")
					.getSingleNode();
			String entity = null;
			if (data.getFirstChild() == null)
				entity = "";
			else
				entity = ((CharacterData) data.getFirstChild()).getData();

			Iterator<Node> hIt = (Iterator<Node>) DOMUtil.evaluateXPath(
					headers, "./header").getNodeIterator();
			ArrayList<Header> allHeaders = new ArrayList<Header>();

			while (hIt.hasNext()) {
				Node headerNode = hIt.next();
				String name = ((Node) DOMUtil.evaluateXPath(headerNode,
						"./name").getSingleNode()).getTextContent();
				String value = ((Node) DOMUtil.evaluateXPath(headerNode,
						"./value").getSingleNode()).getTextContent();
				allHeaders.add(new BasicHeader(name, value));
			}

			return buildResponse(
					allHeaders.toArray(new Header[allHeaders.size()]),
					Integer.valueOf(code), reason, entity);

		} catch (Exception e) {
			throw new UnsupportedOperationException(e);
		}
	}

	private void deserializeThrowable(Node r) {
		// TODO Auto-generated method stub

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
		if (!file.exists()) {
			file.createNewFile();
			FileOutputStream st = new FileOutputStream(file);
			st.write("<?xml version=\"1.0\"?>\n<responses>\n</responses>"
					.getBytes("UTF-8"));
			st.flush();
			st.close();
		}
		return file;
	}

	private String getEndpointName() {
		String endpointName = "";
		Context context = Context.getUnchecked();
		if (context == null) {
			return null;
		}
		String environment = context.getProperty("environment");
		if (environment != null) {
			SBTEnvironment env = (SBTEnvironment) context.getBean(environment);
			SBTEnvironment.Endpoint[] endpointsArray = env.getEndpointsArray();
			for (SBTEnvironment.Endpoint endpoint : endpointsArray) {
				endpointName = endpoint.getAlias();
			}
		}
		return endpointName;
	}

	private String getPath() {
		StackTraceElement trace = getStackTraceElement();
		String basePath = System.getProperty("user.dir");
		String fullClassName = trace.getClassName()
				.replace(".", File.separator);
		String className = fullClassName.substring(fullClassName
				.lastIndexOf(File.separatorChar));
		String packageName = fullClassName.substring(0,
				fullClassName.lastIndexOf(File.separatorChar));
		String methodName = trace.getMethodName();
		String endpointName = getEndpointName();
		String path = new StringBuilder(basePath).append(File.separator)
				.append("test").append(File.separator).append(packageName)
				.append(File.separator).append("mockData")
				.append(File.separator).append(endpointName)
				.append(File.separator).append(className).append("_")
				.append(methodName).append(".mock").toString();
		return path;
	}
}
