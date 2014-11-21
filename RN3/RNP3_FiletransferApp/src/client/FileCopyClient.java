package client;

/* FileCopyClient.java
 Version 0.1 - Muss erg�nzt werden!!
 Praktikum 3 Rechnernetze BAI4 HAW Hamburg
 Autoren:
 */

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

import fc_adt.*;

public class FileCopyClient extends Thread {

	// -------- Constants
	public final static boolean TEST_OUTPUT_MODE = false;

	public final int SERVER_PORT = 23000;

	public final int UDP_PACKET_SIZE = 1008;

	// -------- Public parms
	public String servername;

	public String sourcePath;

	public String destPath;

	public int windowSize;

	public long serverErrorRate;

	// -------- Variables
	// current default timeout in nanoseconds
	private long timeoutValue = 100000000L;
	private long expRTT = timeoutValue - 1000; //Bestaetigen lassen vom Prof, da frei erfunden.
	private long jitter = 0;
	private long observedPacket;
	
	private DatagramSocket client;
	private boolean connectionEstablished;

	private ReceiveAckClient raCl;

	private FileInputStream inToFile;
	

	// Sendepufferpointer
	private List<FCpacket> sendBuf;
	private Semaphore accessBuffer;
	private int sendbase = 0;
	private int nextSeqNum = 1;
	private int skipPos = 0;

	// ... ToDo

	// Constructor
	public FileCopyClient(String serverArg, String sourcePathArg,
			String destPathArg, String windowSizeArg, String errorRateArg) {
		servername = serverArg;
		sourcePath = sourcePathArg;
		destPath = destPathArg;
		windowSize = Integer.parseInt(windowSizeArg);
		serverErrorRate = Long.parseLong(errorRateArg);
		observedPacket = 0;
		
		try {
			this.client = new DatagramSocket(SERVER_PORT,
					InetAddress.getByName(this.servername));
			this.inToFile = new FileInputStream(this.sourcePath);
			this.sendBuf = new LinkedList<FCpacket>();
			this.accessBuffer = new Semaphore(1);

		} catch (UnknownHostException e) {
			System.out.println("can't create an new datagramsocket object");
			e.printStackTrace();
		} catch (SocketException e) {
			System.out.println("can't create an new datagramsocket object");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.out.println("can't read file");
			e.printStackTrace();
		}
	}

	public void runFileCopyClient() {

		FCpacket controlPacket = makeControlPacket();
		DatagramPacket udpControlPacket = new DatagramPacket(controlPacket.getData(),
				UDP_PACKET_SIZE);
		
		this.sendBuf.add(controlPacket);
		readFile(this.windowSize - 1);
		
		try {
			this.raCl = new ReceiveAckClient();
			this.raCl.start();
			controlPacket.setTimestamp(System.nanoTime());
			this.client.send(udpControlPacket);

			controlPacket.setTimeout(this.expRTT + 4 * this.jitter);
			startTimer(controlPacket);
			
			computeTimeoutValue(System.nanoTime() - controlPacket.getTimestamp());
			this.connectionEstablished = true;
		} catch (IOException e) {
			System.out.println("couldn't write first packet");
			this.connectionEstablished = false;
			e.printStackTrace();
		}

		while (this.connectionEstablished && this.sendbase <= this.sendBuf.size()) {
			
			//durch die Liste wird bei 0 angefangen zu zaehlen, d.h. exklusive windowsize - 1
			if(nextSeqNum < sendbase + windowSize){
				try {
					this.accessBuffer.acquire();
					sendNextSeqNumPacket();
					this.accessBuffer.release();
				} catch (InterruptedException e) {
					System.out.println("can't acquire semaphore");
					e.printStackTrace();
				}
			}			
		}


		this.raCl.setInterrupt(true);
		this.client.close();

	}

	private void readFile(int n) {
		byte[] filePart = new byte[UDP_PACKET_SIZE];
		
		for (int j = 0; j < n; j++) {
			try {
				this.inToFile.skip(skipPos * UDP_PACKET_SIZE);
				skipPos++;
				this.inToFile.read(filePart, 0, UDP_PACKET_SIZE);
				this.sendBuf.add(new FCpacket(skipPos, filePart, UDP_PACKET_SIZE));
			} catch (IOException e) {
				System.out.println("initializeRecBuf interrupt");
			}
			
		}
		// liest die Datei an dem angegebenen Pfad
		// und speichert diese in einem Puffer (Liste von FCPackets) ab
		// this.inToFile.read(b, off, len);
	}

	private void sendNextSeqNumPacket() {
		FCpacket nextPacket = this.sendBuf.get(this.nextSeqNum);
		DatagramPacket udpNextPacket = new DatagramPacket(nextPacket.getData(),
				UDP_PACKET_SIZE);
		try {
			nextPacket.setTimestamp(System.nanoTime());
			nextPacket.setTimeout(this.expRTT + 4 * this.jitter);
			startTimer(nextPacket);
			this.client.send(udpNextPacket);
			this.nextSeqNum++;
		} catch (IOException e) {
			System.out.println("couldn't send next packet");
			e.printStackTrace();
		}
	}

	/**
	 *
	 * Timer Operations
	 */
	public void startTimer(FCpacket packet) {
		/* Create, save and start timer for the given FCpacket */
		//FC_Timer timer = new FC_Timer(timeoutValue, this, packet.getSeqNum());
		FC_Timer timer = new FC_Timer(packet.getTimeout(), this, packet.getSeqNum());
		packet.setTimer(timer);
		timer.start();
	}
	
