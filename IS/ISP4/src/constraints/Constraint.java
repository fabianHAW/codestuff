package constraints;

import java.io.Serializable;

public abstract class Constraint implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;

	public Constraint(String name){
		this.name = name;
	}
	
	public abstract boolean operation(Integer x, Integer y);

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
