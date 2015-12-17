package constsraintnet;

import java.util.ArrayList;

public class Edge implements Cloneable{
	
	private Node n1;
	private Node n2;
	private ArrayList<Constraint> constraints;
	
	public Edge(Node n1, Node n2, ArrayList<Constraint> constraints){
		this.n1 = n1;
		this.n2 = n2;
		this.constraints = constraints;
	}

	private void setConstraints(ArrayList<Constraint> c){
		this.constraints = c;
	}
	
	public void setNodes(Node n1, Node n2){
		this.n1 = n1;
		this.n2 = n2;
	}
	
	public void setN1(Node n1){
		this.n1 = n1;
	}
	
	public void setN2(Node n2){
		this.n2 = n2;
	}
	
	public ArrayList<Constraint> getConstraints(){
		return constraints;
	}
	
	public Node getN1(){
		return n1;
	}
	
	public Node getN2(){
		return n2;
	}
	
	public void delN1(){
		n1 = null;
	}
	
	public void delN2(){
		n2 = null;
	}

	@Override
	public String toString() {
		return "{" + n1 + ", " + n2 + ", constraints=" + constraints + "}"
				 + System.getProperty("line.separator");
	}
	
	@Override
	public Edge clone() throws CloneNotSupportedException{
		Edge e = (Edge) super.clone();
		ArrayList<Constraint> cons = new ArrayList<Constraint>();
		for(Constraint c : constraints){
			cons.add(c.clone());
		}
		e.setConstraints(cons);
		e.setNodes(n1.clone(), n2.clone());
		return e;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((constraints == null) ? 0 : constraints.hashCode());
		result = prime * result + ((n1 == null) ? 0 : n1.hashCode());
		result = prime * result + ((n2 == null) ? 0 : n2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		System.out.println("edges equals");
		if (this == obj)
			return true;
		if (obj == null){
			System.out.println("edges a");
			return false;
		}
		if (getClass() != obj.getClass()){
			System.out.println("edges b");
			return false;
		}
		Edge other = (Edge) obj;
		if (constraints == null) {
			if (other.constraints != null){
				System.out.println("edges c");
				return false;
			}
		} else if (!constraints.equals(other.constraints)){
			System.out.println("edges d");
			return false;
		}
		if (n1 == null) {
			if (other.n1 != null){
				System.out.println("edges e");
				return false;
			}
		} else if (!n1.equals(other.n1)){
			System.out.println("edges f");
			return false;
		}
		if (n2 == null) {
			if (other.n2 != null){
				System.out.println("edges g");
				return false;
			}
		} else if (!n2.equals(other.n2)){
			System.out.println("edges h");
			return false;
		}
		return true;
	}

}
