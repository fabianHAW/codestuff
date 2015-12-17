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

		for (Type val : actualNode.getDomain(1)) {
			proofTree = new ProofTree<ConstraintNet>();
			ConstraintNet tmpNet = actualNode.clone();
			tmpNet.assumeNodeValue(1, val);
			proofTree.add(proofTree.getDepth(), tmpNet.clone());
			proofTree.incDepth();
			ConstraintNet solution = backtracking(1, tmpNet);
			printSolution(solution);
		}
	}

	private static ConstraintNet backtracking(int id, ConstraintNet actualNode)
			throws CloneNotSupportedException, Exception {
		int nodeID = id;
		ConstraintNet net = actualNode.clone();

		while (!net.isSolved() && net != null) {
			if (ac3_la(nodeID, net)) {
				if ((nodeID + 1) < net.getNodes().size()) {
					for (Type val : net.getDomain(nodeID + 1)) {
						net.assumeNodeValue(nodeID + 1, val);
						proofTree.add(proofTree.getDepth(), net.clone());
					}
					// System.out.println("2 " + actualNode.equals(net));
					proofTree.incDepth();
					try {
					net = proofTree.getNextNode();
					}catch(Exception e){
						System.out.println("Net1: " + net);
					}
					nodeID = proofTree.getCurrentDepth();
				} else {
					try {
					net = proofTree.getNextNode();
					}catch(Exception e){
						System.out.println("Net2: " + net);
					}
					nodeID = proofTree.getCurrentDepth();
				}
			}

			// System.out.println("#############################");
			// System.out.println("NodeID: " + nodeID);
			// System.out.println(net);
		}
		return net;
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
	public static boolean ac3_la(int cv, ConstraintNet actualNode)
			throws Exception {
		HashSet<Edge> q = new HashSet<Edge>();
		HashSet<Edge> q_clone;
		boolean consistent;

		// Q <- {(Vi,Vcv) in arcs(G), i>cv);
		for (Edge e : actualNode.getEdges()) {
			if (e.getN2().getNr() == cv && e.getN1().getNr() > cv) {
				q.add(e);
			}
		}

		// consistent <- true;
		consistent = true;

		q_clone = (HashSet<Edge>) q.clone();
		Edge e;
		int id = 0;
		Iterator<Edge> iter = q.iterator();

		// while not Q empty & consistent
		while (iter.hasNext() && consistent) {
			// select and delete any arc (Vk, Vm) from Q;
			e = iter.next();
			if (id == 0) {
				q_clone.remove(e);
			} else if (id == 1) {
				q.remove(e);
			}

			// if REVISE(Vk, Vm) then
			if (revise(e.getN1(), e.getN2(), e)) {
				// Q <- Q union {(Vi, Vk) such that (Vi, Vk) in arcs(G), i#k,
				// i#m, i>cv)
				for (Edge edge : actualNode.getEdges()) {
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

		}
		System.out.println("consistent: " + consistent);
		return consistent;
	}

	private static boolean revise(Node vi, Node vj, Edge e) throws Exception {
		boolean delete = false;
		boolean delete_ti = true;
		Node vi_clone = vi.clone();

		for (Type ti : vi.getDomain()) {
			for (Constraint c : e.getConstraints()) {
				delete_ti = true;
				for (Type tj : vj.getDomain()) {
					if (c.isSatisfied(ti, tj)) {
						delete_ti = false;
						break;
					}
				}
			}
			if (delete_ti) {
				vi_clone.removeElem(ti);
				delete = true;
			}
		}

		actualNode.removeNode(vi);
		actualNode.insertNode(vi_clone);

		return delete;
	}

}
