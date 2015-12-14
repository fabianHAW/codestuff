package constraints;

import java.io.Serializable;

public abstract class BinaryConstraint implements Serializable, Constraint {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;

	public BinaryConstraint(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
