package constraints;

public class UngleichConstraint extends Constraint {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UngleichConstraint(String name) {
		super(name);
	}

	@Override
	public boolean operation(Integer x, Integer y) {
		return x.intValue() == y.intValue();
	}
}
