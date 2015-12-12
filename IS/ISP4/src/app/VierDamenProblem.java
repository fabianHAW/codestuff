package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ac3_la.AC3_LA;
import constraints.Constraint;
import constraints.Diff1Constraint;
import constraints.Diff2Constraint;
import constraints.Diff3Constraint;
import constraints.UngleichConstraint;
import datastructs.Graph;
import datastructs.ObjectCloner;
import datastructs.Vertex;

public class VierDamenProblem {

	public static void main(String[] args) {

		Graph graphOrig = new Graph();

		Vertex[] vertices = new Vertex[4];
		Set<Integer> domain = new HashSet<Integer>();
		for (int i = 1; i < vertices.length + 1; i++) {
			domain.add(i);
		}

		for (int i = 0; i < vertices.length; i++) {
			vertices[i] = new Vertex("" + i, domain);
			graphOrig.addVertex(vertices[i], true);
		}

		List<Constraint> cL1 = new ArrayList<Constraint>();
		List<Constraint> cL2 = new ArrayList<Constraint>();
		List<Constraint> cL3 = new ArrayList<Constraint>();
		cL1.add(new UngleichConstraint("Ungleich"));
		cL1.add(new Diff1Constraint("Diff1"));
		cL2.add(new UngleichConstraint("Ungleich"));
		cL2.add(new Diff2Constraint("Diff2"));
		cL3.add(new UngleichConstraint("Ungleich"));
		cL3.add(new Diff3Constraint("Diff3"));

		for (int i = 0; i < vertices.length; i++) {
			for (int j = i + 1; j < vertices.length; j++) {
				if ((i == 0 && j == 1) || (i == 1 && j == 2) || (i == 2 && j == 3))
					graphOrig.addEdge(vertices[i], vertices[j], cL1);
				if ((i == 0 && j == 2) || (i == 1 && j == 3))
					graphOrig.addEdge(vertices[i], vertices[j], cL2);
				if ((i == 0 && j == 3))
					graphOrig.addEdge(vertices[i], vertices[j], cL3);
			}
		}

		Vertex assumptionVertex = null;
		int assumptionValue = 0;
		List<Integer> assumptionValueList = null;
		int vertexCounter = 0;

		assumptionVertex = graphOrig.getVertex(String.valueOf(vertexCounter));
		assumptionValueList = new ArrayList<Integer>(graphOrig.getVertex(String.valueOf(vertexCounter++)).getDomain());
		assumptionValue = assumptionValueList.get(0);
		assumptionValueList.remove(0);

		Map<String, Integer> solutionMap = solve(graphOrig, assumptionVertex, assumptionValueList, assumptionValue,
				vertexCounter);

		System.out.println("\n***SOLUTION***");
		for (Entry<String, Integer> item : solutionMap.entrySet()) {
			System.out.println("v" + item.getKey() + " mit dem Wert: " + item.getValue());
		}

	}

	private static Map<String, Integer> solve(Graph graphOrig, Vertex assumptionVertex,
			List<Integer> assumptionValueList, Integer assumptionValue, int vertexCounter) {
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
