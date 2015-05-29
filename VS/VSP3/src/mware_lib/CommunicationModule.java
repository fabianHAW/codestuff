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
 * @author Fabian
 * 
 *         Lauscht auf einem Socket auf eingehende Nachrichten von entfernten
 *         Kommunikationsmodulen (Request (Server-Seite) und Reply
 *         (Client-Seite)) und realisiert so die Umsetzung des
 *         Request-/Reply-Protokolls. Ob eine Nachricht ein Request oder ein
 *         Reply ist, wird anhand des Nachrichtentyps (Request oder Reply)
 *         ermittelt, der als Parameter in MessageADT enthalten ist. Bei einem
 *         Request muss auf Client-Seite ermittelt werden, auf welchem Host der
 *         Servant zu finden ist. Bei einem Reply muss auf Server-Seite, die
 *         Ursprungsadresse des Requests aus der MessageADT ermittelt werden, um
 *         den Reply zum Client zurück senden zu können.
 */

public class CommunicationModule extends Thread {

	private ServerSocket serverSocket;
	private ObjectInputStream input;
	private static InetAddress localHost;
	private static int COMMUNICATIONMODULEPORT;
	private static final int REQUEST = 0;
	private static final int REPLY = 1;
	private boolean isAlive;
	private static int messageIDCounter;

	public static ReferenceModule refMod = new ReferenceModule();

	private static List<Thread> communicationThreadList;
	private RequestDemultiplexer demultiplexer;

	public CommunicationModule() {
		this.demultiplexer = new RequestDemultiplexer();
		this.isAlive = true;
		messageIDCounter = 0;

		try {
			this.serverSocket = new ServerSocket(0);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		COMMUNICATIONMODULEPORT = this.serverSocket.getLocalPort();
		communicationThreadList = new ArrayList<Thread>();
		try {
			localHost = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
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
				/*
				 * vsp3_sequ_server: 1. MessageADT m received
				 */
				this.input = new ObjectInputStream(socket.getInputStream());
				MessageADT m = (MessageADT) this.input.readObject();

				CommunicationModule.debugPrint(this.getClass(),
						"received new MessageADT");

				/*
				 * vsp3_sequ_server: MessageType == Request
				 */
				if (m.getMessageType() == REQUEST) {
					CommunicationModule.debugPrint(this.getClass(),
							"REQUEST received");
					requestToServant(m);
				}
				/*
				 * vsp3_sequ_server: MessageType == Reply
				 */
//				else if (m.getMessageType() == REPLY) {
//					CommunicationModule.debugPrint(this.getClass(),
//							"REPLY received");
//					/*
//					 * vsp3_sequ_server: 1.3: Reply zum Proxy weitergeben
//					 */
//					replyToProxy(m);
//				}
			} catch (IOException e) {
				CommunicationModule
						.debugPrint(this.getClass(), "Socket closed");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Wurde das Kommunikations-Modul gestoppt, so muss es auch die noch
		 * laufenden Hilfs-Threads unterbrechen
		 */
		synchronized (communicationThreadList) {
			for (Thread item : communicationThreadList) {
				((CommunicationModuleThread) item).interrupt();
				CommunicationModule.debugPrint(this.getClass(),
						"interrupted other thread: <" + item.getName() + ">");
			}
		}

		CommunicationModule.debugPrint(this.getClass(),
				"communication module was interrupted");
	}

	/**
	 * Server-Seite: Leitet einen empfangenen Request an den
	 * Anforderungs-Demultiplexer weiter
	 * 
	 * @param m
	 *            MessageADT mit allen notwendigen Informationen
	 */
	private void requestToServant(MessageADT m) {
		/*
		 * vsp3_sequ_server: 1.1: weiterleiten des Requests
		 */
		this.demultiplexer.pass(m);
	}

	/**
	 * Client-Seite: Sucht den zustaendigen Hilfs-Thread aus der Liste, teilt
	 * ihm die empfangene Nachricht mit und weckt ihn auf
	 * 
	 * @param mReturn
	 *            empfangene MessageADT
	 */
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

	/**
	 * Erzeugt einen neuen Hilfs-Thread, fuegt ihn der Liste hinzu und startet
	 * ihn
	 * 
	 * @param m
	 *            MessageADT die an die Server-Seite weitergeleitet werden soll
	 * @return neu erzeugten Hilfs-Thread
	 */
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

	/**
	 * Zaehlt die eindeutige Nachrichtennummer hoch
	 * 
	 * @return neue Nachrichtennummer
	 */
	public static synchronized int messageIDCounter() {
		return messageIDCounter++;
	}

	/**
	 * Loescht einen Hilfs-Thread aus der Liste der Hilfs-Thread
	 * 
	 * @param c
	 *            Hilfs-Thread der geloescht werden soll
	 */
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

	/**
	 * Schaltet das Kommunikations-Modul ab
	 */
	public void communicationModuleShutdown() {
		this.isAlive = false;
		try {
			this.serverSocket.close();
		} catch (IOException e) {
			CommunicationModule.debugPrint(this.getClass(),
					"closed server socket");
		}
	}

	/**
	 * Erzeugt Debug-Ausgaben auf der Konsole, sofern Debug-Flag auf true
	 * 
	 * @param text
	 *            Nachricht die ausgegeben werden soll
	 */
	public static void debugPrint(String text) {
		if (ObjectBroker.DEBUG) {
			System.out.println(text);
		}
	}

	/**
	 * Erzeugt Debug-Ausgaben auf der Konsole, sofern Debug-Flag auf true
	 * 
	 * @param klasse
	 *            die Klasse die diese Methode aufruft
	 * @param text
	 *            Nachricht die ausgegeben werden soll
	 */
	public static void debugPrint(Class<?> klasse, String text) {
		if (ObjectBroker.DEBUG) {
			System.out.println(klasse.getName() + ": " + text);
		}
	}
}
