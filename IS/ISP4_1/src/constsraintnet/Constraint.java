package constsraintnet;

public abstract class Constraint implements Cloneable {

	String name;
	Type val1;
	Type val2;
	
	public Constraint(String name, Type val1, Type val2){
		this.name = name;
		this.val1 = val1;
		this.val2 = val2;
	}
	
	public abstract boolean isSatisfied(Type val1, Type val2);
	
	public void setVals(Type v1, Type v2){
		this.val1 = v1;
		this.val2 = v2;
	}
	
	public String getName(){
		return name;
	}
	
	@Override
	public Constraint clone() throws CloneNotSupportedException{
		Constraint c = (Constraint) super.clone();
		c.setVals(val1.clone(), val2.clone());
		return c;
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((val1 == null) ? 0 : val1.hashCode());
		result = prime * result + ((val2 == null) ? 0 : val2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Constraint other = (Constraint) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (val1 == null) {
			if (other.val1 != null)
				return false;
		} else if (!val1.equals(other.val1))
			return false;
		if (val2 == null) {
			if (other.val2 != null)
				return false;
		} else if (!val2.equals(other.val2))
			return false;
		return true;
	}
	
}
