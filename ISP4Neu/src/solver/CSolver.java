package solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.UndirectedGraph;

import datastructs.Edge;
import datastructs.ObjectCloner;
import datastructs.Vertex;

public class CSolver {

	public Map<String, Integer> solve(UndirectedGraph<Vertex, Edge>  graphOrig, List<Vertex> vList, Vertex assumptionVertex, List<Integer> assumptionValueList,
			Integer assumptionValue, int vertexCounter) {
		AC3_LA ac3_la_alg = new AC3_LA(graphOrig);

		Map<String, Integer> solutionMap = new HashMap<String, Integer>();

		UndirectedGraph<Vertex, Edge> graphCopy = null;

		try {
			graphCopy = (UndirectedGraph<Vertex, Edge>) ObjectCloner.deepClone(graphOrig);
		} catch (Exception e) {
			System.out.println("Beim kopieren des Graphen trat ein Fehler auf: " + e);
		}

		if (graphCopy != null) {
			System.out.println("annahmeknoten: " + assumptionVertex.getName() + " annahmewert: " + assumptionValue);
			if (ac3_la_alg.ac3_la_procedure(assumptionVertex, assumptionValue)) {
				solutionMap.put(assumptionVertex.getName(), assumptionValue);

				if (vertexCounter >= graphOrig.vertexSet().size())
					return solutionMap;

				// war die Annahme in Ordnung, wird der naechste Knoten mit
				// einem zur Verfuegung stehenden Wert gewaehlt
				assumptionVertex = vList.get(vertexCounter++);
				assumptionValueList = new ArrayList<Integer>(assumptionVertex.getDomain());
				
				
				
				if (!assumptionValueList.isEmpty()) {
					assumptionValue = assumptionValueList.get(0);
					assumptionValueList.remove(0);

					solutionMap.putAll(
							solve(graphOrig, vList, assumptionVertex, assumptionValueList, assumptionValue, vertexCounter));
				}
			} else {
				// Ist die vorherige Annahme falsch, wird die naechste des
				// selben Knoten gewaehlt
				if (assumptionValueList.isEmpty())
					return solutionMap;
				
				List<Vertex> vListNeu = new ArrayList<Vertex>(graphCopy.vertexSet());
				Collections.sort(vListNeu);
				
				for(Vertex item:vListNeu){
					System.out.println(item.getId() + " " + item.getDomain());
				}
				assumptionValue = assumptionValueList.get(0);
				assumptionValueList.remove(0);
				solutionMap.putAll(
						solve(graphCopy, vListNeu, assumptionVertex, assumptionValueList, assumptionValue, vertexCounter));
			}
		} else
			return null;

		return solutionMap;
	}

}
