package mware_lib;

import java.io.Serializable;

public class NameServiceRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String requestType;
	private String serviceName;
	private RemoteObjectRef objectRef;

	public NameServiceRequest(String requestType, String serviceName,
			RemoteObjectRef objectRef) {
		this.requestType = requestType;
		this.serviceName = serviceName;
		this.objectRef = objectRef;
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

	@Override
	public String toString() {
		return "NameServiceRequest [requestType=" + requestType
				+ ", serviceName=" + serviceName + ", objectRef=" + objectRef
				+ "]";
	}
	
	

}
