package ac3_la;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import constraints.Constraint;
import datastructs.Edge;
import datastructs.Graph;

public class AC3_LA {

	private Graph constraintNetz = null;

	public AC3_LA(Graph g) {
		constraintNetz = g;
	}

	public boolean ac3_la_procedure(int cv) {
		boolean consistent = true;
		// Set<Edge> allEdges = constraintNetz.getEdges();
		Set<Edge> q = new HashSet<Edge>();
		List<Edge> neighborsOfCv = constraintNetz.getVertex("" + cv).getNeighbors();
		// System.out.println(constraintNetz.getVertex(""+cv).getNeighbors());

		/**
		 * Menge Q erzeugen
		 */
		for (Edge item : neighborsOfCv) {
			if (Integer.valueOf(item.getTwo().getLabel()) > cv) {
				Edge e = new Edge(item.getTwo(), item.getOne(), item.getConstraintList());
				q.add(e);
//				System.out.println(e);
			}
		}

		while (!q.isEmpty() & consistent) {
			System.out.println("Q: " + q.toString());
			Edge arc = q.iterator().next();
			q.remove(arc);
			// System.out.println(arc);
//			System.out.println("gewählte arc: " + arc.toString());
//			 System.out.println("Q enthält noch: " + q.toString());
//			 System.out.println("Q Empty?: " + q.isEmpty());

			if (revise(arc)) {
				int k = Integer.valueOf(arc.getOne().getLabel());
//				System.out.print("k: " + k);
				int m = Integer.valueOf(arc.getTwo().getLabel());
//				System.out.print("m: " + m);
				List<Edge> neighborsOfK = constraintNetz.getVertex("" + k).getNeighbors();
				// System.out.println(neighborsOfK);

				for (Edge item : neighborsOfK) {
					int i = Integer.valueOf(item.getOne().getLabel());
					if (i == k)
						i = Integer.valueOf(item.getTwo().getLabel());
//					System.out.println("i: " + i);
					if (/* i != k && */ i != m && i > cv) {
						Edge e = new Edge(item.getTwo(), item.getOne(), item.getConstraintList());
						q.add(e);
						System.out.println(e);
					}
					consistent = !constraintNetz.getVertex("" + k).getDomain().isEmpty();
				}
			}
		}

		return consistent;
	}

	private boolean revise(Edge arc) {
		boolean delete = false;
		Set<Integer> delSet = new HashSet<Integer>();
		int y = 1;
		for (int x : arc.getTwo().getDomain()) {
//			for (int y : arc.getOne().getDomain()) {
				for (Constraint item : arc.getConstraintList()) {
					if (item.operation(x, y)) {
//						 System.out.println("x: " + x + " y: " + y);
//						 System.out.println(item.getName());
						delSet.add(x);
						delete = true;
					}
				}
//			}
		}
		Set<Integer> newSet = new HashSet<Integer>(arc.getTwo().getDomain());
		newSet.removeAll(delSet);
		arc.getTwo().setDomain(newSet);
		return delete;
	}

	public Graph getConstraintNetz() {
		return constraintNetz;
	}

	public void setConstraintNetz(Graph constraintNetz) {
		this.constraintNetz = constraintNetz;
	}

}
