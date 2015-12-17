package constraint;

import java.io.Serializable;

public class GleichConstraint implements Constraint, Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean operationBinary(Integer x, Integer y, String varName) {
		return x.intValue() == y.intValue();
	}

	@Override
	public boolean operationUnary(Integer x) {
		return false;
	}

}
