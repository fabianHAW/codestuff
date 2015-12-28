package solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import datastructs.Graph;
import datastructs.ObjectCloner;
import datastructs.Vertex;

public class CSolver {

	public Map<String, Integer> solve(Graph graphOrig, Vertex assumptionVertex, List<Integer> assumptionValueList,
			Integer assumptionValue, int vertexCounter) {
		AC3_LA ac3_la_alg = new AC3_LA(graphOrig);

		Map<String, Integer> solutionMap = new HashMap<String, Integer>();

		Graph graphCopy = null;

		try {
			graphCopy = (Graph) ObjectCloner.deepClone(graphOrig);
		} catch (Exception e) {
			System.out.println("Beim kopieren des Graphen trat ein Fehler auf: " + e);
		}

		if (graphCopy != null) {
			System.out.println("annahmeknoten: " + assumptionVertex.getName() + " annahmewert: " + assumptionValue);
			if (ac3_la_alg.ac3_la_procedure(assumptionVertex, assumptionValue)) {
				solutionMap.put(assumptionVertex.getName(), assumptionValue);

				if (vertexCounter > graphOrig.getVertexCounter())
					return solutionMap;

				// war die Annahme in Ordnung, wird der naechste Knoten mit
				// einem zur Verfuegung stehenden Wert gewaehlt
				assumptionVertex = graphOrig.getVertex(String.valueOf(vertexCounter));
				assumptionValueList = new ArrayList<Integer>(
						graphOrig.getVertex(String.valueOf(vertexCounter++)).getDomain());

				if (!assumptionValueList.isEmpty()) {
					assumptionValue = assumptionValueList.get(0);
					assumptionValueList.remove(0);

					solutionMap.putAll(
							solve(graphOrig, assumptionVertex, assumptionValueList, assumptionValue, vertexCounter));
				}
			} else {
				// Ist die vorherige Annahme falsch, wird die naechste des
				// selben Knoten gewaehlt
				if (assumptionValueList.isEmpty())
					return solutionMap;

				assumptionValue = assumptionValueList.get(0);
				assumptionValueList.remove(0);
				solutionMap.putAll(
						solve(graphCopy, assumptionVertex, assumptionValueList, assumptionValue, vertexCounter));
			}
		} else
			return null;

		return solutionMap;
	}

}
