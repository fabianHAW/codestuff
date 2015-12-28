package constraint;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class AllDiffConstraint implements BinaryConstraint, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean operationBinary(Integer x, Integer y) {
		return x.intValue() != y.intValue();
	}

	@Override
	public Set<Integer> operationBinaryAllDiff(Set<Integer> x, Set<Integer> y) {
		Set<Integer> res = new HashSet<Integer>();
		for (int item : x) {
			if (y.contains(item)) {
				res.add(item);
			}
		}
		return res;
	}

}
