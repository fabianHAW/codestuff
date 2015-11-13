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

		// byte[][] array_1 = new byte[][] {{ 1, 0, 1, 0, 1, 1 }};
		// byte[][] array_2 = new byte[][] {{ 1, 0, 0, 1, 0, 1 }};
		//
		// byte[][] array_3 = new byte[1][6];
		//
		// xor(array_1, array_3, array_2, 0);
		//
		// for(byte item : array_3[0]){
		// System.out.print(item);
		// }

		createDESInstances();

		if (status.equals("encrypt")) {
			System.out.println("Starte encrypt");
			encrypt();
		} else if (status.equals("decrypt")) {
			System.out.println("Starte decrypt");
			decrypt();
		} else
			usage();

	}

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
			System.out.println(new String(iv));
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
		byte[][] m = readFile();
		int raw = m.length;
		byte[][] c_1 = new byte[raw][8];
		byte[][] c_2 = new byte[raw][8];
		byte[][] c_3 = new byte[raw][8];

		cfb(m, c_1, 'e', 0);
		cfb(c_1, c_2, 'd', 1);
		cfb(c_2, c_3, 'e', 2);

		writeFile(m, c_3, raw);

	}

	private static void decrypt() {
		byte[][] c_3 = readFile();
		int raw = c_3.length;
		byte[][] c_1 = new byte[raw][8];
		byte[][] c_2 = new byte[raw][8];
		byte[][] m = new byte[raw][8];

		cfb(c_3, c_2, 'e', 2);
		cfb(c_2, c_1, 'd', 1);
		cfb(c_1, m, 'e', 0);

		writeFile(c_3, m, raw);
	}

	private static void cfb(byte[][] m, byte[][] c, char mode, int round) {
		System.out.println("Starten mit der Runde: " + (round + 1));
		System.out.println(new String(des[round].getKey()));

		byte[][] c_temp = new byte[m.length][8];

		for (int j = 0; j < m.length; j++) {

			switch (mode) {
			case 'e':
				if (j == 0) {
					des[round].encrypt(iv, 0, c_temp[j], 0);
					xor(m, c_temp, c, j);

//					 System.out.print("encrypt iv m: ");
//					 System.out.println(new String(m[j]).toCharArray());
					// System.out.print("encrypt iv c_temp: ");
					// System.out.println(new String(c_temp[j]).toCharArray());
//					 System.out.print("encrypt iv c: " + j + " ");
//					 System.out.println(new String(c[j]).toCharArray());
				} else {
					// System.out.print("encrypt c-1: " + j + " ");
					// System.out.println(new String(c[j-1]).toCharArray());

					des[round].encrypt(c_temp[j - 1], 0, c_temp[j], 0);
					//des[round].encrypt(c[j - 1], 0, c_temp[j], 0);
					// des[runde].encrypt(iv, 0, c_temp[j], 0);
					xor(m, c_temp, c, j);

//					 System.out.print("encrypt m: ");
//					 System.out.println(new String(m[j]).toCharArray());
					// System.out.print("encrypt c-1: " + j + " ");
					// System.out.println(new String(c_temp[j]).toCharArray());
//					 System.out.print("encrypt c: ");
//					 System.out.println(new String(c[j]).toCharArray());
				}
				break;
			case 'd':
				if (j == 0) {
					des[round].decrypt(iv, 0, c_temp[j], 0);
					xor(m, c_temp, c, j);

//					 System.out.print("decrypt iv m: ");
//					 System.out.println(new String(m[j]).toCharArray());
					// System.out.print("decrypt iv c_temp: ");
					// System.out.println(new String(c_temp[j]).toCharArray());
//					 System.out.print("decrypt iv c: " + j + " ");
//					 System.out.println(new String(c[j]).toCharArray());
				} else {
					// System.out.print("decrypt c-1: " + j + " ");
					// System.out.println(new String(c[j-1]).toCharArray());

					des[round].decrypt(c_temp[j - 1], 0, c_temp[j], 0);
					//des[round].decrypt(c[j - 1], 0, c_temp[j], 0);
					// des[runde].decrypt(iv, 0, c_temp[j], 0);
					xor(m, c_temp, c, j);

//					 System.out.print("decrypt m: ");
//					 System.out.println(new String(m[j]).toCharArray());
					// System.out.print("decrypt c-1 c_temp: ");
					// System.out.println(new String(c_temp[j]).toCharArray());
//					 System.out.print("decrypt c: ");
//					 System.out.println(new String(c[j]).toCharArray());
				}
				break;
			default:
				break;
			}
		}
	}

	private static byte[][] readFile() {
		byte[][] m = null;
		try {
			FileInputStream in = new FileInputStream(data_file_in);
			int raw = (int) Math.ceil((double) in.available() / 8);
			m = new byte[raw][8];

			for (int i = 0; i < m.length; i++) {
				in.read(m[i], 0, 8);
				in.skip(0);
				// System.out.println(new String(m[i]).toCharArray());
			}
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return m;
	}

	private static void writeFile(byte[][] sourceFile, byte[][] dest, int raw) {
		try {
			FileOutputStream out = new FileOutputStream(data_file_out);
			int counter = 0;
			for (int i = 0; i < 8; i++) {
				if (sourceFile[raw - 1][i] != 0)
					counter++;
				else
					break;
			}
			for (int i = 0; i < dest.length; i++) {
				if (i == raw - 1)
					out.write(dest[i], 0, counter);
				else
					out.write(dest[i], 0, 8);
				// System.out.println(new String(c_3[i]).toCharArray());
			}

			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void xor(byte[][] m, byte[][] c_temp, byte[][] c, int j) {
		int i = 0;
		// System.out.print("xor: ");
		// System.out.println(new String(m[j]).toCharArray());
		for (byte item : c_temp[j]) {
			c[j][i] = (byte) (item ^ m[j][i++]);
			// i++;
		}
	}

	private static void usage() {

	}

}
