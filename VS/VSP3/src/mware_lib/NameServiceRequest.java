package mware_lib;

public class NameServiceRequest {

	private String requestType;
	private String serviceName;
	private RemoteObjRef objectRef;
	
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

	public RemoteObjRef getObjectRef() {
		return objectRef;
	}

	public void setObjectRef(RemoteObjRef objectRef) {
		this.objectRef = objectRef;
	}

}
