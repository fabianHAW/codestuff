package mware_lib;

public class NameServiceRequest {

	private String requestType;
	private String serviceName;
	private RemoteObjectRef objectRef;
	
	public NameServiceRequest() {
		// TODO Auto-generated constructor stub
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
