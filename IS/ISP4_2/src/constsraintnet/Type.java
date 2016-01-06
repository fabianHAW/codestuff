package constsraintnet;

public class Type implements Cloneable{
	
	Enum<?> e;
	
	public Type(Enum<?> e){
		this.e = e;
	}
	
	public Enum<?> getElem(){
		return e;
	}

	@Override
	public String toString() {
		return "" + e;
	}
	
	@Override
	public Type clone() throws CloneNotSupportedException{
		return (Type) super.clone();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((e == null) ? 0 : e.hashCode());
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
		Type other = (Type) obj;
		if (e == null) {
			if (other.e != null)
				return false;
		} else if (!e.equals(other.e))
			return false;
		return true;
	}
	
	

}
