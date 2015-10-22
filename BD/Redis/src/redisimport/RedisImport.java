package redisimport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import redis.clients.jedis.Jedis;

public class RedisImport {

	public static void main(String[] args) throws IOException {
		Jedis j = new Jedis("localhost");
		JSONParser parser = new JSONParser();
		BufferedReader b = new BufferedReader(new FileReader("data/plz_.data"));

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
				m.put("latitude", Double.toString((Double) s.getLoc().get(0)));
				m.put("longitude", Double.toString((Double) s.getLoc().get(1)));
				m.put("pop", Long.toString(s.getPop()));
				m.put("state", s.getState());

				j.hmset(s.getId(), m);

				Map<String, String> m_ = j.hgetAll(s.getId());
				
				for(Entry<String, String> item : m_.entrySet()){
					System.out.print(item.getKey() + ": ");
					System.out.println(item.getValue());
				}

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			line = b.readLine();
		} while (line != null);

		b.close();
		j.close();
	}
}
