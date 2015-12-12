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
			System.out.println("annahmeknoten: v" + assumptionVertex.getLabel() + " annahmewert: " + assumptionValue);
			if (ac3_la_alg.ac3_la_procedure(assumptionVertex, assumptionValue)) {
				solutionMap.put(assumptionVertex.getLabel(), assumptionValue);

				if (vertexCounter == graphOrig.getVertexCounter())
					return solutionMap;

				assumptionVertex = graphOrig.getVertex(String.valueOf(vertexCounter));
				assumptionValueList = new ArrayList<Integer>(
						graphOrig.getVertex(String.valueOf(vertexCounter++)).getDomain());
				assumptionValue = assumptionValueList.get(0);
				assumptionValueList.remove(0);

				solutionMap.putAll(
						solve(graphOrig, assumptionVertex, assumptionValueList, assumptionValue, vertexCounter));

			} else {
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
