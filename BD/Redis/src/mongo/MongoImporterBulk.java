package src.mongo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;

public class MongoImporterBulk {

	public static void main(String[] args) throws IOException {
		System.out.println("Verbinde zu Redis auf localhost:6379");
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase database = mongoClient.getDatabase("plz");

		MongoCollection<Document> collection = database.getCollection("plz");
		// clear collection
		collection.deleteMany(new Document());

		BufferedReader b = new BufferedReader(new FileReader("data/plz.data"));
		List<Document> documentList = new ArrayList<>();
		long lineCounter = 0;
		String line = b.readLine();
		do {
			try {
				DBObject dbObject = (DBObject) JSON.parse(line);
				documentList.add(new Document(dbObject.toMap()));
				if (++lineCounter % 50 == 0)
					System.out.println(lineCounter + " Zeilen gelesen. ");
			} catch (Exception e) {
				System.out.println("Zeile die einen Fehler geschmissen hat: " + line);
				e.printStackTrace();
			}
			line = b.readLine();
		} while (line != null);

		System.out.println("Importiere Daten");
		collection.insertMany(documentList);
		b.close();
		mongoClient.close();
		System.out.println("Der Import ist fertig");
	}

}
