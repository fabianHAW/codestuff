package constraints;

public class KleinerConstraint extends BinaryConstraint {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public KleinerConstraint(String name) {
		super(name);
	}

	@Override
	public boolean operationBinary(Integer x, Integer y) {
		return x < y;
	}

	@Override
	public boolean operationUnary(Integer x) {
		return false;
	}

}
