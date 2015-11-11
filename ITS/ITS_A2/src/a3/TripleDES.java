package a3;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import util.DES;

public class TripleDES {

	private static String daten_datei_ein = "";
	private static String schluessel_datei = "";
	private static String daten_datei_aus = "";
	private static String status = "";

	private static DES des[] = new DES[3];
	
//	private static byte[] key1 = new byte[8];
//	private static byte[] key2 = new byte[8];
//	private static byte[] key3 = new byte[8];
	private static byte[] iv = new byte[8];

	public static void main(String[] args) {
		String path = System.getProperty("user.dir").replace("bin", "data") + System.getProperty("file.separator")
				+ "a3" + System.getProperty("file.separator");
		daten_datei_ein = path + args[0];
		schluessel_datei = path + args[1];
		daten_datei_aus = path + args[2];
		status = args[3];

		readData();

		if (status.equals("encrypt"))
			encrypt();
		else if (status.equals("decrypt"))
			decrypt();
		else
			usage();

	}

	private static void readData() {
		try {
			FileInputStream in = new FileInputStream(schluessel_datei);
			//byte[] temp = new byte[32];
			byte[] temp = new byte[8];
//			int counter = 0;
			
			//in.read(temp);

			for(int i = 0; i < 3; i++){
				in.read(temp, 0, 8);
				in.skip(0);
				des[i] = new DES(temp);
			}
			in.read(temp, 0, 8);
			System.arraycopy(temp, 0, iv, 0, 8);
			
//			for(int i = 0; i < 4; i++){
//				for(int j = i * 8; j < (i+1) * 8; j++){
//					
//				}
//			}
			
//			for (int i = 0; i < key1.length; i++) {
//				key1[i] = temp[counter++];
//			}
//			for (int i = 0; i < key2.length; i++) {
//				key2[i] = temp[counter++];
//			}
//			for (int i = 0; i < key3.length; i++) {
//				key3[i] = temp[counter++];
//			}
//			for (int i = 0; i < iv.length; i++) {
//				iv[i] = temp[counter++];			iv = temp;
//			}

//			System.out.println(new String(key1).toCharArray());
//			System.out.println(new String(key2).toCharArray());
//			System.out.println(new String(key3).toCharArray());
//			System.out.println(new String(iv).toCharArray());

			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void encrypt() {

	}

	private static void decrypt() {

	}

	private static void usage() {

	}

}
