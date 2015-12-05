package constraints;

public class Diff3Constraint extends Constraint {

	public Diff3Constraint(String name) {
		super(name);
	}

	@Override
	public boolean operation(Integer x, Integer y) {
		return (Math.abs(x - y) == 3) ? true : false;
	}
}
