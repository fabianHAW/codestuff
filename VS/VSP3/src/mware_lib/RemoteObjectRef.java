package mware_lib;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.List;

/**
 * 
 * @author Francis u. Fabian
 *
 * Entfernte Objekt-Referenz als ADT
 */

public class RemoteObjectRef implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private InetAddress inetAddress;
	private int port;
	private long time;
	private int objectNumber;
	private List<String> interfaces;

	public RemoteObjectRef(InetAddress inetAddress, int port, long time,
			int objectNumber, List<String> interfaces) {
		this.inetAddress = inetAddress;
		this.port = port;
		this.time = time;
		this.objectNumber = objectNumber;
		this.interfaces = interfaces;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((inetAddress == null) ? 0 : inetAddress.hashCode());
		result = prime * result
				+ ((interfaces == null) ? 0 : interfaces.hashCode());
		result = prime * result + objectNumber;
		result = prime * result + port;
		result = prime * result + (int) (time ^ (time >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RemoteObjectRef other = (RemoteObjectRef) obj;
		if (inetAddress == null) {
			if (other.inetAddress != null)
				return false;
		} else if (!inetAddress.equals(other.inetAddress))
			return false;
		if (interfaces == null) {
			if (other.interfaces != null)
				return false;
		} else if (!interfaces.equals(other.interfaces))
			return false;
		if (objectNumber != other.objectNumber)
			return false;
		if (port != other.port)
			return false;
		if (time != other.time)
			return false;
		return true;
	}
	
	

}
