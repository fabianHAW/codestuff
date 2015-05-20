package accessor_two;

public class ClassOneAT extends ClassOneImplBase {

	public ClassOneAT() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public double methodOne(String param1, double param2)
			throws SomeException112 {
		if (param2 < 2) {
			throw new SomeException112("param2 is less than 2");
		}
		return 2.2;
	}

	@Override
	public double methodTwo(String param1, double param2)
			throws SomeException112, SomeException304 {
		if (param1.equals("beer")) {
			throw new SomeException304("param1 is to much");
		} else if (param2 > 2) {
			throw new SomeException112("param2 is greater than 2");
		}
		return 3.3;
	}

}
