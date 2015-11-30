package src.client;

public class MainBench {

	private static int COUNTER = 1000;
	private static String PLZ = "01001";
	private static String CITY = "HAMBURG";
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		for (int i = 0; i < COUNTER; i++) {
			MainMongo.getCityAndStateByPlz(PLZ);
			MainMongo.getPlzByCity(CITY);
		}
		System.out.println("Zeit MongoDB: " + (System.currentTimeMillis() - start));
		
		start = System.currentTimeMillis();
		for (int i = 0; i < COUNTER; i++) {
			MainRedis.getCityAndStateByPlz(PLZ);
			MainRedis.getPlzByCity(CITY);
		}
		System.out.println("Zeit RedisDB: " + (System.currentTimeMillis() - start));
	}

}
