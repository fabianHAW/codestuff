package constraints;

import java.io.Serializable;

public abstract class UnaryConstraint implements Serializable, Constraint{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;

	public UnaryConstraint(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
