package constraints;

public class AllDiffConstraint extends BinaryConstraint {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AllDiffConstraint(String name) {
		super(name);
	}

	@Override
	public boolean operationBinary(Integer x, Integer y, String varName) {
		return x.intValue() != y.intValue();
	}

	@Override
	public boolean operationUnary(Integer x) {
		return false;
	}

}
