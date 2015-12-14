package constraints;

public class Diff3Constraint extends BinaryConstraint {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Diff3Constraint(String name) {
		super(name);
	}

	@Override
	public boolean operationBinary(Integer x, Integer y) {
		return (Math.abs(x - y) == 3) ? false : true;
	}

	@Override
	public boolean operationUnary(Integer x) {
		return false;
	}
}
