package constraints;

public class AllDiffConstraint extends Constraint {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AllDiffConstraint(String name) {
		super(name);
	}

	@Override
	public boolean operation(Integer x, Integer y) {
		return x.intValue() == y.intValue();
	}

}
