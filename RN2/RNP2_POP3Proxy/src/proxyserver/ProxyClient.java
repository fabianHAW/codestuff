package proxyserver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
		TIMEINTERVAL = 30000;
		this.accountList = new ArrayList<POP3Account>();
		commands = new Command();
	}
	
	public ProxyClient(List<POP3Account> a){
		//this.connection = connection;
		TIMEINTERVAL = 30000;
		this.accountList = a;
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
		
		
		try {
			fetchMails( POP3Proxy.getKnownAccounts() );
		} catch (InvalidUserException e) {
			System.out.println("ProxyClient.run: fetchMails throws InvalidUserException.");
			e.printStackTrace();
		} catch (InvalidPasswordException e) {
			System.out.println("ProxyClient.run: fetchMails throws InvalidPasswordException.");
			e.printStackTrace();
		} catch (InvalidNumberException e) {
			System.out.println("ProxyClient.run: fetchMails throws InvalidNumberException.");
			e.printStackTrace();
		}
		
		//loop(every 30secs) do fuer jeden account holen
			//loop(i to accountList.length) authentifizieren und mails holen
				//loop(j to numberOfMail) alle vorhandenen Mails vom Server holen und speichern
	}
	
	public void fetchMails(List<POP3Account> accounts) throws InvalidUserException, InvalidPasswordException, InvalidNumberException{
		for(POP3Account a : accounts){
			try {
				//Socket zu Mailserver einrichten
				Socket connectionToMailserver= new Socket(a.getAddress(), a.getPort());
				
				//reader, writer zum Mailserver
				PrintWriter writer = new PrintWriter(new OutputStreamWriter(
						connectionToMailserver.getOutputStream(), StandardCharsets.UTF_8.name()), true);
				Scanner reader = new Scanner(new InputStreamReader(
						connectionToMailserver.getInputStream(), StandardCharsets.UTF_8.name()));
				
				//Alle Mails des Accounts anfordern.
				List<Email> mails = getMails(reader, writer, a);
		
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
	
	public List<Email> getMails(Scanner reader, PrintWriter writer, POP3Account account) throws InvalidUserException, InvalidPasswordException, InvalidNumberException{
		List<Email> mails = new ArrayList<Email>();
		List<String> tmpResponseOfListCommand = new ArrayList<String>();
		String response;
		
		//User Authentifikation
		writer.write(commands.user(account.getUser()));
		response = reader.nextLine();
		if(! response.matches("^(+OK)*")){
			throw new InvalidUserException("ProxyClient.getMails: Benutzername falsch.");
		}
		
		//Passwort Authentifikation
		writer.write(commands.pass(account.getPass()));
		response = reader.nextLine();
		if(! response.matches("^(+OK)*")){
			throw new InvalidPasswordException("ProxyClient.getMails: Passwort falsch.");
		}
		
		//LIST Kommando senden, List-Response in Liste speichern
		writer.write(commands.list());
		while(reader.hasNextLine()){
			tmpResponseOfListCommand.add(reader.nextLine());
		}
		
		int bytesize;
		String uidl;
		int tmpNr;
		String[] splitMessage;
		//Auswertung LIST Kommando, ggf. Mails empfangen und speichern.
		for(int i = 0; i < tmpResponseOfListCommand.size(); i++){
			splitMessage = tmpResponseOfListCommand.get(i).split(" ", 3); //3 = 1 zu viel?
			bytesize = Integer.parseInt(splitMessage[1]);
			tmpNr = Integer.parseInt(splitMessage[0]);
			
			//UIDL anfordern / TODO: Errorhandling - noMail?
			writer.write(commands.uidl(tmpNr));
			response = reader.nextLine();
			splitMessage = response.split(" ", 3);
			uidl = splitMessage[2];
			
			//Mail anfordern / TODO: Errorhandling - noMail?
			writer.write(commands.retr(tmpNr));
			response = reader.nextLine(); //Erste Antwort uberspringen (+OK n octets)
			response = "";
			while(reader.hasNextLine() && !response.equals(".")){
				response += reader.nextLine();
			}
			
			Email mail = new Email(response, bytesize, uidl);
			
			//Empfangene Mail loeschen.
			writer.write(commands.dele(tmpNr));
			
			mails.add(mail);
			
		}
		
		writer.write(commands.quit());
		
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
