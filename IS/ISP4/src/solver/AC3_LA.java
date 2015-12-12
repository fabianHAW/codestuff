package solver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import constraints.AllDiffConstraint;
import constraints.Constraint;
import datastructs.Edge;
import datastructs.Graph;
import datastructs.Tupel;
import datastructs.Vertex;

public class AC3_LA {

	private Graph constraintNetz = null;
	private Vertex assumptionVertex;
	private int assumptionValue;

	public AC3_LA(Graph g) {
		constraintNetz = g;
	}

	public boolean ac3_la_procedure(Vertex assumptionVertex, int assumptionValue) {
		int cv = Integer.valueOf(assumptionVertex.getLabel());
		this.assumptionVertex = assumptionVertex;
		this.assumptionValue = assumptionValue;

		boolean consistent = true;
		Set<Edge> q = new HashSet<Edge>();
		List<Edge> neighborsOfCv = constraintNetz.getVertex("" + cv).getNeighbors();

		/**
		 * Menge Q erzeugen
		 */
		for (Edge item : neighborsOfCv) {
			if (Integer.valueOf(item.getTwo().getLabel()) > cv) {
				Edge e = new Edge(item.getTwo(), item.getOne(), item.getConstraintList());
				q.add(e);
			}
		}

		while (!q.isEmpty() & consistent) {
			System.out.println("Q: " + q.toString());
			Edge arc = null;
			boolean isAssumptionVertex = false;
			for (Edge item : q) {
				if (item.getTwo().equals(assumptionVertex)) {
					arc = item;
					isAssumptionVertex = true;
					break;
				}
			}
			if (!isAssumptionVertex) {
				arc = q.iterator().next();
			}
			q.remove(arc);

			if (revise(arc)) {
				int k = Integer.valueOf(arc.getOne().getLabel());
				int m = Integer.valueOf(arc.getTwo().getLabel());
				List<Edge> neighborsOfK = constraintNetz.getVertex("" + k).getNeighbors();

				for (Edge item : neighborsOfK) {
					int i = Integer.valueOf(item.getOne().getLabel());
					if (i == k)
						i = Integer.valueOf(item.getTwo().getLabel());
					if (/* i != k && */ i != m && i > cv) {
						Edge e = new Edge(item.getTwo(), item.getOne(), item.getConstraintList());
						q.add(e);
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
		Set<Integer> valueSet = new HashSet<Integer>();

		for (int x : arc.getOne().getDomain()) {
			// Ist der Annahmeknoten der Nachbar des zu beschraenkenden Knoten,
			// muss der Annahmewert des Annahmeknoten verwendet werden
			if (arc.getTwo().equals(assumptionVertex))
				valueSet.add(assumptionValue);
			else
				valueSet = arc.getTwo().getDomain();

			List<Tupel> crossProduct = generateCrossProduct(x, valueSet);

			// Iteration ueber die Menge Dj erfolgt in checkConstraints
			if (checkTupelWithConstraints(crossProduct, arc.getConstraintList())) {
				delete = true;
				delSet.add(x);
			}

		}
		Set<Integer> newSet = new HashSet<Integer>(arc.getOne().getDomain());
		newSet.removeAll(delSet);
		arc.getOne().setDomain(newSet);
		return delete;
	}

	private List<Tupel> generateCrossProduct(Integer x, Set<Integer> valueSet) {
		List<Tupel> crossProduct = new ArrayList<Tupel>();

		for (Integer y : valueSet) {
			crossProduct.add(new Tupel(x, y));
		}

		return crossProduct;
	}

	private boolean checkTupelWithConstraints(List<Tupel> crossProduct, List<Constraint> constraintList) {
		int counter = 0;
		boolean alldiffValid = false;

		for (Tupel item : crossProduct) {
			for (Constraint constraint : constraintList) {
				if (constraint instanceof AllDiffConstraint) {
					if (constraint.operation(item.getX(), item.getY())) {
						counter = crossProduct.size();
						// wenn AllDiffConstraint zutrifft, brauchen alle
						// weiteren Constraints nicht mehr geprueft werden, da
						// Wert x in jedem Fall geloescht wird
						alldiffValid = true;
						break;
					}
				} else if (constraint.operation(item.getX(), item.getY())) {
					counter++;
					// Sobald ein Constraint erfuellt ist, muessen die
					// Restlichen nicht weiter betrachtet werden
					break;
				}
			}
			// Sobald der Alldifferent Constraint zugetroffen ist, muss der rest
			// des Kreuzproduktes nicht weiter betrachtet werden, da x-Wert in
			// jedem Fall geloescht wird
			if (alldiffValid)
				break;
		}

		return counter == crossProduct.size() ? true : false;
	}

	public Graph getConstraintNetz() {
		return constraintNetz;
	}

	public void setConstraintNetz(Graph constraintNetz) {
		this.constraintNetz = constraintNetz;
	}

}
