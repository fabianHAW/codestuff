package constraints;


public abstract class Constraint {
	
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
