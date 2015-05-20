package mware_lib;

import java.net.InetAddress;
import java.util.List;

public class MessageADT {

	private InetAddress iNetAdrress;
	private int messageID;
	private String methodName;
	private int messageType;
	private RemoteObjRef objectRef;
	private byte[] returnVal;
	private List<Byte[]> arguments;
	private List<Exception> exceptionList;
	
	public MessageADT() {
		// TODO Auto-generated constructor stub
	}

	public InetAddress getiNetAdrress() {
		return iNetAdrress;
	}

	public void setiNetAdrress(InetAddress iNetAdrress) {
		this.iNetAdrress = iNetAdrress;
	}

	public int getMessageID() {
		return messageID;
	}

	public void setMessageID(int messageID) {
		this.messageID = messageID;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public RemoteObjRef getObjectRef() {
		return objectRef;
	}

	public void setObjectRef(RemoteObjRef objectRef) {
		this.objectRef = objectRef;
	}

	public byte[] getReturnVal() {
		return returnVal;
	}

	public void setReturnVal(byte[] returnVal) {
		this.returnVal = returnVal;
	}

	public List<Byte[]> getArguments() {
		return arguments;
	}

	public void setArguments(List<Byte[]> arguments) {
		this.arguments = arguments;
	}

	public List<Exception> getExceptionList() {
		return exceptionList;
	}

	public void setExceptionList(List<Exception> exceptionList) {
		this.exceptionList = exceptionList;
	}

}
