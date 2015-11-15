package a2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import a1.LCG;

public class HC1 {

	private static long startvalue;
	private static long key;
	private static String file_en = "data/test";
	private static String file_de = "data/test_out";
	private static String file_en_new = "data/test_en_neu";

	public static void main(String[] args) {
		// startwert = Long.valueOf(args[0]);
		// datei = args[1];
		startvalue = 123456789;
		LCG lcg = new LCG(startvalue);
		key = lcg.nextValue();
		encrypt();
		decrypt();
	}

	/**
	 * Encrypt an input file with a given key with XOR and write it to a new
	 * file.
	 */
	private static void encrypt() {
		try {
			DataInputStream din = new DataInputStream(new FileInputStream(file_en));
			DataOutputStream dout = new DataOutputStream(new FileOutputStream(file_de));

			while (din.available() != 0) {
				dout.writeLong(din.readByte() ^ key);
			}

			dout.close();
			din.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Decrypt an input file with a given key with XOR and write it to a new
	 * file.
	 */
	private static void decrypt() {
		try {
			DataInputStream din = new DataInputStream(new FileInputStream(file_de));
			DataOutputStream dout = new DataOutputStream(new FileOutputStream(file_en_new));

			while (din.available() != 0) {
				dout.write((char) (din.readLong() ^ key));
			}

			dout.close();
			din.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
