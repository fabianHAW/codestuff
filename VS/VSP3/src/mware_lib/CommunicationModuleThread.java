package mware_lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class CommunicationModuleThread extends Thread {

	private MessageADT sendMessage;
	private MessageADT receivedMessage;
	private Socket socket;
	private InputStream input;
	private ObjectInputStream oinput;
	private OutputStream output;
	private ObjectOutputStream ooutput;
	
	public CommunicationModuleThread(MessageADT m) {
		// TODO Auto-generated constructor stub
		sendMessage = m;
		try {
			socket = new Socket(m.getObjectRef().getInetAddress(), m.getObjectRef().getPort());
			input = socket.getInputStream();
			oinput = new ObjectInputStream(input);
			output = socket.getOutputStream();
			ooutput = new ObjectOutputStream(output);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run(){
		synchronized(this){
			try {
				ooutput.writeObject(sendMessage);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				receivedMessage = (MessageADT)oinput.readObject();
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		notify();
	}
	
	public MessageADT getReceivedMessage(){
		return receivedMessage;
	}

}
