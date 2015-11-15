package a3;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import util.DES;

public class TripleDES {

	private static String data_file_in = "";
	private static String key_file = "";
	private static String data_file_out = "";
	private static String status = "";

	private static DES des[] = new DES[3];
	private static byte[] iv = new byte[8];

	public static void main(String[] args) {
		String path = System.getProperty("user.dir").replace("bin", "data") + System.getProperty("file.separator")
				+ "a3" + System.getProperty("file.separator");
		data_file_in = path + args[0];
		key_file = path + args[1];
		data_file_out = path + args[2];
		status = args[3];

		createDESInstances();

		if (status.equals("encrypt")) {
			System.out.println("start encrypt");
			encrypt();
		} else if (status.equals("decrypt")) {
			System.out.println("star decrypt");
			decrypt();
		} else
			usage(path);

	}

	/**
	 * Create 3 DES instances with the given keys in the key-file and save the
	 * init-vector.
	 */
	private static void createDESInstances() {
		try {
			FileInputStream in = new FileInputStream(key_file);
			byte[] temp = new byte[8];

			for (int i = 0; i < 3; i++) {
				in.read(temp, 0, 7);
				in.skip(1);
				des[i] = new DES(temp);
			}
			in.read(temp, 0, 8);
			System.arraycopy(temp, 0, iv, 0, 8);

			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Starts the 3DES and encrypts a given file and write it to the outputfile.
	 */
	private static void encrypt() {
		byte[][] m = readFile();
		int raw = m.length;
		byte[][] c_1 = new byte[raw][8];
		byte[][] c_2 = new byte[raw][8];
		byte[][] c_3 = new byte[raw][8];

		cfb_enc(m, c_1, 'e', 0);
		cfb_enc(c_1, c_2, 'd', 1);
		cfb_enc(c_2, c_3, 'e', 2);

		writeFile(m, c_3, raw);
	}

	/**
	 * Starts the 3DES and decrypts a given file and write it to the outputfile.
	 */
	private static void decrypt() {
		byte[][] c_3 = readFile();
		int raw = c_3.length;
		byte[][] c_1 = new byte[raw][8];
		byte[][] c_2 = new byte[raw][8];
		byte[][] m = new byte[raw][8];

		cfb_dec(c_3, c_2, 'e', 2);
		cfb_dec(c_2, c_1, 'd', 1);
		cfb_dec(c_1, m, 'e', 0);

		writeFile(c_3, m, raw);
	}

	/**
	 * The CFB-mode for the encryption of 3DES.
	 * 
	 * @param m
	 *            2D input-byte-array
	 * @param c
	 *            2D Encrypted byte-array
	 * @param mode
	 *            e for encrypt with DES, or d for decrypt with DES
	 * @param round
	 *            Number of the key which will be used.
	 */
	private static void cfb_enc(byte[][] m, byte[][] c, char mode, int round) {
		System.out.println("start with round: " + (round + 1) + " and key: " + new String(des[round].getKey()));

		byte[][] c_temp = new byte[m.length][8];

		for (int i = 0; i < m.length; i++) {

			switch (mode) {
			case 'e':
				if (i == 0) {
					des[round].encrypt(iv, 0, c_temp[i], 0);
					xor(m, c_temp, c, i);
					// System.out.print("encrypt iv m: ");
					// System.out.println(new String(m[j]).toCharArray());
					// System.out.print("encrypt iv c_temp: ");
					// System.out.println(new String(c_temp[j]).toCharArray());
					// System.out.print("encrypt iv c: " + j + " ");
					// System.out.println(new String(c[j]).toCharArray());
				} else {
					des[round].encrypt(c[i - 1], 0, c_temp[i], 0);
					xor(m, c_temp, c, i);
				}
				break;
			case 'd':
				if (i == 0) {
					des[round].decrypt(iv, 0, c_temp[i], 0);
					xor(m, c_temp, c, i);
				} else {
					des[round].decrypt(c[i - 1], 0, c_temp[i], 0);
					xor(m, c_temp, c, i);
				}
				break;
			default:
				break;
			}
		}
	}

	/**
	 * The CFB-mode for the decryption of 3DES.
	 * 
	 * @param m
	 *            2D input-byte-array
	 * @param c
	 *            Encrypted 2D byte-array
	 * @param mode
	 *            e for encrypt with DES, or d for decrypt with DES
	 * @param round
	 *            Number of the key which will be used.
	 */
	private static void cfb_dec(byte[][] m, byte[][] c, char mode, int round) {
		System.out.println("start with round: " + (round + 1) + " and key: " + new String(des[round].getKey()));

		byte[][] c_temp = new byte[m.length][8];

		for (int j = 0; j < m.length; j++) {

			switch (mode) {
			case 'e':
				if (j == 0) {
					des[round].encrypt(iv, 0, c_temp[j], 0);
					xor(m, c_temp, c, j);
				} else {
					des[round].encrypt(m[j - 1], 0, c_temp[j], 0);
					xor(m, c_temp, c, j);
				}
				break;
			case 'd':
				if (j == 0) {
					des[round].decrypt(iv, 0, c_temp[j], 0);
					xor(m, c_temp, c, j);
				} else {
					des[round].decrypt(m[j - 1], 0, c_temp[j], 0);
					xor(m, c_temp, c, j);
				}
				break;
			default:
				break;
			}
		}
	}

	/**
	 * Read the input-file.
	 * 
	 * @return input-file in 2D byte-array
	 */
	private static byte[][] readFile() {
		byte[][] m = null;
		try {
			FileInputStream in = new FileInputStream(data_file_in);
			int raw = (int) Math.ceil((double) in.available() / 8);
			m = new byte[raw][8];

			for (int i = 0; i < m.length; i++) {
				in.read(m[i], 0, 8);
				in.skip(0);
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
			for (int i = 0; i < 8; i++) {
				if (src[raw - 1][i] != 0)
					counter++;
				else
					break;
			}
			for (int i = 0; i < dest.length; i++) {
				if (i == raw - 1)
					out.write(dest[i], 0, counter);
				else
					out.write(dest[i], 0, 8);
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
	private static void xor(byte[][] m, byte[][] c_temp, byte[][] c, int j) {
		int i = 0;
		for (byte item : c_temp[j]) {
			c[j][i] = (byte) (item ^ m[j][i++]);
		}
	}

	private static void usage(String path) {
		System.out.println("usage: java " + TripleDES.class.getCanonicalName() + " <filename to encrypt/decrypt> "
				+ "<key-filename> " + "<output filename> " + "<encrypt/decrypt>");
		System.out.println("the files need to be in the directory: " + path);
	}

}
