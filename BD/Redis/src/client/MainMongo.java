package src.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import redis.clients.jedis.Jedis;

public class MainMongo {

	private static MongoClient mongoClient = new MongoClient("localhost", 27017);
	private static MongoDatabase database = mongoClient.getDatabase("plz");
	private static MongoCollection<Document> collection = database.getCollection("plz");

	/**
	 * Ermittelt die PLZ (oder mehrere) die zur eingegebene Stadt gehoeren.
	 * 
	 * @param city
	 * @return Liste von PLZ
	 */
	static List<String> getPlzByCity(String city) {
		final List<String> list = new ArrayList<>();
		collection.find(new Document("city",city)).forEach(new Block<Document>() {
		    @Override
		    public void apply(final Document document) {
		    	list.add(document.getString("_id"));
		    }
		});
		return list;
	}

	/**
	 * Ermittelt die Stadt und den Staat die zur eingegebenen PLZ gehoeren.
	 * 
	 * @param plz
	 * @return Liste bestehend aus Stadt und Staat
	 */
	static List<String> getCityAndStateByPlz(String plz) {
		final List<String> list = new ArrayList<>();
		collection.find(new Document("_id",plz)).forEach(new Block<Document>() {
		    @Override
		    public void apply(final Document document) {
		    	
		    	list.add("City: " + document.getString("city") + " State: " + document.getString("state") );
		    }
		});
		return list;
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
					input_str = buf.readLine().toUpperCase();
					System.out.println("PLZ zu " + input_str + " lautet: " + getPlzByCity(input_str));
					break;
				case 1:
					System.out.print("Bitte die PLZ der Stadt eingeben: ");
					input_str = buf.readLine().toUpperCase();
					System.out.println("Stadt zur PLZ " + input_str + " lautet: " + getCityAndStateByPlz(input_str));
					break;
				case -1:
					inside = false;
					buf.close();
					mongoClient.close();
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
