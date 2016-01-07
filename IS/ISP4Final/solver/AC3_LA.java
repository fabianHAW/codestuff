package solver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.UndirectedGraph;

import constraint.BinaryConstraint;
import constraint.UnaryConstraint;
import datastructs.Edge;
import datastructs.Vertex;

public class AC3_LA {

	private UndirectedGraph<Vertex, Edge<Vertex>> constraintNetz = null;
	private Vertex assumptionVertex = null;
	private int assumptionValue;

	public AC3_LA(UndirectedGraph<Vertex, Edge<Vertex>> g) {
		constraintNetz = g;
	}

	public void ac3_procedure() {

		/**
		 * Menge Q erzeugen Hierbei muss unterschieden werden, an welchem Ende
		 * der Kante der Knoten vK ist
		 */
		Set<Edge<Vertex>> q = new HashSet<Edge<Vertex>>();
		for (Edge<Vertex> item : constraintNetz.edgeSet()) {
			q.add(item);
		}

		Vertex vK = null;

		while (!q.isEmpty()) {

			Edge<Vertex> arc = q.iterator().next();
			q.remove(arc);

			if (revise(arc)) {
				vK = ((Vertex) arc.getV1());
				int k = Integer.valueOf(vK.getId());
				int m = Integer.valueOf(((Vertex) arc.getV2()).getId());
				int i = -1;
				List<Edge<Vertex>> neighborsOfK = rightNeighborList(constraintNetz.edgesOf(vK), vK);

				for (Edge<Vertex> item : neighborsOfK) {
					i = ((Vertex) item.getV1()).getId();
					if (i != k && i != m) {
						q.add(item);
					}
				}
			}
		}

	}

	public boolean ac3_la_procedure(Vertex assumptionVertex, int assumptionValue) {
		int cv = assumptionVertex.getId();
		this.assumptionVertex = assumptionVertex;
		this.assumptionValue = assumptionValue;
		boolean consistent = true;
		Set<Edge<Vertex>> q = new HashSet<Edge<Vertex>>();

		List<Edge<Vertex>> neighborsOfCv = rightNeighborList(constraintNetz.edgesOf(assumptionVertex),
				assumptionVertex);
		/**
		 * Menge Q erzeugen Hierbei muss unterschieden werden, an welchem Ende
		 * der Kante der Knoten vK ist
		 */
		for (Edge<Vertex> item : neighborsOfCv) {
			if (((Vertex) item.getV1()).getId() > cv)
				q.add(item);

		}
		Vertex vK = null;

		while (!q.isEmpty() && consistent) {

			Edge<Vertex> arc = q.iterator().next();
			q.remove(arc);

			if (revise(arc)) {
				vK = ((Vertex) arc.getV1());
				int k = Integer.valueOf(vK.getId());
				int m = Integer.valueOf(((Vertex) arc.getV2()).getId());
				int i = -1;
				List<Edge<Vertex>> neighborsOfK = rightNeighborList(constraintNetz.edgesOf(vK), vK);

				for (Edge<Vertex> item : neighborsOfK) {
					i = ((Vertex) item.getV1()).getId();
					if (i != k && i != m && i > cv) {
						q.add(item);
						consistent = !vK.getDomain().isEmpty();
					}
				}
			}
		}

		return consistent;
	}

	private List<Edge<Vertex>> rightNeighborList(Set<Edge<Vertex>> set, Vertex vk) {
		List<Edge<Vertex>> newList = new ArrayList<Edge<Vertex>>();

		for (Edge<Vertex> item : set) {
			if (item.getV1().equals(vk))
				newList.add(new Edge<Vertex>(item.getV2(), item.getV1(), item.getConstraintList()));
			else
				newList.add(item);
		}

		return newList;
	}

	private boolean revise(Edge<Vertex> arc) {
		boolean delete = false;
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
		if (assumptionVertex != null) {
			if (((Vertex) arc.getV2()).equals(assumptionVertex))
				domY.add(assumptionValue);
			else
				domY = ((Vertex) arc.getV2()).getDomain();
		} else {
			domY = ((Vertex) arc.getV2()).getDomain();
		}

		List<BinaryConstraint> constraintListB = (List<BinaryConstraint>) arc.getConstraintList();
		for (BinaryConstraint constraint : constraintListB) {
			if (domX.removeAll(constraint.operationBinary(domX, domY, arc.getV1().getName())))
				delete = true;

		}

		((Vertex) arc.getV1()).setDomain(domX);

		return delete;
	}

	public UndirectedGraph<Vertex, Edge<Vertex>> getConstraintNetz() {
		return constraintNetz;
	}

	public void setConstraintNetz(UndirectedGraph<Vertex, Edge<Vertex>> constraintNetz) {
		this.constraintNetz = constraintNetz;
	}

}
