package src.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import redis.clients.jedis.Jedis;

public class Main {

	private static Jedis j = new Jedis("localhost", 6379);

	/**
	 * Ermittelt die PLZ (oder mehrere) die zur eingegebene Stadt gehoeren.
	 * 
	 * @param city
	 * @return Liste von PLZ
	 */
	private static List<String> getPlzByCity(String city) {
		return j.lrange(city, 0, -1);

	}

	/**
	 * Ermittelt die Stadt und den Staat die zur eingegebenen PLZ gehoeren.
	 * 
	 * @param plz
	 * @return Liste bestehend aus Stadt und Staat
	 */
	private static List<String> getCityAndStateByPlz(String plz) {
		return j.hmget(plz, "city", "state");
	}

	public static void main(String[] args) {
		boolean inside = true;
		BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
		int input_val;
		String input_str = "";
		while (inside) {
			try {
				System.out.println("Nach Stadt (0) oder PLZ (1) suchen? Oder Abbrechen (-1)? ");
				input_val = buf.read();
				// Zeilenende auslesen, da diese sonst als naechste Eingabe
				// gewertet wird
				buf.readLine();
				input_val = Character.getNumericValue(input_val);
				switch (input_val) {
				case 0:
					System.out.print("Bitte den Namen der Stadt eingeben: ");
					input_str = buf.readLine();
					System.out.println("PLZ zu " + input_str + " lautet: " + getPlzByCity(input_str));
					break;
				case 1:
					System.out.print("Bitte die PLZ der Stadt eingeben: ");
					input_str = buf.readLine();
					System.out.println("Stadt zur PLZ " + input_str + " lautet: " + getCityAndStateByPlz(input_str));
					break;
				case -1:
					inside = false;
					buf.close();
					j.close();
					System.out.println("ByeBye!");
					break;
				default:
					System.out.println("Falsche Eingabe: " + input_val);
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
