package src.client;

import java.util.List;

import redis.clients.jedis.Jedis;

public class Main {
	
	Jedis j = new Jedis("localhost", 6379);
	
	public Main() {
		System.out.println(getCityByPlz("07419"));
		System.out.println(getPlzByCity("HAMBURG"));
	}
	
	
	private List<String> getCityByPlz(String plz){		
		return 	j.hmget(plz, "city","state");
	}
	
	private List<String> getPlzByCity(String city) {
		return j.lrange(city, 0, -1);
		
	}
	
	public static void main(String[] args) {
		new Main();
	}
}
