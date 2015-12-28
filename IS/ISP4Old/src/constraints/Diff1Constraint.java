package constraints;

public class Diff1Constraint extends BinaryConstraint {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Diff1Constraint(String name) {
		super(name);
	}

	@Override
	public boolean operationBinary(Integer x, Integer y, String varName) {
		return (Math.abs(x - y) == 1) ? false : true;
	}

	@Override
	public boolean operationUnary(Integer x) {
		return false;
	}
}
