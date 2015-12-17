package constsraintnet;

public class ImplicationNegativeConstraint extends Constraint{

	public ImplicationNegativeConstraint(String name, Type val1, Type val2) {
		super(name, val1, val2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isSatisfied(Type val1, Type val2) {
		if(this.val1.getElem() == val1.getElem() && this.val2.getElem() != val2.getElem()){
			return true;
		}
		if(this.val1.getElem() != val1.getElem()){
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "ImplicationNegativeConstraint [name=" + name + ", val1=" + val1
				+ ", val2=" + val2 + "]";
	}

	
	
}
