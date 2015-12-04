package a2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.AlgorithmParameters;
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
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class SSF {

	private static String path = System.getProperty("user.dir").replace("bin", "data")
			+ System.getProperty("file.separator") + "a2" + System.getProperty("file.separator");
	private static String pathToRSAPrvKey = System.getProperty("user.dir").replace("bin", "data")
			+ System.getProperty("file.separator") + "a1" + System.getProperty("file.separator");

	private static String pubKeyFile = "";
	private static String prvKeyFile = "";
	private static String fileIn = "";
	private static String fileOut = "";
	private static PrivateKey prvKey = null;
	private static PublicKey pubKey = null;
	private static SecretKey aesKey = null;
	private static byte[] aesKeyEnc = null;
	private static byte[] sigBytes = null;

	public static void main(String[] args) {
		if (args.length < 4)
			usage();
		else {
			prvKeyFile = pathToRSAPrvKey + args[0];
			pubKeyFile = path + args[1];
			fileIn = path + args[2];
			fileOut = path + args[3];
			readPrvKeyFile();
			readPubKeyFile();
			generateAESKey();
			generateSignatureAndSign();
			encryptAESKey();
			boolean succ = readAndEncryptData();
			if(succ)
				System.out.println("Datei erfolgreich angelegt!");
			else
				System.out.println("Es ist ein Fehler beim Erstellen der Datei aufgetreten!");
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

	private static void generateAESKey() {
		try {
			KeyGenerator kg = KeyGenerator.getInstance("AES");
			kg.init(128);
			aesKey = kg.generateKey();
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Es gibt keine Implementierung des AES-Algorithmus: " + e);
		}
	}

	private static void generateSignatureAndSign() {
		try {
			Signature rsaSig = Signature.getInstance("SHA256withRSA");
			rsaSig.initSign(prvKey);
			rsaSig.update(aesKey.getEncoded());
			sigBytes = rsaSig.sign();
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Es gibt keine Implementierung des SHA256withRSA-Algorithmus: " + e);
		} catch (InvalidKeyException e) {
			System.out.println("Der angegebene private RSA-Schlüssel ist nicht korrekt: " + e);
		} catch (SignatureException e) {
			System.out.println("Beim Signieren des AES-Schlüssel ist ein Fehler aufgetreten: " + e);
		}
	}

	private static void encryptAESKey() {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);

			byte[] aesEncData = cipher.update(aesKey.getEncoded());
			byte[] aesEncRest = cipher.doFinal();
			aesKeyEnc = concatenate(aesEncData, aesEncRest);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			System.out.println(
					"Es gibt keine Implementierung des RSA-Algorithmus, oder beim Padding ist ein Fehler aufgetreten: "
							+ e);
		} catch (InvalidKeyException e) {
			System.out.println("Der angegebene öffentliche RSA-Schlüssel ist nicht korrekt: " + e);
		} catch (IllegalBlockSizeException e) {
			System.out.println("Die Blockgröße beim Verschlüsseln des AES-Schlüssels ist nicht korrekt: " + e);
		} catch (BadPaddingException e) {
			System.out.println("Das Padding ist nicht korrekt abgelaufen: " + e);
		}
	}

	private static boolean readAndEncryptData() {
		try {
			DataInputStream dis = new DataInputStream(new FileInputStream(fileIn));
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(fileOut));
			File fileInputLength = new File(fileIn);
			byte[] fileInput = new byte[(int) fileInputLength.length()];
			byte[] encData = null;
			byte[] encDataRest = null;
			byte[] encDataTotal = null;
			Cipher cipher = Cipher.getInstance("AES/CTR/PKCS5Padding");
			byte[] cipherBytes = null;
			byte[] apBytes = null;

			dis.read(fileInput);
			dis.close();

			cipher.init(Cipher.ENCRYPT_MODE, aesKey);
			encData = cipher.update(fileInput);
			encDataRest = cipher.doFinal();
			encDataTotal = concatenate(encData, encDataRest);

			dos.writeInt(aesKeyEnc.length);
			dos.write(aesKeyEnc);
			dos.writeInt(sigBytes.length);
			dos.write(sigBytes);

			cipherBytes = cipher.getParameters().getEncoded();
			AlgorithmParameters ap = AlgorithmParameters.getInstance("AES");
			ap.init(cipherBytes);
			apBytes = ap.getEncoded();

			dos.writeInt(apBytes.length);
			dos.write(apBytes);
			dos.write(encDataTotal);

			dos.close();
			return true;
		} catch (FileNotFoundException e) {
			System.out.println("Die angegebene Datei wurde nicht gefunden: " + e);
		} catch (IOException e) {
			System.out.println("Es liegt ein Eingabe-/Ausgabe-Fehler vor: " + e);
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Es gibt keine Implementierung des AES/CTR/PKCS5Padding- oder AES-Algorithmus: " + e);
		} catch (NoSuchPaddingException e) {
			System.out.println("Beim Padding ist ein Fehler aufgetreten: " + e);
		} catch (InvalidKeyException e) {
			System.out.println("Der AES-Schlüssel ist nicht korrekt: " + e);
		} catch (IllegalBlockSizeException e) {
			System.out.println("Die Blockgröße beim Verschlüsseln der Daten ist nicht korrekt: " + e);
		} catch (BadPaddingException e) {
			System.out.println("Das Padding ist nicht korrekt abgelaufen: " + e);
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
				+ " <public RSA-key name>" + " <filename to encrypt>" + " <filename for decrypted file>");
		System.out.println("the RSA-private-key-file need to be in the directory: " + pathToRSAPrvKey);
		System.out.println("the RSA-public-key-file and the file to encrypt need to be in the directory: " + path);
	}

}
