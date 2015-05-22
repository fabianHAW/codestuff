package test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class NeuerThread extends Thread{
	private int val = 0;
	
	public void run(){
		synchronized (this) {
			try {
//				ServerSocket s = new ServerSocket(50001);
//				Socket so = s.accept();
				Thread.sleep(2000);
				System.out.println("send notify");
				val = 4;
				notify();				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} //catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
		}
	}
	
	public int getVal(){ 
		return this.val;
	}
	
	public static void test(){
		System.out.println("blaaa");
	}
}