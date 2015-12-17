package datastructures;

import java.util.ArrayList;

import constsraintnet.ConstraintNet;

public class ProofLevel<T> {
	
	private ArrayList<ProofNode> nodes;
	private int pointer;
	private int depth;
	
    ProofLevel(int depth){
		nodes = new ArrayList<ProofNode>();
		pointer = 0;
		this.depth = depth;
	}
	
	protected void addProofNode(ProofNode n){
		nodes.add(n);
	}
	
	protected T getNextNode(){
		if(pointer > (nodes.size() - 1)){
			return null;
		}
		T n = (T) nodes.get(pointer).getNode();
		pointer++;
		
		return n;
	}
	
	protected boolean hasNextNode(){
		return pointer <= (nodes.size() - 1);
	}
	
	

}
