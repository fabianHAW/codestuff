package datastructs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import constraint.UnaryConstraint;

public class Vertex implements Comparable<Vertex>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private Set<Integer> domain;
	private List<UnaryConstraint> unaryConstraintList;

	public Vertex(int id, String name, Set<Integer> domain) {
		this.id = id;
		this.name = name;
		this.domain = domain;
		unaryConstraintList = new ArrayList<UnaryConstraint>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Integer> getDomain() {
		return domain;
	}

	public void setDomain(Set<Integer> domain) {
		this.domain = domain;
	}

	public List<UnaryConstraint> getUnaryConstraintList() {
		return unaryConstraintList;
	}

	public void setUnaryConstraintList(List<UnaryConstraint> unaryConstraintList) {
		this.unaryConstraintList = unaryConstraintList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		// result = prime * result + ((domain == null) ? 0 : domain.hashCode());
		result = prime * result + id;
		// result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		// if (getClass() != obj.getClass())
		// return false;
		Vertex other = (Vertex) obj;
		// if (domain == null) {
		// if (other.domain != null)
		// return false;
		// } else if (!domain.equals(other.domain))
		// return false;
		if (id != other.id)
			return false;
		// if (name == null) {
		// if (other.name != null)
		// return false;
		// } else if (!name.equals(other.name))
		// return false;
		return true;
	}

	@Override
	public String toString() {
		return "id=" + id + ", name=" + name + ", domain=" + domain;
	}

	@Override
	public int compareTo(Vertex o) {
		if (this.getId() < o.getId())
			return -1;
		else if (this.getId() > o.getId())
			return 1;
		return 0;
	}

}
