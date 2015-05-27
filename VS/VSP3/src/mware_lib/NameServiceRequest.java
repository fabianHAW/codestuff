package mware_lib;

import java.io.Serializable;

/**
 * 
 * @author Francis
 * 
 *         Dieses Objekt wird an den entfernten Namensdienst geschickt, um
 *         entweder ein rebind oder ein resolve durchzuführen.
 */

public class NameServiceRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String requestType;
	private String serviceName;
	private RemoteObjectRef objectRef;
	private String host;
	private int port;

	public NameServiceRequest(String requestType, String serviceName,
			RemoteObjectRef objectRef, String host, int port) {
		this.requestType = requestType;
		this.serviceName = serviceName;
		this.objectRef = objectRef;
		this.host = host;
		this.port = port;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public RemoteObjectRef getObjectRef() {
		return objectRef;
	}

	public void setObjectRef(RemoteObjectRef objectRef) {
		this.objectRef = objectRef;
	}
	
	public void setHost(String h){
		this.host = h;
	}
	
	public String getHost(){
		return host;
	}
	
	public int getPort(){
		return port;
	}
	
	public void setPort(int p){
		this.port = p;
	}

	@Override
	public String toString() {
		return "NameServiceRequest [requestType=" + requestType
				+ ", serviceName=" + serviceName + ", objectRef=" + objectRef
				+ "]";
	}

}
