package mware_lib;

import java.net.InetAddress;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Francis und Fabian
 * 
 *         Stellt Request/Reply-Protokoll bereit und hat somit auch einen Socket
 *         bzw. Serversocket
 */

public class CommunicationModule extends Thread {

	private ServerSocket serverSocket;
	private ObjectInputStream input;
	private static InetAddress localHost;
	// private static final int COMMUNICATIONMODULEPORT = 50001;
	// TODO zum lokalen Testen wird port in applikation gesetzt
	private static int COMMUNICATIONMODULEPORT;
	private static final int REQUEST = 0;
	private static final int REPLY = 1;
	private boolean isAlive;
	private static int messageIDCounter;

	private static List<Thread> communicationThreadList;
	private RequestDemultiplexer demultiplexer;

	public CommunicationModule() {
		this.demultiplexer = new RequestDemultiplexer();
		this.isAlive = true;
		messageIDCounter = 0;

		try {
			this.serverSocket = new ServerSocket(COMMUNICATIONMODULEPORT);
			communicationThreadList = new ArrayList<Thread>();
			localHost = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		Socket socket;

		while (this.isAlive) {
			try {
				CommunicationModule.debugPrint(this.getClass(),
						"waiting for something on port: "
								+ getCommunicationmoduleport());

				socket = this.serverSocket.accept();
				this.input = new ObjectInputStream(socket.getInputStream());
				MessageADT m = (MessageADT) this.input.readObject();

				CommunicationModule.debugPrint(this.getClass(),
						"received new MessageADT");

				// Request
				if (m.getMessageType() == REQUEST) {
					CommunicationModule.debugPrint(this.getClass(),
							"REQUEST received");
					requestToServant(m);
				}
				// Reply
				else if (m.getMessageType() == REPLY) {
					CommunicationModule.debugPrint(this.getClass(),
							"REPLY received");
					replyToProxy(m);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		for (Thread item : communicationThreadList) {
			((CommunicationModuleThread) item).interrupt();
			CommunicationModule.debugPrint(this.getClass(), "interrupted other thread: <" + item.getName() + ">");
		}

		CommunicationModule
				.debugPrint(this.getClass(),
						"communication module was interrupted");

	}

	private void requestToServant(MessageADT m) {
		this.demultiplexer.pass(m);
	}

	private void replyToProxy(MessageADT mReturn) {
		synchronized (communicationThreadList) {
			CommunicationModule.debugPrint(this.getClass(),
					"searching for right thread");
			for (Thread item : communicationThreadList) {
				if (((CommunicationModuleThread) item).getSendMessage()
						.getMessageID() == mReturn.getMessageID()) {
					((CommunicationModuleThread) item)
							.setReceivedMessage(mReturn);
					((CommunicationModuleThread) item).interrupt();
					CommunicationModule.debugPrint(this.getClass(),
							"found right thread and interrupted");
				}
			}
		}
	}

	public static CommunicationModuleThread getNewCommunicationThread(
			MessageADT m) {
		CommunicationModuleThread c = new CommunicationModuleThread(m);
		CommunicationModule
				.debugPrint("mware_lib.CommunicationModule: created new thread <"
						+ c.getName() + ">");
		synchronized (communicationThreadList) {
			communicationThreadList.add(c);
		}
		c.start();
		return c;
	}

	public static synchronized int messageIDCounter() {
		return messageIDCounter++;
	}

	public static void removeThreadFromList(CommunicationModuleThread c) {
		synchronized (communicationThreadList) {
			CommunicationModule
					.debugPrint("mware_lib.CommunicationModule: remove CommunicatioModuleThread from list <"
							+ c.getName() + ">");
			communicationThreadList.remove(c);

		}
	}

	public static InetAddress getLocalHost() {
		return localHost;
	}

	public static int getCommunicationmoduleport() {
		return COMMUNICATIONMODULEPORT;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
		try {
			this.serverSocket.close();
		} catch (IOException e) {
			CommunicationModule.debugPrint(this.getClass(),
					"closed server socket");
			// TODO
			e.printStackTrace();
		}
		;
	}

	public static void debugPrint(String text) {
		if (ObjectBroker.DEBUG) {
			System.out.println(text);
		}
	}

	public static void debugPrint(Class<?> klasse, String text) {
		if (ObjectBroker.DEBUG) {
			System.out.println(klasse.getName() + ": " + text);
		}
	}

	// TODO fuer lokales testen kann port geaendert werden
	public static void setCommunicatiomoduleport(int port) {
		COMMUNICATIONMODULEPORT = port;
	}

}
