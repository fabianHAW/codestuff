package constraints;

public class NachbarMinusEinsConstraint extends BinaryConstraint {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NachbarMinusEinsConstraint(String name) {
		super(name);
	}

	@Override
	public boolean operationBinary(Integer x, Integer y, String varName) {
		if(varName.equals("gruen"))
//			return !(x == y - 1);
			return x >= y;
		return false;
//		return x < 1;
	}

	@Override
	public boolean operationUnary(Integer x) {
		return false;
	}

}
