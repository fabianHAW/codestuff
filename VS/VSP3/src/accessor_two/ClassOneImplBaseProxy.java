package accessor_two;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import mware_lib.MessageADT;

/**
 * 
 * @author Francis und Fabian
 * 
 */

public class ClassOneImplBaseProxy extends ClassOneImplBase {

	private InetAddress inetAddress;
	private String host = "localhost";
	private int port = 50002;

	public double methodOne(String param1, double param2)
			throws SomeException112 {
		prepareAndSendMessage(param1, param2, "methodOne");
		MessageADT m = listenToSocket();
		
		List<Exception> exceptionList = m.getExceptionList();
		if(exceptionList.size() != 0){
			for(Exception item : exceptionList){
				throw (SomeException112) item;
			}
		}
		
		return Double.parseDouble(new String(m.getReturnVal()));
	}

	public double methodTwo(String param1, double param2)
			throws SomeException112, SomeException304 {
		prepareAndSendMessage(param1, param2, "methodTwo");
		MessageADT m = listenToSocket();
		
		List<Exception> exceptionList = m.getExceptionList();
		if(exceptionList.size() != 0){
			for(Exception item : exceptionList){
				if(item instanceof SomeException112){
					throw (SomeException112) item;
					}
				else if(item instanceof SomeException304){
					throw (SomeException304) item;
				}
			}
		}

		return Double.parseDouble(new String(m.getReturnVal()));
	}

	private void prepareAndSendMessage(String param1, double param2, String mName){
		Socket s = null;
		ObjectOutputStream o;
		try {
			this.inetAddress = InetAddress.getByName(host);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<byte[]> values = new ArrayList<byte[]>();
		values.add(param1.getBytes());
		values.add(String.valueOf(param2).getBytes());
		
		//welche entfertne objektreferenz?
		MessageADT m = new MessageADT(this.inetAddress, -1, mName, 0, null,
				null, values, null);
		try {
			s = new Socket(this.inetAddress, this.port);
			o = new ObjectOutputStream(s.getOutputStream());
			o.writeObject(m);
			o.close();
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public MessageADT listenToSocket() {
		MessageADT m = null;
		try {
			ServerSocket proxySocket = new ServerSocket();
			Socket s = proxySocket.accept();
			ObjectInputStream i = new ObjectInputStream(s.getInputStream());
			m = (MessageADT) i.readObject();
			
			i.close();
			s.close();
			proxySocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return m;
	}

}
