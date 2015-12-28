
package constraints;

public class UngleichConstraint extends BinaryConstraint {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UngleichConstraint(String name) {
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
