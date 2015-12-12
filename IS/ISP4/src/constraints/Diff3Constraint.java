package constraints;

public class Diff3Constraint extends Constraint {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Diff3Constraint(String name) {
		super(name);
	}

	@Override
	public boolean operation(Integer x, Integer y) {
		return (Math.abs(x - y) == 3) ? true : false;
	}
}
