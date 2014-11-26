package client;

/* FileCopyClient.java
 Version 0.1 - Muss erg�nzt werden!!
 Praktikum 3 Rechnernetze BAI4 HAW Hamburg
 Autoren:
 */

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

import fc_adt.*;

public class FileCopyClient extends Thread {

	// -------- Constants
	public final static boolean TEST_OUTPUT_MODE = false;

	public final int SERVER_PORT = 23000;
	public final int CLIENTCOPY_PORT = 23001;

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

	private FileInputStream readFileInput;
	byte [] fileData;
	//bereits gelesene Datenpakete aus der Datei
	private int justReaded = 0;
	//insgesamt zu lesende Datenpakete aus der Datei
	private int needsToRead = 0;
	//Flag um zu signalisieren, ob letztes Datenpaket bereits gelesen wurde
	private boolean eofReached = false;
	

	// Sendepufferpointer
	private List<FCpacket> sendBuf;
	private Semaphore accessBuffer;
	private int sendbase = 0;
	private int nextSeqNum = 1;
	
	//OLD, wurde im alten READFILE(n) verwendet 
	//private int skipPos = 0;

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
			this.client = new DatagramSocket(CLIENTCOPY_PORT,
					InetAddress.getByName(this.servername));
			this.readFileInput = new FileInputStream(this.sourcePath);
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
		
		//*****TESTAUSGABE START******
		/*System.out.println("controlpacketsize: " + controlPacket.getLen());
		System.out.println("controlpacketsize with seqnum: "
				+ controlPacket.getSeqNumBytesAndData().length);
		System.out.println("udp_packetsize: " + UDP_PACKET_SIZE);
		System.out.println("seqnum of controlpacket: " + controlPacket.getSeqNum());
		*///*****TESTAUSGABE STOP******
		
		DatagramPacket udpControlPacket;
		try {
			//+8 Bytes, wegen der Sequenznummer -> werden diese nicht mit drauf gerechnet
			//werden auf der Serverseite die uebergebenen Parameter nicht korrekt erkannt
			udpControlPacket = new DatagramPacket(controlPacket.getSeqNumBytesAndData(),
					controlPacket.getLen() + 8, InetAddress.getByName(this.servername), SERVER_PORT);
			
		} catch (UnknownHostException e1) {
			System.out.println("couldn't get inetadresse by name");
			e1.printStackTrace();
			udpControlPacket = null;
			this.connectionEstablished = false;
		}
		
		this.sendBuf.add(controlPacket);
		
		//da bereits controlPacket erstellst wurde, ein Paket weniger
		//readFile(this.windowSize - 1);
		readFile();
		
		try {
			if(udpControlPacket != null){
				this.raCl = new ReceiveAckClient();
				this.raCl.start();
				
				controlPacket.setTimestamp(System.nanoTime());
				this.client.send(udpControlPacket);
	
				controlPacket.setTimeout(this.expRTT + 4 * this.jitter);
				startTimer(controlPacket);
				
				//computeTimeoutValue(System.nanoTime() - controlPacket.getTimestamp());
				this.connectionEstablished = true;
			}
		} catch (IOException e) {
			System.out.println("couldn't write first packet");
			e.printStackTrace();
		
		}
		
