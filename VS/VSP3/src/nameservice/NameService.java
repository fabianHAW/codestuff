package nameservice;

public class NameService extends mware_lib.NameService {

	private static Integer port;
	
	public static void main(String[] args) {

		if (args.length != 1) {
			usage();
			System.exit(-1);
		}
		port = Integer.valueOf(args[0]);

	}

	// - Schnittstelle zum Namensdienst -

	// Meldet ein Objekt (servant) beim Namensdienst an.
	// Eine eventuell schon vorhandene Objektreferenz gleichen Namens
	// soll überschrieben werden.
	@Override
	public void rebind(Object servant, String name) {
		// TODO Auto-generated method stub

	}

	// Liefert eine generische Objektreferenz zu einem Namen. (vgl. unten)
	@Override
	public Object resolve(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private static void usage(){
		System.out.println("***forgot portnumber! try this***\n"
				+ "java nameservice/NameServiceImpl portnumber");
	}

}
