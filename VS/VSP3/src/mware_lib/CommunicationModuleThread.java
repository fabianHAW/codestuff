package mware_lib;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * @author Francis und Fabian
 * 
 *         Dieser Thread wird vom Proxy (Client-Seite) erzeugt. Der Proxy teilt
 *         ihm mit welche Nachricht er �ber das Netzwerk auf die Server-Seite
 *         versenden muss. Danach wartet der jeweilige Thread auf die Antwort.
 *         Trifft diese beim Kommunikationsmodul ein, so wird der Thread geweckt
 *         und kann dem Proxy das Ergebnis (MessageADT) zur�ck liefern.
 */
public class CommunicationModuleThread extends Thread {

	private MessageADT sendMessage;
	private MessageADT receivedMessage;
	private ServerSocket serversocket;
	private Socket socket;
	private ObjectOutputStream output;
	private ObjectInputStream input;

	public CommunicationModuleThread(MessageADT m) {
		CommunicationModule.debugPrint(this.getClass(), "initialized");
		this.sendMessage = m;
		try {

			this.serversocket = new ServerSocket(0);
			this.sendMessage.setport(this.serversocket.getLocalPort());
			this.socket = new Socket(m.getObjectRef().getInetAddress(), m
					.getObjectRef().getPort());

			this.output = new ObjectOutputStream(this.socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		// synchronized (this) {
		try {
			CommunicationModule.debugPrint(this.getClass(),
					"send message to remote server");

			this.output.writeObject(sendMessage);

			CommunicationModule.debugPrint(this.getClass(),
					"waiting for reply of own communication module");
			// this.wait();
			this.socket = this.serversocket.accept();
			this.input = new ObjectInputStream(this.socket.getInputStream());
			this.setReceivedMessage((MessageADT) this.input.readObject());

			CommunicationModule.debugPrint(this.getClass(), "REPLY received");	
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		
//			} catch (InterruptedException e) {
//				CommunicationModule.debugPrint(this.getClass(),
//						"someone interrupted me");
//				notify();
//			}
		CommunicationModule.removeThreadFromList(this);
	}

	public MessageADT getReceivedMessage() {
		return receivedMessage;
	}

	/**
	 * Setzt die empfangene Nachricht die an den Proxy weitergeleitet wird
	 * 
	 * @param m
	 *            empfangene MessageADT
	 */
	public void setReceivedMessage(MessageADT m) {
		this.receivedMessage = m;
	}

	public MessageADT getSendMessage() {
		return sendMessage;
	}
}
