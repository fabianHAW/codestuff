package mware_lib;

import java.io.Serializable;
import java.net.InetAddress;
/**
 * Verweise zum Entwurf:
 * <Entwurfsdokument> : Nicht im Entwurfsdokument angegeben.
 * <Klassendiagramm> : Implementierung durch Methoden zur in mware_lib deklarierten Klasse - RemoteObjectRef
 * <Sequenzdiagramm vsp3_sequ_server:_start> : Realiserung der Sequenznummer 3.2.1.1.1
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

	/**
	 * Verweis zum Entwurf
	 *  <Sequenzdiagramm vsp3_sequ_server:_start> : Realiserung der Sequenznummer 3.2.1.1.1
	 *  
	 * @param inetAddress
	 * @param port
	 * @param time
	 * @param objectNumber
	 */
	public RemoteObjectRef(InetAddress inetAddress, int port, long time,
			int objectNumber) {
		this.inetAddress = inetAddress;
		this.port = port;
		this.time = time;
		this.objectNumber = objectNumber;
	}

	/**
	 * Wird verwendet um zu ermittelten, auf welchem Server das Objekt liegt.
	 * @return inetAddress die Adresse auf der das Objekt erreicht werden kann, für dass diese Objekterferenz
	 * steht.
	 */
	public InetAddress getInetAddress() {
		return inetAddress;
	}

	/**
	 * Einfacher Setter.
	 * @param inetAddress Die Adresse auf der das Objekt erreicht werden kann.
	 */
	public void setInetAddress(InetAddress inetAddress) {
		this.inetAddress = inetAddress;
	}

	/**
	 * Einfacher Getter
	 * @return port liefert den Port auf dem das Objekt erreicht werden kann.
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Einfacher Setter
	 * @param port Der Port auf dem das Objekt an der InetAddress erreicht werden kann.
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * Einfacher Getter
	 * @return time die Aktuelle Zeit in long.
	 */
	public long getTime() {
		return time;
	}

	/**
	 * Einfacher Setter
	 * @param time Die aktuelle Zeit.
	 */
	public void setTime(long time) {
		this.time = time;
	}

	/**
	 * Einfacher Getter - wird verwedet, um zu ermittelten welcher Klasse diese 
	 * Objektreferenz zugehörig ist.
	 * @return objectNumber Die Objektnummer.
	 */
	public int getObjectNumber() {
		return objectNumber;
	}

	/**
	 * Einfacher Setter
	 * 
	 */
	public void setObjectNumber(int objectNumber) {
		this.objectNumber = objectNumber;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((inetAddress == null) ? 0 : inetAddress.hashCode());
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
		if (objectNumber != other.objectNumber)
			return false;
		if (port != other.port)
			return false;
		if (time != other.time)
			return false;
		return true;
	}



}
