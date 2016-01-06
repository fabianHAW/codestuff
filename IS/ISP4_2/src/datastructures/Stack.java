package datastructures;

import java.util.ArrayList;
import java.util.HashMap;

import constsraintnet.ConstraintNet;

public class Stack {
	
	private ArrayList<ConstraintNet> stack;
	private HashMap<Integer, Integer> cvsOnStack;
	
	public Stack(){
		stack = new ArrayList<ConstraintNet>();
		cvsOnStack = new HashMap<Integer, Integer>();
	}
	
	public void push(ConstraintNet net, int cv){
		if(!cvsOnStack.containsKey(cv)){
			cvsOnStack.put(cv, 1);
		}else{
			cvsOnStack.put(cv, cvsOnStack.get(cv) + 1);
		}
		stack.add(net);
		
	}
	
	public boolean hasCvs(int cv){
		if(cvsOnStack.containsKey(cv)){
			return cvsOnStack.get(cv) > 0;
		}
		return false;
	}
	
	public ConstraintNet pop(int cv){
		if(stack.size() - 1 >=0){
			int i = cvsOnStack.get(cv);
			cvsOnStack.put(cv, i - 1);
			
			ConstraintNet n = stack.get(stack.size() - 1);
			stack.remove(stack.size() - 1);
			return n;
		}
		
		
		return null;
	}
	
	public int size(){
		return stack.size();
	}
	
	public boolean isEmpty(){
		return stack.isEmpty();
	}

}
