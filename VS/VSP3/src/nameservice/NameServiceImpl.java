package nameservice;

public class NameServiceImpl extends NameService{

	
	private static Integer port;
	
	public static void main(String[] args){
		
		if(args.length != 1){
			usage();
			System.exit(-1);
		}
		port = Integer.valueOf(args[0]);
		
	}
	
	public void rebind(Object servant, String name) {
		// TODO Auto-generated method stub
		
	}

	public Object resolve(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private static void usage(){
		System.out.println("***forgot portnumber! try this***\n"
				+ "java nameservice/NameServiceImpl portnumber");
	}

}
