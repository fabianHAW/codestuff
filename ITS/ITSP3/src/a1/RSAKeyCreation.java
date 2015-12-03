package a1;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAKeyCreation {

	private static String name = "";
	private static String path = System.getProperty("user.dir").replace("bin", "data")
			+ System.getProperty("file.separator") + "a1" + System.getProperty("file.separator");
	private static KeyPair kp = null;

	public static void main(String[] args) {

		if (args.length < 1)
			usage();
		else {
			name = args[0];
			createRSAKeyPair();
			boolean succ = writeKeysToFile();
			if (succ)
				System.out.println("SUCCESS: generated new RSA-Key-Pair-Files in " + path);
			else
				System.out.println("FAILED: something went wrong, while creating new RSA-Key-Pair-Files");
		}
	}

	private static void createRSAKeyPair() {
		try {
			//generate new RSA-Key-Pair
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(2048);
			kp = kpg.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	private static boolean writeKeysToFile() {
		try {
			DataOutputStream dosPub = new DataOutputStream(new FileOutputStream(path + name + ".pub"));
			DataOutputStream dosPrv = new DataOutputStream(new FileOutputStream(path + name + ".prv"));

			PublicKey pubKey = kp.getPublic();
			byte[] pubKeyEncoded = pubKey.getEncoded();
			PrivateKey prvKey = kp.getPrivate();
			byte[] prvKeyEncoded = prvKey.getEncoded();

			/**
			 * write public key to file 
			 * 1. Länge des Inhaber‐Namens (integer) 
			 * 2. Inhaber‐Name (Bytefolge) 
			 * 3. Länge des öffentlichen Schlüssels (integer) 
			 * 4. Öffentlicher Schlüssel (Bytefolge) [X.509‐Format]
			 */
			dosPub.writeInt(name.length());
			dosPub.writeBytes(name);
			dosPub.writeInt(pubKeyEncoded.length);
			X509EncodedKeySpec x509 = new X509EncodedKeySpec(pubKeyEncoded);
			dosPub.write(x509.getEncoded());

			dosPub.close();

			/**
			 * write private key to file
			 * 1. Länge des Inhaber‐Namens (integer) 
			 * 2. Inhaber‐Name (Bytefolge) 
			 * 3. Länge des privaten Schlüssels (integer) 
			 * 4. Privater Schlüssel (Bytefolge) [PKCS8‐Format] 
			 */
			dosPrv.writeInt(name.length());
			dosPrv.writeBytes(name);
			dosPrv.writeInt(prvKeyEncoded.length);
			PKCS8EncodedKeySpec pkcs8 = new PKCS8EncodedKeySpec(prvKeyEncoded);
			dosPrv.write(pkcs8.getEncoded());

			dosPrv.close();
			return true;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static void usage() {
		System.out.println("usage: java " + RSAKeyCreation.class.getCanonicalName() + " <owner name>");
	}
}
