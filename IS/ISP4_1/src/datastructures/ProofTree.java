package datastructures;

import java.util.HashMap;


public class ProofTree<T> {
	
	private HashMap<Integer, ProofLevel<T>> proofLevels;
	
	private int depth;
	private int currentDepth;
	
	public ProofTree(){
		proofLevels = new HashMap<Integer, ProofLevel<T>>();
		depth = 0;
		currentDepth = 1;
	}
	
	public void add(int depth, T n) throws Exception{
		if(depth != this.depth){
			throw new Exception(ProofTree.class + ": Tiefe von " + depth + " nicht vorhanden.");
		}
		ProofNode<T> node = new ProofNode<T>(n);
		if(!proofLevels.containsKey(depth)){
			proofLevels.put(depth, new ProofLevel<T>(depth));
		}
		proofLevels.get(depth).addProofNode(node);
	}
	
	//Tiefensuche
	public T getNextNode() throws Exception{
		int idx = currentDepth;
		
		if(proofLevels.containsKey(currentDepth) && proofLevels.get(currentDepth).hasNextNode()){
			idx = currentDepth;
			currentDepth++;
		}else if(!proofLevels.containsKey(currentDepth) || !proofLevels.get(currentDepth).hasNextNode()){
			currentDepth--;
			while(currentDepth >= 1 && !proofLevels.get(currentDepth).hasNextNode()){
				currentDepth--;
			}
			if(!proofLevels.get(currentDepth).hasNextNode()){
				throw new Exception(ProofTree.class + ": Der ProofTree hat keine Blätter mehr :-(");
			}
			idx = currentDepth;
		}
		
		T n = proofLevels.get(idx).getNextNode();
		
		return n;
	}
	
	public int getCurrentDepth(){
		return currentDepth;
	}
	
// Breitensuche
//	public T getNextNode() throws Exception{
//		if(!proofLevels.get(currentDepth).hasNextNode()
//				&& proofLevels.containsKey(currentDepth + 1)){
//			currentDepth++;
//		}else if(!proofLevels.get(currentDepth).hasNextNode()){
//			while(currentDepth >= 1 && !proofLevels.get(currentDepth).hasNextNode()){
//				currentDepth--;
//			}
//		}
//		
//		if(!proofLevels.get(currentDepth).hasNextNode()){
//			throw new Exception(ProofTree.class + ": Der ProofTree hat keine Blätter mehr :-(");
//		}
//		
//		T n = proofLevels.get(currentDepth).getNextNode();
//		
//		return n;
//	}
	
	
	public int getDepth(){
		return depth;
	}
	
	public void incDepth(){
		depth++;
	}
	
	

	
	
	

}
