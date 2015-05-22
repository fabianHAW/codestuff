package test;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class Test {

	public static void main(String[] args) throws UnknownHostException {
		System.out.println(InetAddress.getLocalHost());

		
		
//		try {
//			Enumeration<NetworkInterface> b = NetworkInterface
//					.getNetworkInterfaces();
//			while (b.hasMoreElements()) {
//				for (InterfaceAddress f : b.nextElement()
//						.getInterfaceAddresses()) {
//					//System.out.println(f.getAddress());
//					if (f.getAddress().isSiteLocalAddress())
//						System.out.println(f.getAddress());
//				}
//			}
//		} catch (SocketException e) {
//			e.printStackTrace();
//		}
	}

}
