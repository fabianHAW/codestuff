package mware_lib;

public class NameServiceRequest {

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

}
