package constraints;

public class GleichConstraint extends Constraint {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GleichConstraint(String name) {
		super(name);
	}

	@Override
	public boolean operation(Integer x, Integer y) {
		return x.intValue() != y.intValue();
	}

}
