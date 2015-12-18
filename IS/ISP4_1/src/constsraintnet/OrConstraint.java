package constsraintnet;

public class OrConstraint extends Constraint {

	Type val3;
	
	public OrConstraint(String name, Type val1, Type val2, Type val3) {
		super(name, val1, val2);
		// TODO Auto-generated constructor stub
		this.val3 = val3;
	}

	@Override
	public boolean isSatisfied(Type val1, Type val2) {
		// TODO Auto-generated method stub
		if(this.val1.getElem() == val1.getElem() && this.val2.getElem() == val2.getElem()
				|| (this.val1.getElem() == val1.getElem() && this.val2.getElem() == val3.getElem())
				|| (this.val1.getElem() == val2.getElem() && this.val2.getElem() == val1.getElem())
				|| (this.val1.getElem() == val2.getElem() && this.val3.getElem() == val1.getElem())){
			return true;
		}
		if(val1.getElem() != this.val1.getElem() && val2.getElem() != this.val1.getElem()){
			return true;
		}
		return false;
	}

}
