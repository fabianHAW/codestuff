package constraints;

public class Diff2Constraint extends Constraint {

	public Diff2Constraint(String name) {
		super(name);
	}

	@Override
	public boolean operation(Integer x, Integer y) {
		return (Math.abs(x - y) == 2) ? true : false;
	}
}
