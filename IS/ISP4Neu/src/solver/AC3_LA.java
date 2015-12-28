package solver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.UndirectedGraph;

import constraint.AllDiffConstraint;
import constraint.BinaryConstraint;
import constraint.UnaryConstraint;
import datastructs.Edge;
import datastructs.Tupel;
import datastructs.Vertex;

public class AC3_LA {

	private UndirectedGraph<Vertex, Edge<Vertex>> constraintNetz = null;
	private Vertex assumptionVertex;
	private int assumptionValue;

	public AC3_LA(UndirectedGraph<Vertex, Edge<Vertex>> g) {
		constraintNetz = g;
	}

	public boolean ac3_la_procedure(Vertex assumptionVertex, int assumptionValue) {
		int cv = assumptionVertex.getId();
		this.assumptionVertex = assumptionVertex;
		this.assumptionValue = assumptionValue;

		boolean consistent = true;
		Set<Edge<Vertex>> q = new HashSet<Edge<Vertex>>();
		List<Edge<Vertex>> neighborsOfCv = new ArrayList<Edge<Vertex>>(constraintNetz.edgesOf(assumptionVertex));

		/**
		 * Menge Q erzeugen Hierbei muss unterschieden werden, an welchem Ende
		 * der Kante der Knoten vK ist
		 */
		for (Edge<Vertex> item : neighborsOfCv) {
			if (((Vertex) item.getV2()).getId() == cv && ((Vertex) item.getV1()).getId() > cv)
				q.add(item);
			else if (((Vertex) item.getV1()).getId() == cv && ((Vertex) item.getV2()).getId() > cv) {
				q.add(new Edge<Vertex>(((Vertex) item.getV2()), ((Vertex) item.getV1()), item.getConstraintList()));
			}
		}
		System.out.println("Q: " + q.toString());
		Vertex vK = null;

		while (!q.isEmpty() && consistent) {
			Edge<Vertex> arc = q.iterator().next();
			q.remove(arc);

			if (revise(arc)) {
				vK = ((Vertex) arc.getV1());
				int k = Integer.valueOf(vK.getId());
				int m = Integer.valueOf(((Vertex) arc.getV2()).getId());
				List<Edge<Vertex>> neighborsOfK = new ArrayList<Edge<Vertex>>(constraintNetz.edgesOf(vK));

				for (Edge<Vertex> item : neighborsOfK) {
					int i = Integer.valueOf(((Vertex) item.getV1()).getId());
					if (i == k)
						i = Integer.valueOf(((Vertex) item.getV2()).getId());
					if (i != k && i != m && i > cv) {
						q.add(new Edge<Vertex>(item.getV2(), item.getV1(), item.getConstraintList()));
					}
					consistent = !vK.getDomain().isEmpty();
				}
			}
		}

		return consistent;
	}

	private boolean revise(Edge<Vertex> arc) {
		boolean delete = false;
		Set<Integer> delSet = new HashSet<Integer>();
		Set<Integer> domY = new HashSet<Integer>();

		Set<Integer> domX = null;

		// Unaere Constraints der Knoten pruefen
		List<UnaryConstraint> constraintListU = (List<UnaryConstraint>) ((Vertex) arc.getV1()).getUnaryConstraintList();
		if (constraintListU.isEmpty())
			domX = new HashSet<Integer>(((Vertex) arc.getV1()).getDomain());
		else {
			domX = new HashSet<Integer>();
			for (UnaryConstraint constraint : constraintListU) {
				domX.addAll(constraint.operationUnary(((Vertex) arc.getV1()).getId()));
				delete = true;
			}
		}

		// Ist der Annahmeknoten der Nachbar des zu beschraenkenden Knoten,
		// muss der Annahmewert des Annahmeknoten verwendet werden
		if (((Vertex) arc.getV2()).equals(assumptionVertex))
			domY.add(assumptionValue);
		else
			domY = ((Vertex) arc.getV2()).getDomain();

		//Im Vorfeld AllDifferent Constraint abarbeiten
		List<BinaryConstraint> constraintListB = (List<BinaryConstraint>) arc.getConstraintList();
		for (BinaryConstraint constraint : constraintListB) {
			if (constraint instanceof AllDiffConstraint) {
				delete = domX.removeAll(constraint.operationBinaryAllDiff(domX, domY));
			}
		}

		for (int x : domX) {
			for (BinaryConstraint constraint : constraintListB) {
				if (!(constraint instanceof AllDiffConstraint)) {
					List<Tupel> crossProduct = generateCrossProduct(x, domY);

					// Iteration ueber die Menge Dj erfolgt in checkConstraints
					if (checkTupelWithConstraints(crossProduct, arc.getConstraintList())) {
						delete = true;
						delSet.add(x);
					}

				}
			}

		}

		//Zu loeschende Menge erzeugen und die Domaene des jeweiligen Knoten anpassen
		Set<Integer> newSet = new HashSet<Integer>(domX);
		newSet.removeAll(delSet);
		((Vertex) arc.getV1()).setDomain(newSet);

		return delete;
	}

	private List<Tupel> generateCrossProduct(Integer x, Set<Integer> valueSet) {
		List<Tupel> crossProduct = new ArrayList<Tupel>();

		for (Integer y : valueSet) {
			crossProduct.add(new Tupel(x, y));
		}

		return crossProduct;
	}

	private boolean checkTupelWithConstraints(List<Tupel> crossProduct, List<BinaryConstraint> constraintList) {
		int counter = 0;

		for (Tupel item : crossProduct) {
			for (BinaryConstraint constraint : constraintList) {
				if (!constraint.operationBinary(item.getX(), item.getY()))
					counter++;
			}
		}

		return counter == crossProduct.size() ? true : false;
	}

	public UndirectedGraph<Vertex, Edge<Vertex>> getConstraintNetz() {
		return constraintNetz;
	}

	public void setConstraintNetz(UndirectedGraph<Vertex, Edge<Vertex>> constraintNetz) {
		this.constraintNetz = constraintNetz;
	}

}
