package constraints;

public interface Constraint {
	
	public boolean operationBinary(Integer x, Integer y, String varName);
	public boolean operationUnary(Integer x);

}
