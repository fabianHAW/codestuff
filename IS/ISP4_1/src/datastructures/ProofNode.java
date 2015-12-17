package datastructures;

import java.util.ArrayList;

public class ProofNode<T> {
	
	private T net;
	private ArrayList<ProofNode> childs;
	
    ProofNode(T n){
		this.net = n;
		childs = new ArrayList<ProofNode>();
	}
	
	public void setChild(ProofNode n){
		childs.add(n);
	}
	
	public ProofNode getChildProofNode(int i){
		return childs.get(i);
	}
	
	public T getNode(){
		return net;
	}
	
	

}
