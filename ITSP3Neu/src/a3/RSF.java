package a3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import a2.SSF;

public class RSF {

	private static String path = System.getProperty("user.dir").replace("bin", "data")
			+ System.getProperty("file.separator") + "a3" + System.getProperty("file.separator");
	private static String pathToRSAPubKey = System.getProperty("user.dir").replace("bin", "data")
			+ System.getProperty("file.separator") + "a1" + System.getProperty("file.separator");
	private static String pathToSSFFile = System.getProperty("user.dir").replace("bin", "data")
			+ System.getProperty("file.separator") + "a2" + System.getProperty("file.separator");

	private static String pubKeyFile = "";
	private static String prvKeyFile = "";
	private static String fileIn = "";
	private static String fileOut = "";
	private static PrivateKey prvKey = null;
	private static PublicKey pubKey = null;
	private static byte[] aesKeyEncBytes = null;
	private static byte[] aesKeyDecBytes = null;
	private static byte[] sigBytes = null;
	private static byte[] algParam = null;
	private static byte[] encData = null;
	private static byte[] decDataTotal = null;
	private static SecretKeySpec aesKeySpec = null;

	public static void main(String[] args) {
		if (args.length < 4)
			usage();
		else {
			prvKeyFile = path + args[0];
			pubKeyFile = pathToRSAPubKey + args[1];
			fileIn = pathToSSFFile + args[2];
			fileOut = path + args[3];
			readPubKeyFile();
			readPrvKeyFile();
			readDataFile();
			decryptAESKey();
			decryptDataFile();
			writeDecryptedDataToFile();
			if (checkSignature())
				System.out.println("Signatur erfolgreich verifiziert und Datei entschlüsselt!");
			else
				System.out.println("Signatur nicht erfolgreich verifiziert!");
		}
	}

	private static void readPubKeyFile() {
		try {
			DataInputStream dis = new DataInputStream(new FileInputStream(pubKeyFile));

			int lenOwner = dis.readInt();
			byte[] ownerName = new byte[lenOwner];
			dis.read(ownerName);
			int lenPubKey = dis.readInt();
			byte[] pubKeyByte = new byte[lenPubKey];
			dis.read(pubKeyByte);

			X509EncodedKeySpec x509 = new X509EncodedKeySpec(pubKeyByte);
			KeyFactory kf = KeyFactory.getInstance("RSA");
			pubKey = kf.generatePublic(x509);

			dis.close();
		} catch (FileNotFoundException e) {
			System.out.println("Die angegebene Datei wurde nicht gefunden: " + e);
		} catch (IOException e) {
			System.out.println("Es liegt ein Eingabe-/Ausgabe-Fehler vor: " + e);
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Es gibt keine Implementierung des RSA-Algorithmus: " + e);
		} catch (InvalidKeySpecException e) {
			System.out.println("Die Schlüssel Spezifikation ist nicht korrekt: " + e);
		}
	}

	private static void readPrvKeyFile() {
		try {
			DataInputStream dis = new DataInputStream(new FileInputStream(prvKeyFile));

			int lenOwner = dis.readInt();
			byte[] ownerName = new byte[lenOwner];
			dis.read(ownerName);
			int lenPrvKey = dis.readInt();
			byte[] prvKeyByte = new byte[lenPrvKey];
			dis.read(prvKeyByte);

			PKCS8EncodedKeySpec pks8 = new PKCS8EncodedKeySpec(prvKeyByte);
			KeyFactory kf = KeyFactory.getInstance("RSA");
			prvKey = kf.generatePrivate(pks8);

			dis.close();
		} catch (FileNotFoundException e) {
			System.out.println("Die angegebene Datei wurde nicht gefunden: " + e);
		} catch (IOException e) {
			System.out.println("Es liegt ein Eingabe-/Ausgabe-Fehler vor: " + e);
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Es gibt keine Implementierung des RSA-Algorithmus: " + e);
		} catch (InvalidKeySpecException e) {
			System.out.println("Die Schlüssel Spezifikation ist nicht korrekt: " + e);
		}
	}

	private static void readDataFile() {
		try {
			File file = new File(fileIn);
			DataInputStream dis = new DataInputStream(new FileInputStream(fileIn));

			int lenAESKey = 0;
			int lenSigBytes = 0;
			int lenAlgParam = 0;
			lenAESKey = dis.readInt();

			aesKeyEncBytes = new byte[lenAESKey];
			dis.read(aesKeyEncBytes);

			lenSigBytes = dis.readInt();
			sigBytes = new byte[lenSigBytes];
			dis.read(sigBytes);

			lenAlgParam = dis.readInt();
			algParam = new byte[lenAlgParam];
			dis.read(algParam);

			encData = new byte[(int) file.length()
					- ((3 * (Integer.SIZE / 8)) + lenAESKey + lenSigBytes + lenAlgParam)];
			dis.read(encData);
			dis.close();

		} catch (FileNotFoundException e) {
			System.out.println("Die angegebene Datei wurde nicht gefunden: " + e);
		} catch (IOException e) {
			System.out.println("Es liegt ein Eingabe-/Ausgabe-Fehler vor: " + e);
		}
	}

