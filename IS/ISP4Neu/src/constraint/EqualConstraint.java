package constraint;

import java.io.Serializable;
import java.util.Set;

public class EqualConstraint implements BinaryConstraint, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean operationBinary(Integer x, Integer y) {
		return x.intValue() == y.intValue();
	}

	@Override
	public Set<Integer> operationBinaryAllDiff(Set<Integer> x, Set<Integer> y) {
		return null;
	}

}
