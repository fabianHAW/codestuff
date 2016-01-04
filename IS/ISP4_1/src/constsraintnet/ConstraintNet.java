package constsraintnet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ConstraintNet implements Cloneable{
	
	HashSet<Node> nodes;
	HashSet<Edge> edges;
	
	public ConstraintNet(){
		nodes = new HashSet<Node>();
		edges = new HashSet<Edge>();
	}
	
	public void setConstraint(Node n1, Node n2, ArrayList<Constraint> constraints){
		nodes.add(n1);
		nodes.add(n2);
		edges.add(new Edge(n1,n2,constraints));
	}

	@Override
	public String toString() {
		return "ConstraintNet [nodes=" + nodes /*+  System.getProperty("line.separator") +", edges=" + edges */+ "]";
	}

	public Set<Node> getNodes() {
		return nodes;
	}

	public void setNodes(HashSet<Node> nodes) {
		this.nodes = nodes;
	}

	public HashSet<Edge> getEdges() {
		return edges;
	}

	public void setEdges(HashSet<Edge> edges) {
		this.edges = edges;
	}
	
	public void removeNode(Node n){
		for(Edge e : edges){
			if(e.getN1().getName().equals(n.getName())){
				e.delN1();
			}else if(e.getN2().getName().equals(n.getName())){
				e.delN2();
			}
		}
		
		nodes.remove(n);
	}
	
	public void insertNode(Node n) throws Exception{
		for(Edge e : edges){
			if(e.getN1() == null && e.getN2() == null){
				throw new Exception(ConstraintNet.class + ": insertNode(Node n) - Mindestens eine Kante, deren beider Knoten null sind. Welcher von beiden ist n?");
			}
			
			if(e.getN1() == null){
				e.setN1(n);
			}else if(e.getN2() == null){
				e.setN2(n);
			}
		}
		
		nodes.add(n);
	}
	
	public void assumeNodeValue(int nodeID, Type val) throws Exception{
		Node n = null;
		ArrayList<Type> domain_clone;
		for(Node node : nodes){
			if(node.getNr() == nodeID){
				n = node;
				break;
			}
		}
		
		if(n == null){
			throw new Exception(ConstraintNet.class + ": assumeNodeValue Kein Knoten mit id " + nodeID);
		}
		
		removeNode(n);
		domain_clone = (ArrayList<Type>) n.getDomain().clone();
		for(Type t : n.getDomain()){
			if(!t.equals(val)){
				domain_clone.remove(t);
			}
		}
		n.setDomain(domain_clone);
		insertNode(n);
	}
	
	public ArrayList<Type> getDomain(int nodeID) throws Exception{
		for(Node n : nodes){
			if(n.getNr() == nodeID){
				return n.getDomain();
			}
		}
		throw new Exception(ConstraintNet.class + ": getDomain Kein Knoten mit ID" + nodeID);
	}
	
	public String getNodeName(int id){
		for(Node n : nodes){
			if(n.getNr() == id){
				return n.getName();
			}
		}
		return ConstraintNet.class + ": Fail in getNodeName";
	}
	
	public boolean isSolved(){
		int c = 0;
		for(Node n : nodes){
			if(n.getDomain().size() == 1){
				c++;
			}
		}
		return c == nodes.size();
	}
	
	@Override
	public ConstraintNet clone() throws CloneNotSupportedException {
		ConstraintNet n = (ConstraintNet) super.clone();
		HashSet<Node> n1 = new HashSet<Node>();
		HashSet<Edge> e1 = new HashSet<Edge>();
		for(Node node : nodes){
			n1.add(node.clone());
		}
		for(Edge edge : edges){
			e1.add(edge.clone());
		}
		n.setNodes(n1);
		n.setEdges(e1);
		return n;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((edges == null) ? 0 : edges.hashCode());
		result = prime * result + ((nodes == null) ? 0 : nodes.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null){
			System.out.println("a");
			return false;}
		if (getClass() != obj.getClass()){
		System.out.println("b");
			return false;
		}
		ConstraintNet other = (ConstraintNet) obj;
		if (edges == null) {
			if (other.edges != null){
			System.out.println("c");
				return false;
			}
		} else if (!edges.equals(other.edges)){
			System.out.println("d");
			return false;
		}
		if (nodes == null) {
			if (other.nodes != null){
				System.out.println("e");
				return false;
			}
		} else if (!nodes.equals(other.nodes)){
			System.out.println("f");
			return false;
		}
		return true;
	}
	
	

}
