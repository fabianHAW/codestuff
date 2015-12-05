package constraints;

public class Diff1Constraint extends Constraint {

	public Diff1Constraint(String name) {
		super(name);
	}

	@Override
	public boolean operation(Integer x, Integer y) {
		return (Math.abs(x - y) == 1) ? true : false;
	}
}
