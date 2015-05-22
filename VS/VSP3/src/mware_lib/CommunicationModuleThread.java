package mware_lib;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class CommunicationModuleThread extends Thread {

	private MessageADT sendMessage;
	private MessageADT receivedMessage;
	private Socket socket;
	private ObjectOutputStream output;

	public CommunicationModuleThread(MessageADT m) {
		this.sendMessage = m;
		try {
			this.socket = new Socket(m.getObjectRef().getInetAddress(),
					CommunicationModule.getCommunicationmoduleport());
			this.output = new ObjectOutputStream(this.socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		synchronized (this) {
			try {
				this.output.writeObject(sendMessage);
				this.wait();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				notify();
			}
		}

	}

	public MessageADT getReceivedMessage() {
		return receivedMessage;
	}

	public void setReceivedMessage(MessageADT m) {
		this.receivedMessage = m;
	}

	public MessageADT getSendMessage() {
		return sendMessage;
	}
}
