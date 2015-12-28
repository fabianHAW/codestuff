package datastructs;

import java.util.List;

import org.jgraph.graph.DefaultEdge;

import constraint.BinaryConstraint;

public class Edge<V> extends DefaultEdge{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private V v1;
    private V v2;
    private List<BinaryConstraint> constraintList;

    public Edge(V v1, V v2, List<BinaryConstraint> list) {
        this.v1 = v1;
        this.v2 = v2;
        this.constraintList = list;
    }

    public V getV1() {
        return v1;
    }

    public V getV2() {
        return v2;
    }

	public List<BinaryConstraint> getConstraintList() {
		return constraintList;
	}

	public void setConstraintList(List<BinaryConstraint> list) {
		this.constraintList = list;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((constraintList == null) ? 0 : constraintList.hashCode());
		result = prime * result + ((v1 == null) ? 0 : v1.hashCode());
		result = prime * result + ((v2 == null) ? 0 : v2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		Edge<Vertex> other = (Edge<Vertex>) obj;
		if (constraintList == null) {
			if (other.constraintList != null)
				return false;
		} else if (!constraintList.equals(other.constraintList))
			return false;
		if (v1 == null) {
			if (other.v1 != null)
				return false;
		} else if (!v1.equals(other.v1))
			return false;
		if (v2 == null) {
			if (other.v2 != null)
				return false;
		} else if (!v2.equals(other.v2))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "v1=" + v1.toString() + ", v2=" + v2.toString() + "\n";
	}
	
	

}
