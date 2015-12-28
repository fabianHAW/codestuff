package constraint;

import java.util.Set;

public interface BinaryConstraint {

	public boolean operationBinary(Integer x, Integer y);

	public Set<Integer> operationBinaryAllDiff(Set<Integer> x, Set<Integer> y);

}
