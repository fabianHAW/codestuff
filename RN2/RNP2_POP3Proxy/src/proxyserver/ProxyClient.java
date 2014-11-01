package proxyserver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import proxyserver.exceptions.InvalidNumberException;
import proxyserver.exceptions.InvalidPasswordException;
import proxyserver.exceptions.InvalidUserException;
import account.Email;
import account.POP3Account;

/**
 * Ein ProxyClient-Thread der die Kommunikation mit dem POP3MailServer steuert
 * @author Fabian Reiber und Francis Opoku
 *
 */

public class ProxyClient extends Thread{

//	private Socket connection;
	/**
	 * TIMEINTERVAL in ms
	 */
	private static int TIMEINTERVAL;
	/**
	 * Liste der Accounts von denen die Mails abegrufen werden sollen
	 */
	private List<POP3Account> accountList;
	private Command commands;
	
	public ProxyClient(){
		//this.connection = connection;
		TIMEINTERVAL = 2000;
		this.accountList = new ArrayList<POP3Account>();
		commands = new Command();
	}
	
	public ProxyClient(List<POP3Account> a){
		//this.connection = connection;
		TIMEINTERVAL = 2000;
		this.accountList = a;
		commands = new Command();
	}
	
	/**
	 * Hauptroutine in der der Proxy ggü. dem POP3MailServer als Client fungiert und
	 * alle TIMEINTERVAL ms die Mails abholt und in einem "Abhol-Account" sichert
	 */
	public void run(){
		try {
			Thread.sleep(TIMEINTERVAL);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("ProxyClient.run(): Sleep interrupted.");
			e.printStackTrace();
		}
		
		List<POP3Account> accounts = POP3Proxy.getKnownAccounts();
		if (! (accounts.size() == 0)) {
			try {
				fetchMails(POP3Proxy.getKnownAccounts());
			} catch (InvalidUserException e) {
				System.out
						.println("ProxyClient.run: fetchMails throws InvalidUserException.");
				e.printStackTrace();
			} catch (InvalidPasswordException e) {
				System.out
						.println("ProxyClient.run: fetchMails throws InvalidPasswordException.");
				e.printStackTrace();
			} catch (InvalidNumberException e) {
				System.out
						.println("ProxyClient.run: fetchMails throws InvalidNumberException.");
				e.printStackTrace();
			}
		}
		//loop(every 30secs) do fuer jeden account holen
			//loop(i to accountList.length) authentifizieren und mails holen
				//loop(j to numberOfMail) alle vorhandenen Mails vom Server holen und speichern
	}
	
	public void fetchMails(List<POP3Account> accounts) throws InvalidUserException, InvalidPasswordException, InvalidNumberException{
		SSLSocketFactory sslSocketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
		for(POP3Account a : accounts){
			try {
				//Socket zu Mailserver einrichten
				SSLSocket connectionToMailserver= (SSLSocket)sslSocketFactory.createSocket(a.getAddress(), a.getPort());
			
				connectionToMailserver.setUseClientMode(true);
				connectionToMailserver.setEnabledProtocols(connectionToMailserver.getSupportedProtocols());
				
				String[] suites = connectionToMailserver.getSupportedCipherSuites();
				connectionToMailserver.setEnabledCipherSuites(suites);
				connectionToMailserver.addHandshakeCompletedListener(
						new HandshakeCompletedListener() {
							
							@Override
							public void handshakeCompleted(HandshakeCompletedEvent event) {
								System.out.println("Handshake abgeschlossen.");
								System.out.println("Verwendete cipher suite: " + event.getCipherSuite());
								
							}
				});
				connectionToMailserver.startHandshake();
				//connectionToMailserver.setKeepAlive(true);
				System.out.println("getKeepAlive " + connectionToMailserver.getKeepAlive());
				
				//reader, writer zum Mailserver
				PrintWriter writer = new PrintWriter(new OutputStreamWriter(
						connectionToMailserver.getOutputStream(), StandardCharsets.UTF_8.name()), true);
				Scanner reader = new Scanner(new InputStreamReader(
						connectionToMailserver.getInputStream(), StandardCharsets.UTF_8.name()));
				
				//Alle Mails des Accounts anfordern.
				List<Email> mails = getMails(connectionToMailserver,reader, writer, a);
		
				connectionToMailserver.close();
				
				saveMails(mails, a);
			} catch (IOException e) {
				System.out.println("ProxyClient.fetchMails: Connection to "
						+ "mailserver " + a.getServeraddress() + " on Port " + a.getPort()
						+ " failed.");
				e.printStackTrace();
			}
		}
	}
	
