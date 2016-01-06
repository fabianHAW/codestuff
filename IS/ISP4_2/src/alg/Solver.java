package alg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import constsraintnet.Constraint;
import constsraintnet.ConstraintNet;
import constsraintnet.Edge;
import constsraintnet.Node;
import constsraintnet.Type;
import datastructures.Stack;

public class Solver {
	
	private int count;
	private Stack stack;
	private ConstraintNet tmpNet;
	private ConstraintNet tmpNet2;
	private boolean consistent = true;
	
	public void start(ConstraintNet net) throws Exception{
		stack = new Stack();
		backtracking(net, 1);
		count = 0;
		
	}
	
	private void backtracking(ConstraintNet net, int cv) throws Exception{
		
		tmpNet = net.clone();
		for(int i = cv; i < net.getNodes().size();){
			System.out.print("i: " + i + " ");
			if(consistent){
				ArrayList<Type> domain = tmpNet.getDomain(i);
				for(Type t : domain){
					ConstraintNet n = tmpNet.clone();
					n.assumeNodeValue(i, t);
					stack.push(n.clone(), i);
				}
			}
			if(stack.hasCvs(i)){
				tmpNet = stack.pop(i).clone();
				consistent = ac3_la(i);
			}else{
				i--;
				while(!stack.hasCvs(i) && i >= 1){
					i--;
				}
				tmpNet = stack.pop(i);
				if(tmpNet == null){
					System.out.println("######################### KEINE LÖSUNG ######################");
					break;
				}
				consistent = ac3_la(i);
			}
			
			if(consistent){
				i++;
			}
			if(tmpNet.getDomain(1).size() > 1){
				System.out.println("##################### Größer als 1: " + i);
			}
			System.out.println (i);
		}
		
		if(tmpNet != null){
			printSolution();
		}
	
	}
	
	public void printSolution(){
		ConstraintNet n = tmpNet;
		HashMap<Integer, Type> solution = new HashMap<Integer, Type>();
		HashMap<Integer, String> solution2 = new HashMap<Integer, String>();
		for (Node node : n.getNodes()) {
			if(node.getDomain().size() == 1){
			solution.put(node.getNr(), node.getDomain().get(0));
			solution2.put(node.getNr(), node.getName());
			}
		}
		for(Integer i : solution.keySet()){
			System.out.println(solution2.get(i) + " " + solution.get(i));
		}
	}
	
	public boolean ac3_la(int cv)//ok
			throws Exception {
		HashSet<Edge> q = new HashSet<Edge>();
		HashSet<Edge> q_clone;
		boolean consistent;

		// Q <- {(Vi,Vcv) in arcs(G), i>cv); //ok
		for (Edge e : tmpNet.getEdges()) {
			if (e.getN2().getNr() == cv && e.getN1().getNr() > cv) {
				q.add(e);
			}
			//System.out.println(e.getN2().getNr() + "== " + cv +  ", " + e.getN1().getNr() + " > " + cv);
			//System.out.println("Edge: " + e);
		}

		// consistent <- true;//ok
		consistent = true;

		q_clone = (HashSet<Edge>) q.clone();
		Edge e;
		int id = 0;
		Iterator<Edge> iter = q.iterator();
		String tmpName = "Q is Empty";
		
		// while not Q empty & consistent
		while (iter.hasNext() && consistent) {
			count++;
			// select and delete any arc (Vk, Vm) from Q; //ok
			e = iter.next();
			tmpName = e.getN1().getName();
			if (id == 0) {
				q_clone.remove(e);
			} else if (id == 1) {
				q.remove(e);
			}
			
			// if REVISE(Vk, Vm) then
			if (revise(e)) {
				// Q <- Q union {(Vi, Vk) such that (Vi, Vk) in arcs(G), i#k,
				// i#m, i>cv)
//				System.out.println(tmpNet);
//				System.out.println("#############################");
				for (Edge edge : tmpNet.getEdges()) {
					if (edge.getN2().getName().equals(e.getN1().getName())
							&& edge.getN1().getNr() != e.getN1().getNr()
							&& edge.getN1().getNr() != e.getN2().getNr()
							&& edge.getN1().getNr() > cv) {
						if (id == 0) {
							q_clone.add(edge);
						} else if (id == 1) {
							q.add(edge);
						}
					}
				}
				
				consistent = (!e.getN1().getDomain().isEmpty());
					
				if (id == 0) {
					iter = q_clone.iterator();
					id = 1;
					q = (HashSet<Edge>) q_clone.clone();
				} else if (id == 1) {
					iter = q.iterator();
					id = 0;
					q_clone = (HashSet<Edge>) q.clone();
				}

			}
			//System.out.println(count);
		}
		//System.out.println("DomainName: " + tmpName + " id: " + (cv));
//		System.out.println("consistent: " + consistent);
//		System.out.println(actualNode);
//		System.out.println("##############################################");
		return consistent;
	}

	private boolean revise(Edge e) throws Exception {
		boolean delete = false; //DELETE <- false //ok
		boolean delete_ti = true;
		Node vi = e.getN1();
		Node vj = e.getN2();
		Node vi_clone = vi.clone();
		String constraintFail = "constraintFail: ";
		int constraint = 0;
		int log_types_ti = 0;
		int log_types_tj = 0;
		System.out.println("Vi: " + vi.getName());
		String constraintClass = "";
		for (Type ti : vi.getDomain()) { //for each X in Di do //ok
			log_types_ti++;
			for (Constraint c : e.getConstraints()) {
				delete_ti = true;
				for (Type tj : vj.getDomain()) {//if there is no such Y in Dj such that (X,Y) is consistent,
					log_types_tj++;
					if (c.isSatisfied(ti, tj)) {
						//System.out.println(c.getName() + " is satisfied: " + ti.getElem() + " " + tj.getElem());
						delete_ti = false;
						constraint++;
						break;
					}else {
						constraintClass = c.getName();
						constraintFail = constraintFail + "-" + c.getName();
					}
				}
			}
			if (delete_ti /*&& constraint != e.getConstraints().size()*/) {
				//System.out.println("constraint: " + constraint + "e.getConstraint.size: " + e.getConstraints().size());
				System.out.println("DEL(" + vi.getName() + ", " + ti + ") cause " + constraintClass + " ON " + "NODE(" + vj.getName() + ", " + vj.getDomain() +  ")" + ", ");
				vi_clone.removeElem(ti);
				delete = true;
			}
//			if(delete){
//				System.out.print("d: " + ti + ", ");
//			}
			constraint = 0;
		}
		if(delete){
			//System.out.println("Revise: " + vi.getName() + " " + vj.getName() + "//" + constraintFail);
			//System.out.println("");
		}
		tmpNet.removeNode(vi);
		tmpNet.insertNode(vi_clone);
//		if(delete){
//			System.out.println("VORHER: " + vi);
//			System.out.println("NACHHER: " + vi_clone);
//		}
		return delete;
	}

}
