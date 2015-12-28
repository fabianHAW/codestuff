package solver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.UndirectedGraph;

import constraint.AllDiffConstraint;
import constraint.Constraint;
import datastructs.Edge;
import datastructs.Tupel;
import datastructs.Vertex;

public class AC3_LA {

	private UndirectedGraph<Vertex, Edge> constraintNetz = null;
	private Vertex assumptionVertex;
	private int assumptionValue;

	public AC3_LA(UndirectedGraph<Vertex, Edge> g) {
		constraintNetz = g;
	}

	public boolean ac3_la_procedure(Vertex assumptionVertex, int assumptionValue) {
		int cv = assumptionVertex.getId();
		this.assumptionVertex = assumptionVertex;
		this.assumptionValue = assumptionValue;

		boolean consistent = true;
		Set<Edge> q = new HashSet<Edge>();
//		System.out.println(constraintNetz.containsVertex(assumptionVertex));
//		System.out.println(constraintNetz.toString());
		List<Edge> neighborsOfCv = new ArrayList<Edge>(constraintNetz.edgesOf(assumptionVertex));
		// this.getAllNeighbors(assumptionVertex);
		// constraintNetz.getVertex("" + cv).getNeighbors();

		// nur zum loggen gedacht
		int leereWertemenge = 0;

		/**
		 * Menge Q erzeugen
		 */
		for (Edge item : neighborsOfCv) {
			if (((Vertex) item.getV2()).getId() == cv && ((Vertex) item.getV1()).getId() > cv)
				q.add(item);
			else if (((Vertex) item.getV1()).getId() == cv && ((Vertex) item.getV2()).getId() > cv) {
				q.add(new Edge(((Vertex) item.getV2()), ((Vertex) item.getV1()), item.getConstraintList()));
			}
		}
		Vertex vK = null;
		while (!q.isEmpty() && consistent) {
//			System.out.println("Q: " + q.toString());
			Edge arc = null;
			boolean isAssumptionVertex = false;
			for (Edge item : q) {
				if (((Vertex) item.getV2()).equals(assumptionVertex)) {
					arc = item;
					isAssumptionVertex = true;
					break;
				}
			}
			if (!isAssumptionVertex) {
				arc = q.iterator().next();
			}
			q.remove(arc);
//			System.out.println("gewählte arc: " + arc.toString() + " isAssumptionVertex: " + isAssumptionVertex);

			if (revise(arc)) {
				vK = ((Vertex) arc.getV1());
				int k = Integer.valueOf(vK.getId());
				int m = Integer.valueOf(((Vertex) arc.getV2()).getId());
//				System.out.println(vK.toString());
				List<Edge> neighborsOfK = new ArrayList<Edge>(constraintNetz.edgesOf(assumptionVertex));

				for (Edge item : neighborsOfK) {
					int i = Integer.valueOf(((Vertex) arc.getV1()).getId());
					if (i == k)
						i = Integer.valueOf(((Vertex) arc.getV2()).getId());
					if (i != k && i != m && i > cv) {
						// Edge e = null;
						// if(Integer.valueOf(item.getOne().getLabel()) <
						// Integer.valueOf(item.getTwo().getLabel()))
						// e = new Edge(item.getTwo(), item.getOne(),
						// item.getConstraintList());
						// else
						// e = new Edge(item.getOne(), item.getTwo(),
						// item.getConstraintList());
						q.add(new Edge(item.getV2(), item.getV1(), item.getConstraintList()));
					}
					consistent = !vK.getDomain().isEmpty();
					leereWertemenge = k;
				}
			}
		}

		if (!consistent) {
			System.out.println("***********");
			System.out.println("Consistent: " + consistent);
			System.out.println("Vk: " + vK);
			System.out.println("Wertemenge von Vertex " + leereWertemenge + " ist leer");
			System.out.println("Rest von Q: " + q.toString());
			System.out.println("***********");
		} else if (q.isEmpty()){
			System.out.println("***********");
			System.out.println("Menge Q ist leer");
			System.out.println("***********");
		}
		else
			System.out.println("consistent: " + consistent + " q: " + q.toString());

		return consistent;
	}

