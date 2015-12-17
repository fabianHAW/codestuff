package constraint;

import java.io.Serializable;

public class NachbarMinusEinsConstraint implements Constraint, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean operationBinary(Integer x, Integer y, String varName) {
		if (varName.equals("gruen"))
			// return !(x == y - 1);
			return x <= y;
		return false;
		// return x < 1;
	}

	@Override
	public boolean operationUnary(Integer x) {
		return false;
	}

}
