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
import datastructs.Edge;
import datastructs.Graph;
import datastructs.Vertex;

public class VierDamenProblem {

	public static void main(String[] args) {

		Graph graph = new Graph();

		Vertex[] vertices = new Vertex[4];
		Set<Integer> domain = new HashSet<Integer>();
		for (int i = 1; i < vertices.length + 1; i++) {
			domain.add(i);
		}

		for (int i = 0; i < vertices.length; i++) {
			vertices[i] = new Vertex("" + i, domain);
			graph.addVertex(vertices[i], true);
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
					graph.addEdge(vertices[i], vertices[j], cL1);
				if ((i == 0 && j == 2) || (i == 1 && j == 3))
					graph.addEdge(vertices[i], vertices[j], cL2);
				if ((i == 0 && j == 3))
					graph.addEdge(vertices[i], vertices[j], cL3);
			}
		}
//		for(Edge item : graph.getEdges()){
//			System.out.println(item.toString());
//		}

		// // display the initial setup- all vertices adjacent to each other
		// for (int i = 0; i < vertices.length; i++) {
		// System.out.println(vertices[i]);
		//
		// System.out.println(vertices[i].getNeighbors());
		//// for (int j = 0; j < vertices[i].getNeighborCount(); j++) {
		//// System.out.println(vertices[i].getNeighbor(j));
		//// }
		//
		// System.out.println();
		// }

		AC3_LA ac3_la_alg = new AC3_LA(graph);

		Map<String, Integer> solutionMap = new HashMap<String, Integer>();
		boolean solutionFound = false;
		Vertex assumptionVertex = null;
		int assumptionValue = 0;
		List<Integer> assumptionValueList = null;
		int vertexCounter = 0;

		assumptionVertex = graph.getVertex(String.valueOf(vertexCounter));
		assumptionValueList = new ArrayList<Integer>(graph.getVertex(String.valueOf(vertexCounter++)).getDomain());
		
//		assumptionValueList.forEach(l -> System.out.println(l));
		
		while (!solutionFound) {
			// for (int i = 0; i < 4; i++) {
			assumptionValue = assumptionValueList.get(0);
			assumptionValueList.remove(0);
			System.out.println("annahmeknoten: " + assumptionVertex.getLabel() + " annahmewert: " + assumptionValue);
//			assumptionValueList.forEach(l -> System.out.println(l));
//			System.out.println(assumptionValueList.get(0));
			// for (int j = 0; j < 4; j++) {
			// while (true) {
			if (ac3_la_alg.ac3_la_procedure(assumptionVertex, assumptionValue)) {
				solutionMap.put(assumptionVertex.getLabel(), assumptionValue);
				assumptionVertex = graph.getVertex(String.valueOf(vertexCounter));
				assumptionValueList = new ArrayList<Integer>(
						graph.getVertex(String.valueOf(vertexCounter++)).getDomain());
				
				System.out.println("inside");
				// i = 4;
				// break;
			} else {
				vertexCounter = 0;
				// assumptionVertex =
				// graph.getVertex(String.valueOf(vertexCounter));
				// assumptionValueList = new ArrayList<Integer>(
				// graph.getVertex(String.valueOf(vertexCounter++)).getDomain());
				assumptionValue = assumptionValueList.get(0);
				assumptionValueList.remove(0);
				System.out.println("else");
				// i = 4;
				// break;
				// }
				// }
			}
			if (solutionMap.size() == 4)
				solutionFound = true;
			// vertexCounter++;
		}

		for (Entry<String, Integer> item : solutionMap.entrySet()) {
			System.out.println(item.getKey() + " " + item.getValue());
		}

//		 System.out.println(ac3_la_alg.ac3_la_procedure(graph.getVertex("0"),
//		 1));
		System.out.println(ac3_la_alg.getConstraintNetz().getEdges().toString());

	}

}
