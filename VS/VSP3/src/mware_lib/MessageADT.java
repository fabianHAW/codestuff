package mware_lib;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.List;

import shared_types.RemoteObjectRef;


/**
 * 
 * @author Francis
 * 
 *         Dient als Kommunikationsobjekt zwischen den Komponenten. Für einen
 *         Reqeust erzeugt der Proxy, auf Client-Seite, aus den gegebenen Daten
 *         ein solches Objekt (Marshals Request). Das Skeleton, auf
 *         Server-Seite, packt dieses Objekt aus (Unmarshals Request) und erhält
 *         somit die nötigen Informationen. Für einen Reply erzeugt das
 *         Skeleton, auf Server-Seite, ein neues MessageADT (Marshals Reply).
 *         Auf der Client-Seite muss der Proxy dieses Objekt wieder entpacken
 *         (Unmarshals Reply).
 */
public class MessageADT implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private InetAddress iNetAdrress;
	private int port;
	private int messageID;
	private String methodName;
	private int messageType;
	private RemoteObjectRef objectRef;
	private byte[] returnVal;
	private List<byte[]> arguments;
	private List<Exception> exceptionList;

	/**
	 * Copy-Konstruktor
	 * 
	 * @param iNetAddress
	 *            Internetadresse des Senders
	 * @param messageID
	 *            eindeutige Nachrichtennummer
	 * @param methodName
	 *            Methodenname der aufgerufen werden soll
	 * @param messageType
	 *            Nachrichtentyp (0 := Request, 1 := Reply)
	 * @param objectRef
	 *            entfernte Objekt-Referenz des aufzurufenden Objektes
	 * @param returnVal
	 *            return-Wert der entfernten Methode
	 * @param arguments
	 *            Argumente der aufzurufenden Methode
	 * @param exceptionList
	 *            eine Liste von Exceptions die ggf. erzeugt werden
	 */
	public MessageADT(InetAddress iNetAddress, int port, Integer messageID,
			String methodName, Integer messageType, RemoteObjectRef objectRef,
			byte[] returnVal, List<byte[]> arguments,
			List<Exception> exceptionList) {
		this.iNetAdrress = iNetAddress;
		this.port = port;
		this.messageID = messageID;
		this.methodName = methodName;
		this.messageType = messageType;
		this.objectRef = objectRef;
		this.returnVal = returnVal;
		this.arguments = arguments;
		this.exceptionList = exceptionList;
	}

	public InetAddress getiNetAdrress() {
		return iNetAdrress;
	}

	public void setiNetAdrress(InetAddress iNetAdrress) {
		this.iNetAdrress = iNetAdrress;
	}
	
	public int getPort() {
		return port;
	}

	public void setport(int port) {
		this.port = port;
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

	public RemoteObjectRef getObjectRef() {
		return objectRef;
	}

	public void setObjectRef(RemoteObjectRef objectRef) {
		this.objectRef = objectRef;
	}

	public byte[] getReturnVal() {
		return returnVal;
	}

	public void setReturnVal(byte[] returnVal) {
		this.returnVal = returnVal;
	}

	public List<byte[]> getArguments() {
		return arguments;
	}

	public void setArguments(List<byte[]> arguments) {
		this.arguments = arguments;
	}

	public List<Exception> getExceptionList() {
		return exceptionList;
	}

	public void setExceptionList(List<Exception> exceptionList) {
		this.exceptionList = exceptionList;
	}

}
