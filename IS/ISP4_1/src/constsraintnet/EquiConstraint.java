package constsraintnet;

public class EquiConstraint extends Constraint{

	
	
	public EquiConstraint(String name, Type val1, Type val2) {
		super(name, val1, val2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isSatisfied(Type val1, Type val2) {
		if(this.val1.getElem() == val1.getElem() && this.val2.getElem() == val2.getElem()
				|| (this.val1.getElem() == val2.getElem() && this.val2.getElem() == val1.getElem())){
				//System.out.print("-#");
			return true;
		}
		if(val1.getElem() != this.val1.getElem() && val1.getElem() != this.val2.getElem()
				&& val2.getElem() != this.val1.getElem() && val2.getElem() != this.val2.getElem()){
			//System.out.print("-+ val1: " + val1.getElem() + " this.val1: " + this.val1.getElem() + ", val2: " + val2.getElem() + ", this.val2: " + this.val2.getElem());
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "EquiConstraint [name=" + name + ", val1=" + val1 + ", val2="
				+ val2;
	}

}
