package constraint;

import java.util.Set;

public interface BinaryConstraint {

	public Set<Integer> operationBinary(Set<Integer> x, Set<Integer> y, String name);

}