	private static void decryptAESKey() {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, prvKey);
			byte[] aesDec = cipher.update(aesKeyEncBytes);
			byte[] aesRest = cipher.doFinal();
			aesKeyDecBytes = concatenate(aesDec, aesRest);

		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			System.out.println(
					"Es gibt keine Implementierung des RSA-Algorithmus, oder beim Padding ist ein Fehler aufgetreten: "
							+ e);
		} catch (InvalidKeyException e) {
			System.out.println("Der RSA-Schlüssel ist nicht korrekt: " + e);
		} catch (IllegalBlockSizeException e) {
			System.out.println("Die Blockgröße ist beim Entschlüsseln des AES-Schlüssels ist nicht korrekt: " + e);
		} catch (BadPaddingException e) {
			System.out.println("Das Padding ist nicht korrekt abgelaufen: " + e);
		}
	}

	private static void decryptDataFile() {
		try {
			Cipher cipher = Cipher.getInstance("AES/CTR/PKCS5Padding");
			AlgorithmParameters ag = AlgorithmParameters.getInstance("AES");
			ag.init(algParam);
			aesKeySpec = new SecretKeySpec(aesKeyDecBytes, "AES");
			cipher.init(Cipher.DECRYPT_MODE, aesKeySpec, ag);

			byte[] decData = cipher.update(encData);
			byte[] decDataRest = cipher.doFinal();
			decDataTotal = concatenate(decData, decDataRest);

		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			System.out.println(
					"Es gibt keine Implementierung des AES-Algorithmus, oder beim Padding im AES/CTR/PKCS5Padding-Modus ist ein Fehler aufgetreten: "
							+ e);
		} catch (IOException e) {
			System.out.println("Es liegt ein Eingabe-/Ausgabe-Fehler vor: " + e);
		} catch (InvalidKeyException e) {
			System.out.println("Der AES-Schlüssel ist nicht korrekt: " + e);
		} catch (InvalidAlgorithmParameterException e) {
			System.out.println("Die Algorithmischen Parameter sind nicht korrekt: " + e);
		} catch (IllegalBlockSizeException e) {
			System.out.println("Die Blockgröße beim Entschlüsseln der Daten ist nicht korrekt: " + e);
		} catch (BadPaddingException e) {
			System.out.println("Das Padding ist nicht korrekt abgelaufen: " + e);
		}
	}

	private static void writeDecryptedDataToFile() {
		try {
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(fileOut));
			dos.write(decDataTotal);
			dos.close();
		} catch (FileNotFoundException e) {
			System.out.println("Die angegebene Datei wurde nicht gefunden: " + e);
		} catch (IOException e) {
			System.out.println("Es liegt ein Eingabe-/Ausgabe-Fehler vor: " + e);
		}
	}

	private static boolean checkSignature() {
		try {
			Signature sig = Signature.getInstance("SHA256withRSA");
			sig.initVerify(pubKey);
			sig.update(aesKeyDecBytes);
			return sig.verify(sigBytes);
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Es gibt keine Implementierung des SHA256withRSA-Algorithmus: " + e);
		} catch (SignatureException e) {
			System.out.println("Beim Verifizieren des AES-Schlüssel ist ein Fehler aufgetreten: " + e);
		} catch (InvalidKeyException e) {
			System.out.println("Der angegeben öffentliche RSA-Schlüssel ist nicht korrekt: " + e);
		}
		return false;
	}

	/**
	 * Concatenate two byte arrays
	 */
	private static byte[] concatenate(byte[] ba1, byte[] ba2) {
		int len1 = ba1.length;
		int len2 = ba2.length;
		byte[] result = new byte[len1 + len2];

		// Fill with first array
		System.arraycopy(ba1, 0, result, 0, len1);
		// Fill with second array
		System.arraycopy(ba2, 0, result, len1, len2);

		return result;
	}

	private static void usage() {
		System.out.println("usage: java " + SSF.class.getCanonicalName() + " <private RSA-key name>"
				+ " <public RSA-key name>" + " <filename (ssf) to decrypt>" + " <filename for encrypted file>");
		System.out.println("the RSA-public-key-file need to be in the directory: " + pathToRSAPubKey);
		System.out.println("the RSA-private-key-file and the file to encrypt need to be in the directory: " + path);
	}

}