	public List<Email> getMails(SSLSocket connection, Scanner reader, PrintWriter writer, POP3Account account) throws InvalidUserException, InvalidPasswordException, InvalidNumberException{
		List<Email> mails = new ArrayList<Email>();
		List<String> tmpResponseOfListCommand = new ArrayList<String>();
		String response;
		
		//User Authentifikation
		/*
		System.out.println("getMails:writer.checkError: " + writer.checkError());
		System.out.println("accountData: " + account.getPass() + " " + account.getUser() + " " + account.getServeraddress());
		System.out.println("commands.user(): " + commands.user(account.getUser()));
		*/
		response = reader.nextLine();
		System.out.println("response1: " + response);
		
		writer.println(commands.user(account.getUser()));
		
		/*
		System.out.println("" + connection.isConnected());
		System.out.println("" + connection.isInputShutdown());
		System.out.println("" + connection.isOutputShutdown());
		System.out.println("" + connection.isBound());
		System.out.println("" + connection.isClosed());
		System.out.println(reader.ioException());
		System.out.println("after write1");
		*/
		
		response = reader.nextLine();
		System.out.println("response2: " + response);
		if(! response.matches("\\+OK.*")){
			throw new InvalidUserException("ProxyClient.getMails: Benutzername falsch.");
		}
		
		//Passwort Authentifikation
		writer.println(commands.pass(account.getPass()));
		response = reader.nextLine();
		System.out.println("response3: " + response);
		if(! response.matches("\\+OK.*")){
			throw new InvalidPasswordException("ProxyClient.getMails: Passwort falsch.");
		}
		
		//LIST Kommando senden, List-Response in Liste speichern
		writer.println(commands.list());
		reader.nextLine();
		while(!response.equals(".") && reader.hasNextLine()){
			response = reader.nextLine();
			System.out.println("responsex: " + response);
			tmpResponseOfListCommand.add(response);
		}
		
		int bytesize;
		String uidl;
		int tmpNr;
		String[] splitMessage;
		//Auswertung LIST Kommando, ggf. Mails empfangen und speichern.
		for(int i = 0; i < tmpResponseOfListCommand.size(); i++){
			System.out.println("i: " + i);
			splitMessage = tmpResponseOfListCommand.get(i).split(" ", 3); //3 = 1 zu viel?
			for(int j = 0; j < tmpResponseOfListCommand.size(); j++){
				System.out.println("tmpResponse...:" + tmpResponseOfListCommand.get(j));
			}
			System.out.println("" + splitMessage[0] + " " + splitMessage[1]);
			bytesize = Integer.parseInt(splitMessage[1]);
			tmpNr = Integer.parseInt(splitMessage[0]);
			System.out.println("Integer: " + tmpNr);
			//UIDL anfordern / TODO: Errorhandling - noMail?
			writer.println(commands.uidl(tmpNr));
			System.out.println(commands.uidl(tmpNr));
			response = reader.nextLine();
			System.out.println("response5: " + response);
			splitMessage = response.split(" ", 3);
			uidl = splitMessage[2];
			
			//Mail anfordern / TODO: Errorhandling - noMail?
			writer.println(commands.retr(tmpNr));
			response = reader.nextLine(); //Erste Antwort uberspringen (+OK n octets)
			System.out.println("response6: " + response);
			response = "";
			while(!response.equals(".") && reader.hasNextLine()){
				response += reader.nextLine();
				//System.out.println("responsex2: " + response);
			}
			System.out.println("after " + i);
			Email mail = new Email(response, bytesize, uidl);
			System.out.println("after "  + i);
			//Empfangene Mail loeschen.
			writer.println(commands.dele(tmpNr));
			System.out.println("after "  + i);
			mails.add(mail);
			System.out.println("after "  + i);
		}
		System.out.println("after end");
		writer.println(commands.quit());
		System.out.println("after end 2");
		return mails;
	}
	/**
	 * geholte Mails in einen Abholaccount sichern
	 */
	private void saveMails(List<Email> mails, POP3Account a){
		a.addMails(mails);
	}
	
	//ggf. raus? da nur server adden kann?!
	/**
	 * hinzufuegen eines neuen Accounts in die Liste
	 * @param a POP3Account
	 */
	public void addAccount(POP3Account a){
		this.accountList.add(a);
	}
}
