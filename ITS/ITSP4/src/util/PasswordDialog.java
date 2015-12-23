/**
 * 
 */
package util;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * Zeige ein Fenster mit einem Passworteingabe-Feld als modalen Dialog
 * 
 */
public class PasswordDialog extends JDialog {
	private static final long serialVersionUID = 873795915516658285L;

	private JPasswordField passwortField = null;
	private boolean status = false;

	/*
	 * ----------------- Zugriffsmethoden auf die Ergebnisse
	 * ----------------------------------------------------
	 */
	public boolean statusOK() {
		/* Liefere den Status: OK = true, cancel = false */
		return status;
	}

	public char[] getPassword() {
		/* Liefere das Passwort (Array-Referenz) */
		return passwortField.getPassword();
	}

	/*
	 * ----------------- Konstruktor
	 * ----------------------------------------------------
	 */
	public PasswordDialog(String userName) {
		// Modalen Dialog �ber Parameter festlegen
		super((Frame) null, "HAW Department Informatik WP IT-Sicherheit", true);

		String okLabel = "  OK  ";
		String cancelLabel = "Abbruch";

		// Fenster-Parameter setzen
		setLocation(200, 100);
		Container contentPanel = getContentPane();
		contentPanel.setLayout(new BorderLayout(100, 20));
		// Text-Anzeige
		JLabel labelMessage1 = new JLabel("<html><body><font size=\"+1\">"
				+ "<em>Bitte Passwort f�r " + userName
				+ " eingeben: </em></font><br>" + "</body></html>",
				SwingConstants.CENTER);
		contentPanel.add(labelMessage1, BorderLayout.NORTH);

		// F�llregionen links und rechts
		JLabel leftDist = new JLabel(" ");
		contentPanel.add(leftDist, BorderLayout.WEST);
		JLabel rightDist = new JLabel(" ");
		contentPanel.add(rightDist, BorderLayout.EAST);

		passwortField = new JPasswordField(15);

		// passwortField auf Fenster verankern
		contentPanel.add(passwortField, BorderLayout.CENTER);

		// Panel f�r die OK/Cancel-Buttons erzeugen
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());

		// OK-Button
		JButton okButton = new JButton(okLabel);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				status = true;
				closeDialog();
			}
		});
		buttonPanel.add(okButton);

		// Abbrechen-Button
		JButton cancelButton = new JButton(cancelLabel);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				status = false;
				closeDialog();
			}
		});
		buttonPanel.add(cancelButton);

		// Buttons auf Fenster verankern
		contentPanel.add(buttonPanel, BorderLayout.SOUTH);

		// Anzeige
		pack();
		setVisible(true);
	}

	private void closeDialog() {
		// Fenster schlie�en
		setVisible(false);
		dispose();
	}

	public static void main(String argv[]) throws Exception {
		// Testmethode!
		PasswordDialog myClient = new PasswordDialog("Testuser");
		System.out.println("Status: " + myClient.statusOK());
		System.out.println("Passwort: " + new String(myClient.getPassword()));
	}

}
