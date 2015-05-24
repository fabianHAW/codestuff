package nameservice;

public class Main {

	public Main() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length != 1) {
			usage();
			System.exit(-1);
		}
		NameService n = new NameService();
		n.start(Integer.valueOf(args[0]));
	}
	
	private static void usage(){
		System.out.println("***forgot portnumber! try this***\n"
				+ "java nameservice/NameServiceImpl portnumber");
	}

}
