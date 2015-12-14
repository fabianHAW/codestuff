package constraints;

public class Diff2Constraint extends BinaryConstraint {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Diff2Constraint(String name) {
		super(name);
	}

	@Override
	public boolean operationBinary(Integer x, Integer y) {
		return (Math.abs(x - y) == 2) ? false : true;
	}

	@Override
	public boolean operationUnary(Integer x) {
		return false;
	}
}
