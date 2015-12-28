package constraint;

import java.io.Serializable;
import java.util.Set;

public class Diff2Constraint implements BinaryConstraint, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean operationBinary(Integer x, Integer y) {
		return (Math.abs(x - y) == 2) ? false : true;
	}

	@Override
	public Set<Integer> operationBinaryAllDiff(Set<Integer> x, Set<Integer> y) {
		return null;
	}

}
