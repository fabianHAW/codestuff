package a3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import util.DES;

public class TripleDESNeu {

	private static String data_file_in = "";
	private static String key_file = "";
	private static String data_file_out = "";
	private static String status = "";

	private static DES des[] = new DES[3];
	private static final int SIZE = 8;
	private static byte[] iv = new byte[SIZE];

	public static void main(String[] args) throws UnsupportedEncodingException {
		String path = System.getProperty("user.dir").replace("bin", "data") + System.getProperty("file.separator")
				+ "a3" + System.getProperty("file.separator");

		if (args.length < 3)
			usage(path);
		else {
			data_file_in = path + args[0];
			key_file = path + args[1];
			data_file_out = path + args[2];
			status = args[3];

			createDESInstances();

			if (status.equals("encrypt") || status.equals("decrypt")) {
				crypt(status);
			} else
				usage(path);
		}

	}

	/**
	 * Create 3 DES instances with the given keys in the key-file and save the
	 * init-vector.
	 */

	private static void createDESInstances() {
		try {
			FileInputStream in = new FileInputStream(key_file);
			InputStreamReader isr = new InputStreamReader(in, "ISO-8859-15");
			byte[] temp = new byte[SIZE];
			char[] tempc = new char[SIZE];

			for (int i = 0; i < 4; i++) {
				isr.read(tempc, 0, SIZE);
				for (int j = 0; j < tempc.length; j++) {
					temp[j] = (byte) tempc[j];
				}
				if (i < 3)
					des[i] = new DES(temp);
				else {
					for (int j = 0; j < tempc.length; j++) {
						iv[j] = (byte) tempc[j];
					}
				}
			}

			isr.close();
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void crypt(String status) {
		byte[][] m = readFile();
		int raw = m.length;
		byte[] c1_temp = new byte[SIZE];
		byte[][] c_out = new byte[raw][SIZE];
		byte[] des_block = new byte[SIZE];

		des_block = tripleDES(iv);
		// Blockweises verschluesseln in CFB-Modus
		for (int i = 0; i < m.length; i++) {
			xor(m[i], des_block, c1_temp);
			System.arraycopy(c1_temp, 0, c_out[i], 0, SIZE);
			// Triple DES
			if (status.equals("encrypt"))
				des_block = tripleDES(c1_temp);
			else if (status.equals("decrypt"))
				des_block = tripleDES(m[i]);
		}

		writeFile(m, c_out, raw);
	}

	private static byte[] tripleDES(byte[] in) {
		byte[] temp1 = new byte[SIZE];
		byte[] temp2 = new byte[SIZE];
		byte[] out = new byte[SIZE];
		des[0].encrypt(in, 0, temp1, 0);
		des[1].decrypt(temp1, 0, temp2, 0);
		des[2].encrypt(temp2, 0, out, 0);
		return out;
	}

	/**
	 * Read the input-file.
	 * 
	 * @return input-file in 2D byte-array
	 */
	private static byte[][] readFile() {
		byte[][] m = null;
		try {
			File file = new File(data_file_in);
			FileInputStream in = new FileInputStream(file);
			int raw = (int) Math.ceil((double) file.length() / SIZE);
			m = new byte[raw][SIZE];

			for (int i = 0; i < m.length; i++) {
				in.read(m[i], 0, SIZE);
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return m;
	}

	/**
	 * Write the 2D byte-array to output-file. If the the last byte of src
	 * contains 0-bits at the end, they are going to write to output-file to
	 * fill the last byte.
	 * 
	 * @param src
	 *            The source-byte-array
	 * @param dest
	 *            The destination-byte-array
	 * @param raw
	 *            The last raw of source-byte-array
	 */
	private static void writeFile(byte[][] src, byte[][] dest, int raw) {
		try {
			FileOutputStream out = new FileOutputStream(data_file_out);
			int counter = 0;
			for (int i = 0; i < SIZE; i++) {
				if (src[raw - 1][i] != 0)
					counter++;
				else
					break;
			}
			for (int i = 0; i < dest.length; i++) {
				if (i == raw - 1)
					out.write(dest[i], 0, counter);
				else
					out.write(dest[i], 0, SIZE);
			}
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * XOR the 2D-message-byte-array with the encrypted/decrypted
	 * 2D-temporary-array.
	 * 
	 * @param m
	 *            2D-message-byte-array
	 * @param c_temp
	 *            2D-temporary-array
	 * @param c
	 *            2D-destination-array
	 * @param j
	 *            Raw of the 2D-array
	 */
	private static void xor(byte[] m, byte[] c_temp, byte[] c) {
		for (int j = 0; j < SIZE; j++) {
			c[j] = (byte) (m[j] ^ c_temp[j]);
		}
	}

	private static void usage(String path) {
		System.out.println("usage: java " + TripleDESNeu.class.getCanonicalName() + " <filename to encrypt/decrypt> "
				+ "<key-filename> " + "<output filename> " + "<encrypt/decrypt>");
		System.out.println("the files need to be in the directory: " + path);
	}

}
