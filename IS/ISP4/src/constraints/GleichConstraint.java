package constraints;

public class GleichConstraint extends BinaryConstraint {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GleichConstraint(String name) {
		super(name);
	}

	@Override
	public boolean operationBinary(Integer x, Integer y) {
		return x.intValue() == y.intValue();
	}

	@Override
	public boolean operationUnary(Integer x) {
		return false;
	}

}
