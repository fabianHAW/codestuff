package accessor_one;

public class ClassOneAO extends ClassOneImplBase {

	public ClassOneAO() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String methodOne(String param1, int param2) throws SomeException112 {
		// TODO Auto-generated method stub
		if(param2 % 2 == 0){
			return param1 + " " + param2 + " % 2 == 0"; 
		}else{
			throw new SomeException112("ClassOneAO-methodOne-SomeException112:::(" + param2 + " % 2 != 0)");
		}
	}

}
