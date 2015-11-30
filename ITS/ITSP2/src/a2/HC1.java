package a2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import a1.LCG;

public class HC1 {

	/**
	 * 2 Dateien mit diff -s file1 file2 vergleichen.
	 */
	
	private static long startvalue;
	private static String file = "";
	private static byte numericKey;

	public static void main(String[] args) {
		String path = System.getProperty("user.dir").replace("bin", "data") + System.getProperty("file.separator")
				+ "a2" + System.getProperty("file.separator");

		if (args.length < 2)
			usage(path);
		else {
			startvalue = Long.valueOf(args[0]);
			file = path + args[1];
			createsChiffreFile();
		}
	}

	public static void createsChiffreFile() {
		File chiffreFile = new File(file + "_chiff");

		LCG lcg = new LCG(startvalue);
		
		try {
			FileInputStream in = new FileInputStream(file);
			FileOutputStream out = new FileOutputStream(chiffreFile);

			byte[] buffer;
			byte[] chiffreBuffer;

			int a = file.length();
			buffer = new byte[a];
			in.read(buffer);
			in.close();
			chiffreBuffer = new byte[a];

			for (int i = 0; i < a; i++) {
				numericKey = (byte) lcg.nextValue();
				chiffreBuffer[i] = (byte) (numericKey ^ buffer[i]);
			}

			out.write(chiffreBuffer);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void usage(String path) {
		System.out.println(
				"usage: java " + HC1.class.getCanonicalName() + " <startvalue>" + " <filename to encrypt/decrypt> ");
		System.out.println("the files need to be in the directory: " + path);
	}
}
