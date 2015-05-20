package mware_lib;

public class NameServiceRequest {

	private String requestType;
	private String serviceName;
	private Object objectRef;

	public NameServiceRequest(String requestType, String serviceName,
			Object objectRef) {
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

	public Object getObjectRef() {
		return objectRef;
	}

	public void setObjectRef(Object objectRef) {
		this.objectRef = objectRef;
	}

}
