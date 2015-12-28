package solver;

import java.util.HashMap;
import java.util.Map;

import org.jgrapht.UndirectedGraph;

import datastructs.Edge;
import datastructs.Node;
import datastructs.ObjectCloner;
import datastructs.Vertex;

public class CSolver {

	private Map<String, Integer> solutionMap = new HashMap<String, Integer>();

	public void solve(UndirectedGraph<Vertex, Edge<Vertex>> graphOrig) {
		int counter = 0;
		Node n = new Node(graphOrig, counter);
		counter++;
		solve(graphOrig, n, counter);

	}

	public boolean solve(UndirectedGraph<Vertex, Edge<Vertex>> graphOrig, Node n, int counter) {
		// Abbruchkriterium, wenn der letzte Knoten erreicht wurde.
		if (counter > graphOrig.vertexSet().size())
			return true;

		System.out.println("****EBENE: " + (n.getDepth()) + "*****");
		System.out.println("Annahmeknoten: " + n.getAssumptionVertex() + " Annahmewert: " + n.getAssumptionValue());

		AC3_LA ac3_la_alg = new AC3_LA(graphOrig);
		UndirectedGraph<Vertex, Edge<Vertex>> graphCopy = getGraphCopy(graphOrig);

		// Mit aktuellem Annahmeknoten und Annahmewert auf Konsistenz pruefen
		boolean consistent = ac3_la_alg.ac3_la_procedure(n.getAssumptionVertex(), n.getAssumptionValue());

		return check(graphOrig, graphCopy, n, consistent, counter);
	}

	private boolean check(UndirectedGraph<Vertex, Edge<Vertex>> graphOrig,
			UndirectedGraph<Vertex, Edge<Vertex>> graphCopy, Node n, boolean isConsistent, int counter) {

		if (isConsistent) {
			// Da Kanten konsistent sind, gehoert der Knoten zur Loesung
			solutionMap.put(n.getAssumptionVertex().getName(), n.getAssumptionValue());
			// Der naechste Knoten wird betrachtet
			Node nNew = new Node(graphOrig, counter);
			counter++;

			boolean solved = solve(graphOrig, nNew, counter);

			// wurde im unteren Bereich des Baumes eine Inkosistenz entdeckt,
			// muss Domaene im aktuellen Knoten weiter ausgeschoepft werden
			if (!solved) {
				// Loesung daher nicht mehr korrekt
				solutionMap.remove(n.getAssumptionVertex().getName(), n.getAssumptionValue());
				counter--;
			}

			while (!solved) {
				// gibt es weitere Werte in der Domaene des aktuellen Knotens?
				int size = n.getAssumptionValueList().size();
				for (int i = 0; i < size; i++) {
					n.setAssumptionValue();
					solved = solve(graphCopy, n, counter);
					// wurde Problem im unteren Teil des Baums geloest?
					if (solved)
						return true;
				}
				return false;
			}
		} else {
			if (n.setAssumptionValue()) {
				return solve(graphCopy, n, counter);
			} else {
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	private UndirectedGraph<Vertex, Edge<Vertex>> getGraphCopy(UndirectedGraph<Vertex, Edge<Vertex>> graphOrig) {
		UndirectedGraph<Vertex, Edge<Vertex>> graphCopy = null;

		// Kopie eines Graphen erzeugen
		try {
			graphCopy = (UndirectedGraph<Vertex, Edge<Vertex>>) ObjectCloner.deepClone(graphOrig);
		} catch (Exception e) {
			System.out.println("Beim kopieren des Graphen trat ein Fehler auf: " + e);
		}
		return graphCopy;
	}

	public Map<String, Integer> getSolutionMap() {
		return solutionMap;
	}
}