	private boolean revise(Edge arc) {
		boolean delete = false;
		Set<Integer> delSet = new HashSet<Integer>();
		Set<Integer> domY = new HashSet<Integer>();

		Set<Integer> domX = new HashSet<Integer>(((Vertex) arc.getV1()).getDomain());

		// Ist der Annahmeknoten der Nachbar des zu beschraenkenden Knoten,
		// muss der Annahmewert des Annahmeknoten verwendet werden
		if (((Vertex) arc.getV2()).equals(assumptionVertex))
			domY.add(assumptionValue);
		else
			domY = ((Vertex) arc.getV2()).getDomain();

		// checkDomainContent(domX, domY);
		List<Constraint> constraintList = (List<Constraint>) arc.getConstraintList();
		for (Constraint constraint : constraintList) {
			if (constraint instanceof AllDiffConstraint) {
//				if (!domX.equals(domY)) {
					delete = domX.removeAll(getIntersection(domX, domY));
//				}
			}
		}
		// System.out.println(arc.getOne().getLabel() + " " + domX.toString() +
		// " " + arc.getTwo().getLabel() + " "
		// + domY.toString());
		// if (arc.getConstraintList().contains(AllDiffConstraint)) {
		//
		// }

		// for (int x : arc.getOne().getDomain()) {
		for (int x : domX) {
			for (Constraint constraint : constraintList) {
				if (!(constraint instanceof AllDiffConstraint)) {
					List<Tupel> crossProduct = generateCrossProduct(x, domY);

					// Iteration ueber die Menge Dj erfolgt in checkConstraints
					if (checkTupelWithConstraints(crossProduct, arc.getConstraintList(),
							((Vertex) arc.getV1()).getName())) {
						delete = true;
						delSet.add(x);
					}

				}
			}

		}

		Set<Integer> newSet = new HashSet<Integer>(domX);
		// newSet.removeAll(delSet);
		newSet.removeAll(delSet);
		// arc.getOne().setDomain(newSet);
		Vertex vOld = (Vertex) arc.getV1();
		vOld.setDomain(newSet);
		
		for(Vertex item : constraintNetz.vertexSet()){
			if(item.equals(vOld))
				item.setDomain(newSet);
		}
		
		return delete;
	}

	private Set<Integer> getIntersection(Set<Integer> domX, Set<Integer> domY) {
		Set<Integer> res = new HashSet<Integer>();
		for (int x : domX) {
			if (domY.contains(x)) {
				res.add(x);
			}
		}
		// System.out.println("getIntersection: " + res.toString());
		return res;
	}

	// private void checkDomainContent(Set<Integer> domX, Set<Integer> domY){
	// if()
	// }

	private List<Tupel> generateCrossProduct(Integer x, Set<Integer> valueSet) {
		List<Tupel> crossProduct = new ArrayList<Tupel>();

		for (Integer y : valueSet) {
			crossProduct.add(new Tupel(x, y));
		}

		return crossProduct;
	}

	private boolean checkTupelWithConstraints(List<Tupel> crossProduct, List<Constraint> constraintList,
			String varName) {
		int counter = 0;
		boolean alldiffValid = false;

		for (Tupel item : crossProduct) {
			for (Constraint constraint : constraintList) {
				// if (constraint instanceof AllDiffConstraint) {
				// if (!constraint.operationBinary(item.getX(), item.getY())) {
				// counter = crossProduct.size();
				// // wenn AllDiffConstraint zutrifft, brauchen alle
				// // weiteren Constraints nicht mehr geprueft werden, da
				// // Wert x in jedem Fall geloescht wird
				// alldiffValid = true;
				// break;
				// }
				// } else
				if (!constraint.operationBinary(item.getX(), item.getY(), varName))
					counter++;
			}
			// Sobald der Alldifferent Constraint zugetroffen ist, muss der rest
			// des Kreuzproduktes nicht weiter betrachtet werden, da x-Wert in
			// jedem Fall geloescht wird
			// if (alldiffValid)
			// break;
		}

		return counter == crossProduct.size() ? true : false;
	}

	public UndirectedGraph<Vertex, Edge> getConstraintNetz() {
		return constraintNetz;
	}

	public void setConstraintNetz(UndirectedGraph<Vertex, Edge> constraintNetz) {
		this.constraintNetz = constraintNetz;
	}

}
