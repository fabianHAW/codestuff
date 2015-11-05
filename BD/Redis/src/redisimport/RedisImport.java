package src.redisimport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import redis.clients.jedis.Jedis;

public class RedisImport {

	public static void main(String[] args) throws IOException {
		System.out.println("Verbinde zu Redis auf localhost:6379");
		Jedis j = new Jedis("localhost", 6379);
		JSONParser parser = new JSONParser();
		BufferedReader b = new BufferedReader(new FileReader("data/plz.data"));
		long lineCounter = 0;
		String line = b.readLine();
		do {
			try {
				JSONObject o = (JSONObject) parser.parse(line);
				PLZStructure s = new PLZStructure();
				s.setId((String) o.get("_id"));
				s.setCity((String) o.get("city"));
				s.setLoc((JSONArray) o.get("loc"));
				s.setPop((Long) o.get("pop"));
				s.setState((String) o.get("state"));

				Map<String, String> m = new HashMap<String, String>();
				m.put("city", s.getCity());
				m.put("latitude", s.getLoc().get(0).toString());
				m.put("longitude", s.getLoc().get(1).toString());
				m.put("pop", Long.toString(s.getPop()));
				m.put("state", s.getState());

				j.hmset(s.getId(), m);

				j.rpush(s.getCity(), s.getId());
				
				if(++lineCounter%50==0)
					System.out.println(lineCounter + " Zeilen hinzugefügt. ");
			} catch (Exception e) {
				System.out.println("Zeile die einen Fehler geschmissen hat: " + line);
				e.printStackTrace();
			}
			line = b.readLine();
		} while (line != null);

		b.close();
		j.close();
		System.out.println("Der Import ist fertig");
	}
}
