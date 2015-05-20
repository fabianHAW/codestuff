package mware_lib;

import java.net.InetAddress;
import java.util.List;

public class RemoteObjRef {

	private InetAddress inetAddress;
	private int port;
	private long time;
	private int objectNumber;
	private List<String> interfaces;
	
	public RemoteObjRef() {
		// TODO Auto-generated constructor stub
		
	
	}

	public InetAddress getInetAddress() {
		return inetAddress;
	}

	public void setInetAddress(InetAddress inetAddress) {
		this.inetAddress = inetAddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getObjectNumber() {
		return objectNumber;
	}

	public void setObjectNumber(int objectNumber) {
		this.objectNumber = objectNumber;
	}

	public List<String> getInterfaces() {
		return interfaces;
	}

	public void setInterfaces(List<String> interfaces) {
		this.interfaces = interfaces;
	}

}
