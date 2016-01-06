package constsraintnet;

import util.NameValMapping;

@Deprecated
public class OrConstraint extends Constraint {

	Type val3;
	
	public OrConstraint(String name, Type val1, Type val2, Type val3) {
		super(name, val1, val2);
		// TODO Auto-generated constructor stub
		this.val3 = val3;
	}
	
	public OrConstraint(String name, String val1, String val2, Type val3) {
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
	
	@Override
	public boolean isSatisfied(String v1, String v2) {
		// TODO Auto-generated method stub
		Type val1 = new Type(NameValMapping.map(v1));
		Type val2 = new Type(NameValMapping.map(v1));
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
