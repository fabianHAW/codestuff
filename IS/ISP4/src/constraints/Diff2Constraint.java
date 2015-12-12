package constraints;

public class Diff2Constraint extends Constraint {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Diff2Constraint(String name) {
		super(name);
	}

	@Override
	public boolean operation(Integer x, Integer y) {
		return (Math.abs(x - y) == 2) ? true : false;
	}
}
