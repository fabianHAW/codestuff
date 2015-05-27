package nameservice;

/**
 * Startert den Nameservice.
 * @author Francis u. Fabian.
 *
 */
public class NameserviceMain {

	public NameserviceMain() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Erwartet beim Start einen als einzigen Parameter den Port auf dem der Nameservice lauschen kann.
	 * @param args Enthält genau einen Parameter, den Port auf dem der Nameservice lauschen kann.
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length != 1) {
			usage();
			System.exit(-1);
		}
		NameService n = new NameService();
		n.start(Integer.valueOf(args[0]));
	}
	
	/**
	 * Wenn kein Parameter übergeben wurde, wird diese Nachricht ausgegeben.
	 */
	private static void usage(){
		System.out.println("***No portnumber as argument! Try this***\n"
				+ "java nameservice/NameServiceImpl portnumber");
	}

}
