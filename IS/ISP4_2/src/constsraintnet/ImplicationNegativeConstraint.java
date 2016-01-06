package constsraintnet;

import util.NameValMapping;

public class ImplicationNegativeConstraint extends Constraint{

	public ImplicationNegativeConstraint(String name, Type val1, Type val2) {
		super(name, val1, val2);
		// TODO Auto-generated constructor stub
	}
	
	public ImplicationNegativeConstraint(String name, String val1, String val2) {
		super(name, val1, val2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isSatisfied(Type val1, Type val2) {
		if(this.val1.getElem() == val1.getElem() && this.val2.getElem() != val2.getElem()
				|| (this.val1.getElem() == val2.getElem() && this.val2.getElem() != val1.getElem())){
			return true;
		}
		if(this.val1.getElem() != val1.getElem() && this.val1.getElem() != val2.getElem()){
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isSatisfied(String v1, String v2) {
		// TODO Auto-generated method stub
		Type val1 = new Type(NameValMapping.map(v1));
		Type val2 = new Type(NameValMapping.map(v1));
		if(this.val1.getElem() == val1.getElem() && this.val2.getElem() != val2.getElem()
				|| (this.val1.getElem() == val2.getElem() && this.val2.getElem() != val1.getElem())){
			return true;
		}
		if(this.val1.getElem() != val1.getElem() && this.val1.getElem() != val2.getElem()){
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
