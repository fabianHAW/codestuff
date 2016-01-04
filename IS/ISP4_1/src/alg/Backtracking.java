package alg;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import constsraintnet.Constraint;
import constsraintnet.ConstraintNet;
import constsraintnet.Edge;
import constsraintnet.Node;
import constsraintnet.Type;
import datastructures.ProofTree;

public class Backtracking {

	private static ProofTree<ConstraintNet> proofTree;
	private static ConstraintNet actualNode;

	public static void solve(ConstraintNet net) throws Exception {
		actualNode = net;
		System.out.println("START");
		for (Type val : actualNode.getDomain(1)) {
			proofTree = new ProofTree<ConstraintNet>();
			tmpNet = actualNode.clone();
			tmpNet.assumeNodeValue(1, val);
			proofTree.add(proofTree.getDepth(), tmpNet.clone());
			proofTree.incDepth();
			//System.out.println("count2: " + count2);
			Backtracking.tmpNet = proofTree.getNextNode();
			ConstraintNet solution = backtracking(1);
			System.out.println("#############################");
			System.out.println("#############################");
			System.out.println("#############################");
			printSolution(solution);
			System.out.println("#############################");
			System.out.println("#############################");
			System.out.println("#############################");
			
		}
	}
	static int count = 0;
	static int count2 = 0;
	static ConstraintNet tmpNet;
	
	private static ConstraintNet backtracking(int id)
			throws CloneNotSupportedException, Exception {
		int nodeID = id;

		
		while (!tmpNet.isSolved() && tmpNet != null) {
			System.out.println("NodeID: " + actualNode.getNodeName(nodeID));
			if (ac3_la(nodeID)) {
				 System.out.println(tmpNet);
				// System.out.println("ÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖ");
				if ((nodeID + 1) <= tmpNet.getNodes().size()) {
					for (Type val : tmpNet.getDomain(nodeID + 1)) {
						ConstraintNet tmp = tmpNet.clone();
						tmp.assumeNodeValue(nodeID + 1, val);
						proofTree.add(proofTree.getDepth(), tmp.clone());
						// System.out.println(tmp);
						// System.out.println("LLLLLLLLLLLLLLLLLLLLLLLLLLLL");
					}
					// System.out.println("###############################");
					// System.out.println("2 " + actualNode.equals(net));
					proofTree.incDepth();
					
					tmpNet = proofTree.getNextNode();
				//	System.out.println("Net1: " + net);
					nodeID = proofTree.getCurrentDepth();
				} else {
					
					tmpNet = proofTree.getNextNode();
					//System.out.println("Net2: " + net);
					nodeID = proofTree.getCurrentDepth();
				}
			}

			// System.out.println("#############################");
			// System.out.println("NodeID: " + nodeID);
			// System.out.println(net);
		}
		//System.out.println("SOLVED: " + tmpNet);
		System.out.println("Node: " + actualNode.getNodeName(nodeID));
		
			 System.out.println(tmpNet);
		return tmpNet;
	}

	public static void printSolution(ConstraintNet n) {
		HashMap<Integer, Type> solution = new HashMap<Integer, Type>();
		for (Node node : n.getNodes()) {
			solution.put(node.getNr(), node.getDomain().get(0));
		}
		System.out.println(solution.get(1));
		System.out.println(solution.get(2));
		System.out.println(solution.get(3));
		System.out.println(solution.get(4));
		System.out.println(solution.get(5));
		System.out.println(solution.get(6));
	}

	// procedure AC3-LA(cv)
	public static boolean ac3_la(int cv)
			throws Exception {
		HashSet<Edge> q = new HashSet<Edge>();
		HashSet<Edge> q_clone;
		boolean consistent;

		// Q <- {(Vi,Vcv) in arcs(G), i>cv);
		for (Edge e : tmpNet.getEdges()) {
			if (e.getN2().getNr() == cv && e.getN1().getNr() > cv) {
				q.add(e);
			}
			//System.out.println(e.getN2().getNr() + "== " + cv +  ", " + e.getN1().getNr() + " > " + cv);
			//System.out.println("Edge: " + e);
		}

		// consistent <- true;
		consistent = true;

		q_clone = (HashSet<Edge>) q.clone();
		Edge e;
		int id = 0;
		Iterator<Edge> iter = q.iterator();
		String tmpName = "Q is Empty";
		
		// while not Q empty & consistent
		while (iter.hasNext() && consistent) {
			count++;
			// select and delete any arc (Vk, Vm) from Q;
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

	private static boolean revise(Edge e) throws Exception {
		boolean delete = false;
		boolean delete_ti = true;
		Node vi = e.getN1();
		Node vj = e.getN2();
		Node vi_clone = vi.clone();
		String constraintFail = "";
		int constraint = 0;
		
		for (Type ti : vi.getDomain()) {
			for (Constraint c : e.getConstraints()) {
				delete_ti = true;
				for (Type tj : vj.getDomain()) {
					if (c.isSatisfied(ti, tj)) {
						//System.out.println(c.getName() + " is satisfied: " + ti.getElem() + " " + tj.getElem());
						delete_ti = false;
						constraint++;
						break;
					}else {
						constraintFail = constraintFail + "-" + c.getName();
					}
				}
			}
			if (/*delete_ti && */constraint != e.getConstraints().size()) {
				System.out.print("d: " + ti + ", ");
				vi_clone.removeElem(ti);
				delete = true;
			}
//			if(delete){
//				System.out.print("d: " + ti + ", ");
//			}
			constraint = 0;
		}
		if(delete){
			System.out.println("Revise: " + vi.getName() + " " + vj.getName() + "//" + constraintFail);
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
