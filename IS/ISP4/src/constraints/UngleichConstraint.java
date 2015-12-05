package constraints;

public class UngleichConstraint extends Constraint {

	public UngleichConstraint(String name) {
		super(name);
	}

	@Override
	public boolean operation(Integer x, Integer y) {
		return x.intValue() == y.intValue();
	}
}