	public void startTimerOnRetransmit(FCpacket packet) {
		/* Create, save and start timer for the given FCpacket */
		//FC_Timer timer = new FC_Timer(timeoutValue, this, packet.getSeqNum());
		FC_Timer timer = new FC_Timer(2 * packet.getTimeout(), this, packet.getSeqNum());
		packet.setTimer(timer);
		timer.start();
	}

	public void cancelTimer(FCpacket packet) {
		/* Cancel timer for the given FCpacket */
		testOut("Cancel Timer for packet" + packet.getSeqNum());

		if (packet.getTimer() != null) {
			packet.getTimer().interrupt();
		}
	}

	/**
	 * Implementation specific task performed at timeout
	 */
	public void timeoutTask(long seqNum) {
		FCpacket p = this.sendBuf.get((int)seqNum);
		//computeTimeoutValue(sampleRTT);
		try {
			p.setTimestamp(System.nanoTime());
		//	this.timeoutValue = 2 * this.timeoutValue;
			startTimerOnRetransmit(p); 
			this.client.send(new DatagramPacket(p.getData(), p.getLen()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 *
	 * Computes the current timeout value (in nanoseconds)
	 */
	public void computeTimeoutValue(long sampleRTT) {
		float x = 0.25f;
		float y = x / 2;
		//sampleRTT = systm.nanotime - gettimestamp
		this.expRTT = (long) ((1 - y) * this.expRTT + (y * sampleRTT));
		jitter = (long) ((1 - x) * jitter + x * Math.abs(sampleRTT) - expRTT);
		
		//this.timeoutValue = 2 * this.timeoutValue;
		/*
	    long millis = expRTT / 1000000L;
	    int nanos = (int) (expRTT % 1000000L);
	*/
	}
	
	private void chooseObservedPacket(){
		FCpacket oPacket = this.sendBuf.get((int)observedPacket);
		for(int i = sendbase; i < sendBuf.size(); i++){
			FCpacket packet = sendBuf.get(i);
			if(!packet.isValidACK() && packet.compareTo(oPacket) != 0){
				observedPacket = sendBuf.get(i).getSeqNum();
				break;
			}
		}
	}
	
	/**
	 * erzeugt das erste zu sendene Paket an den Server Return value: FCPacket
	 * with (0 destPath;windowSize;errorRate)
	 */
	public FCpacket makeControlPacket() {
		/*
		 * Create first packet with seq num 0. Return value: FCPacket with (0
		 * destPath ; windowSize ; errorRate)
		 */
		String sendString = destPath + ";" + windowSize + ";" + serverErrorRate;
		byte[] sendData = null;
		try {
			sendData = sendString.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new FCpacket(0, sendData, sendData.length);
	}

	public void testOut(String out) {
		if (TEST_OUTPUT_MODE) {
			System.err.printf("%,d %s: %s\n", System.nanoTime(), Thread
					.currentThread().getName(), out);
		}
	}

	public static void main(String argv[]) throws Exception {
		FileCopyClient myClient = new FileCopyClient(argv[0], argv[1], argv[2],
				argv[3], argv[4]);
		// geht das so? oder muss run() erzeugt werden und myClient.run()
		// aufgerufen werden?
		myClient.runFileCopyClient();
	}

	/**
	 * Empfangen der Quittungen als eigenen Thread implementiert
	 * 
	 * @author Francis Opoku und Fabian Reiber
	 *
	 */
	private class ReceiveAckClient extends Thread {

		private DatagramSocket raClient;
		private boolean isInterrupted;

		public ReceiveAckClient() {
			try {
				this.raClient = new DatagramSocket(SERVER_PORT,
						InetAddress.getByName(servername));
				this.isInterrupted = false;
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void run() {
			byte[] buf = new byte[UDP_PACKET_SIZE];
			DatagramPacket udpPacket = new DatagramPacket(buf, UDP_PACKET_SIZE);
			FCpacket receivedFCpacket = null;

			while (!this.isInterrupted) {
				try {
					this.raClient.receive(udpPacket);
					receivedFCpacket = new FCpacket(udpPacket.getData(),
							udpPacket.getLength());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (receivedFCpacket != null) {
					if (sendBuf.contains(receivedFCpacket)) {
						ackReceivedPacket(receivedFCpacket);

						timeoutValue = expRTT + 4*jitter;
						
						if (sendbase == receivedFCpacket.getSeqNum()) {
							try {
								accessBuffer.acquire();
								int counter = moveSendbase(receivedFCpacket.getSeqNum());
								readFile(counter);
								//getMorePackets();
								accessBuffer.release();
							} catch (InterruptedException e) {
								System.out.println("can't acquire semaphore");
								e.printStackTrace();
							}
						}
						
						if(receivedFCpacket.getSeqNum() == observedPacket){
							long sampleRTT = System.nanoTime() - receivedFCpacket.getTimestamp();
							computeTimeoutValue(sampleRTT);
							expRTT = (expRTT + sampleRTT) / sendbase;	
							chooseObservedPacket();
						}
						
						
					}
				}
			}
		}

		private void ackReceivedPacket(FCpacket receivedPacket) {
			receivedPacket.setValidACK(true);
			cancelTimer(receivedPacket);
		}

		private int moveSendbase(long seqNum) {
			int counter = 0;
			for(int i = sendbase; i <= seqNum; i++){
				if(sendBuf.get(i).isValidACK()){
					sendbase++;
					counter++;
				}
			}
			return counter;
		}
/*
		private void getMorePackets() {

		}
*/
		public void setInterrupt(boolean i) {
			this.isInterrupted = i;
		}
	}

}