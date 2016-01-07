package constraint;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class EinsteinUnaryConstraint implements UnaryConstraint, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Set<Integer> operationUnary(int id) {
		Set<Integer> domain = new HashSet<Integer>();
		if(id == 7)
			domain.add(1);
		if(id == 18)
			domain.add(3);
		
		return domain;
	}

}
