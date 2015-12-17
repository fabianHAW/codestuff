package constraint;

import java.io.Serializable;

public class NachbarPlusMinusEinsConstraint implements Constraint, Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean operationBinary(Integer x, Integer y, String varName) {
		return (x + 1 == y) || (x - 1 == y);
	}

	@Override
	public boolean operationUnary(Integer x) {
		// TODO Auto-generated method stub
		return false;
	}

}
