package constsraintnet;

public class EqualConstraint extends Constraint {

	public EqualConstraint(String name, Type val1, Type val2) {
		super(name, val1, val2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isSatisfied(Type val1, Type val2) {
		// TODO Auto-generated method stub
		if(val1.getElem() == val2.getElem()){
			return true;
		}
		return false;
	}

}
