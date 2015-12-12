package problem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import constraints.Constraint;
import constraints.Diff1Constraint;
import constraints.Diff2Constraint;
import constraints.Diff3Constraint;
import constraints.UngleichConstraint;
import datastructs.Graph;
import datastructs.Vertex;
import solver.CSolver;

public class VierDamenProblem {

	public static void main(String[] args) {

		Graph graphOrig = new Graph();
		CSolver solver = new CSolver();

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

		Map<String, Integer> solutionMap = solver.solve(graphOrig, assumptionVertex, assumptionValueList,
				assumptionValue, vertexCounter);

		System.out.println("\n***SOLUTION***");
		for (Entry<String, Integer> item : solutionMap.entrySet()) {
			System.out.println("v" + item.getKey() + " mit dem Wert: " + item.getValue());
		}

	}

}
