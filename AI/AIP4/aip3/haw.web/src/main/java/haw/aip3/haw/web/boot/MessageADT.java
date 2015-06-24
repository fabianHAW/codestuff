package haw.aip3.haw.web.boot;

import java.io.Serializable;
import java.net.Socket;

public class MessageADT implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Socket socket;
	private boolean isAlive;

	public MessageADT() {
		// TODO Auto-generated constructor stub
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket s) {
		socket = s;
	}

	public boolean isAlive() {
		return isAlive;
	}
	
	public void setIsAlive(boolean b){
		this.isAlive = b;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isAlive ? 1231 : 1237);
		result = prime * result + ((socket == null) ? 0 : socket.hashCode());
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
		MessageADT other = (MessageADT) obj;
		if (isAlive != other.isAlive)
			return false;
		if (socket == null) {
			if (other.socket != null)
				return false;
		} else if (!socket.equals(other.socket))
			return false;
		return true;
	}

}