		while (this.connectionEstablished && this.sendbase < needsToRead) {
			//durch die Liste wird bei 0 angefangen zu zaehlen, d.h. exklusive windowsize - 1
			if(this.nextSeqNum < this.sendbase + this.windowSize 
					&& this.nextSeqNum < this.sendBuf.size()){

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

		try {
			this.readFileInput.close();
		} catch (IOException e) {
			System.out.println("couldn't close fileinputstream");
			e.printStackTrace();
		}
		ReceiveAckClient.currentThread().interrupt();
		this.client.close();

		int checkCounter = 0;
		for(FCpacket item : this.sendBuf){
			if(item.isValidACK()){
				checkCounter++;
			}
		}
		
		//plus 1, da controlpacket ebenfalls in dem Sendepuffer enthalten
		if(checkCounter == this.justReaded + 1){
			System.out.println("successful sent!");
		}
		else{
			System.out.println("something went wrong! not all packets sent right!");
		}
		
		//********TESTAUSGABE OB ALLE PAKETE "GEACKED" WURDEN
		/*for(FCpacket item : sendBuf){
			System.out.println(item.toString());
		}*/
		
	}
	
	private void readFile(){
		try {
			this.fileData = Files.readAllBytes(Paths.get(sourcePath));
			//anzahl der Pakete die insgesamt in den Sendepuffer geschrieben werden muessen
			if(this.fileData.length < UDP_PACKET_SIZE - 8){
				this.needsToRead = 1;
			}
			else{
				//Pruefen ob round tats�chlich aufrundet
				this.needsToRead = Math.round(this.fileData.length / (UDP_PACKET_SIZE - 8));
				//Math.round
			}
			//System.out.println("datalength: " + this.fileData.length);
		} catch (IOException e) {
			System.out.println("can't read all bytes from sourcepath");
			e.printStackTrace();
		}
		
		storePacketsInSendBuffer(windowSize - 1);
	}

	private void storePacketsInSendBuffer(int n){
		int fileByteSize = UDP_PACKET_SIZE - 8;
		byte[] packetTmp;
		for(int i = 0; i < n; i++){
			if(this.needsToRead == 1 || this.justReaded == this.needsToRead){
				int packetLength = this.fileData.length % fileByteSize;
				packetTmp = new byte[packetLength];
				//*******TESTAUSGABE START***********
			/*	System.out.println("justreaded: " + this.justReaded);
				System.out.println("filebytesize: " + fileByteSize);
				System.out.println("packetlength: " + packetLength);
				*///*******TESTAUSGABE ENDE***********
				
				System.arraycopy(this.fileData, this.justReaded * fileByteSize, packetTmp, 0, packetLength);
				this.sendBuf.add(new FCpacket(this.justReaded + 1, packetTmp , packetLength));
				this.eofReached = true;
				//ist nach dem letzte paket um 1 groesser als needsToRead
				this.justReaded++;
				break;
			}
			else{
				packetTmp = new byte[fileByteSize];
				System.arraycopy(this.fileData, this.justReaded * fileByteSize, packetTmp, 0, fileByteSize);
				this.sendBuf.add(new FCpacket(this.justReaded + 1, packetTmp , fileByteSize));
				//ist nach dem letzte paket um 1 groesser als needsToRead
				this.justReaded++;
			}
			
		}
	//	System.out.println("justreaded: " + this.justReaded);
		//System.out.println("needstoread: " + this.needsToRead);
	}
	
	/* OLD READFILE -> hat nicht die gesamte Datei gelesen
	private void readFile(int n) {
		//return, wenn Ende der Datei bereits erreicht wurde
		if(eofReached){
			return;
		}
		
		//UDP_PACKET_SIZE - 8, weil das eigentliche Packet 1000 Bytes gross ist
		//die 8 Bytes sind nur fuer die SEQ-Nummer und gibt es beim lesen der Datei nicht
		int fileByteSize = UDP_PACKET_SIZE - 8;
		byte[] filePart = new byte[fileByteSize];
		
		for (int j = 0; j < n; j++) {
			try {
				this.readFileInput.skip(skipPos * fileByteSize);
				skipPos++;
				//-1, wenn Dateiende erreicht
				int returnval = this.readFileInput.read(filePart, 0, fileByteSize);
				System.out.println("returnval of readfile: " + returnval);
				if(returnval != -1){
					this.sendBuf.add(new FCpacket(skipPos, filePart, fileByteSize));
				}
				else{
					System.out.println("EOF reached");
					System.out.println("size of sendbuf after EOF reached: " + this.sendBuf.size());
					testOut("EOF reached");
					this.eofReached = true;
					break;
				}
			} catch (IOException e) {
				System.out.println("initializeRecBuf interrupt");
				e.printStackTrace();
			}
			
		}
		// liest die Datei an dem angegebenen Pfad
		// und speichert diese in einem Puffer (Liste von FCPackets) ab
		// this.inToFile.read(b, off, len);
		
		/*System.out.println("length of first packet in sendbuf: " + this.sendBuf.get(1).getLen());
		System.out.println("seqnum of first packet in sendbuf: " + this.sendBuf.get(1).getSeqNum());
		System.out.println("data of first packet in sendbuf: " + this.sendBuf.get(1).getData());
		*//*
		}*/

	private void sendNextSeqNumPacket() {
		FCpacket nextPacket = this.sendBuf.get(this.nextSeqNum);
		try {
			/*DatagramPacket udpNextPacket = new DatagramPacket(nextPacket.getSeqNumBytesAndData(),
					UDP_PACKET_SIZE, InetAddress.getByName(this.servername), SERVER_PORT);*/
			DatagramPacket udpNextPacket = new DatagramPacket(nextPacket.getSeqNumBytesAndData(),
					nextPacket.getLen() + 8, InetAddress.getByName(this.servername), SERVER_PORT);
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
		FC_Timer timer = new FC_Timer(timeoutValue, this, packet.getSeqNum());
		//FC_Timer timer = new FC_Timer(packet.getTimeout(), this, packet.getSeqNum());
		packet.setTimer(timer);
		//packet.setTimeout(computeTimeoutValue(sampleRTT)); EVTL. HIER TimeOutValue aufrunden
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
		if(!this.client.isClosed()){
			try {
				p.setTimestamp(System.nanoTime());
			//	this.timeoutValue = 2 * this.timeoutValue;
				startTimer(p);
				//startTimerOnRetransmit(p); 
				this.client.send(new DatagramPacket(p.getSeqNumBytesAndData(), p.getLen() + 8, 
						InetAddress.getByName(this.servername), SERVER_PORT));
			} catch (IOException e) {
				System.out.println("couldn't send datagram to server");
				e.printStackTrace();
			}
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
	}
	
	//ist nur auskommentiert...
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
		myClient.runFileCopyClient();
	}

	/**
	 * Empfangen der Quittungen als eigenen Thread implementiert
	 * 
	 * @author Francis Opoku und Fabian Reiber
	 *
	 */
	private class ReceiveAckClient extends Thread {

		private boolean isInterrupted = false;

		public void run() {
			byte[] buf = new byte[UDP_PACKET_SIZE];
			DatagramPacket udpPacket = new DatagramPacket(buf, UDP_PACKET_SIZE);
			FCpacket receivedFCpacket = null;

			while (!this.isInterrupted) {
				try {
					client.receive(udpPacket);
					receivedFCpacket = new FCpacket(udpPacket.getData(),
							udpPacket.getLength());
					testOut("received ack of packet: " + receivedFCpacket.getSeqNum());
					
					if (receivedFCpacket != null) {
						try {
							accessBuffer.acquire();
							int indexOfContainedPacket = sendBuf.indexOf(receivedFCpacket);
							if (indexOfContainedPacket != -1) {
							
							ackReceivedPacket(sendBuf.get(indexOfContainedPacket));
							//System.out.println("packet is inside of senbuffer");
							//System.out.println("sendbase: " + sendbase);
							
							//timeoutValue = expRTT + 4*jitter;
							
//							System.out.println("sendbase after ack is set of received packet: " + sendbase);
							
							if (sendbase == receivedFCpacket.getSeqNum()) {
									int counter = moveSendbase();
									//readFile(counter);
									if(!eofReached){
										storePacketsInSendBuffer(counter);
										}
									}
							}
							accessBuffer.release();
							} catch (InterruptedException e) {
								System.out.println("couldn't acquire semaphore");
								e.printStackTrace();
								}
							}
							
							if(receivedFCpacket.getSeqNum() == observedPacket){
								//long sampleRTT = System.nanoTime() - receivedFCpacket.getTimestamp();
								//computeTimeoutValue(sampleRTT);
								//expRTT = (expRTT + sampleRTT) / sendbase;	
								//chooseObservedPacket();
								}
							} catch (IOException e) {
								System.out.println("receive client was interrupted");
								this.isInterrupted = true;
								//e.printStackTrace();
								}
				}
			}

		private void ackReceivedPacket(FCpacket receivedPacket) {
			receivedPacket.setValidACK(true);
			cancelTimer(receivedPacket);
		}

		private int moveSendbase() {
			int counter = 0;
			//sendbase++;
			//if(sendbase < sendBuf.size()){
				//System.out.println(sendBuf.get(sendbase).toString());
				do{
					sendbase++;	
					counter++;
					if(sendbase == sendBuf.size()){
						break;
					}
				}while(sendBuf.get(sendbase).isValidACK());
			//}
			return counter;
		}
	}

}
