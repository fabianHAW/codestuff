package constraints;

public class NachbarPlusMinusEinsConstraint extends BinaryConstraint{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NachbarPlusMinusEinsConstraint(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean operationBinary(Integer x, Integer y, String varName) {
		return (x + 1 == y) || (x - 1 == y);
	}

	@Override
	public boolean operationUnary(Integer x) {
		// TODO Auto-generated method stub
		return false;
	}

}
