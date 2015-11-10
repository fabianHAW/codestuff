package a2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import a1.LCG;

public class HC1 {

	private static long startwert;
	private static long schluessel;
	private static String datei_en = "data/test";
	private static String datei_de = "data/test_out";
	private static String datei_en_neu = "data/test_en_neu";

	public static void main(String[] args) {
		// startwert = Long.valueOf(args[0]);
		// datei = args[1];
		startwert = 123456789;
		LCG lcg = new LCG(startwert);
		schluessel = lcg.nextValue();
		encrypt();
		decrypt();
	}

	private static void encrypt() {
		try {
			DataInputStream din = new DataInputStream(new FileInputStream(datei_en));
			DataOutputStream dout = new DataOutputStream(new FileOutputStream(datei_de));

			while (din.available() != 0) {
				dout.writeLong(din.readByte() ^ schluessel);
			}

			dout.close();
			din.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void decrypt() {
		try {
			DataInputStream din = new DataInputStream(new FileInputStream(datei_de));
			DataOutputStream dout = new DataOutputStream(new FileOutputStream(datei_en_neu));

			while (din.available() != 0) {
				dout.write((char) (din.readLong() ^ schluessel));
			}

			dout.close();
			din.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
