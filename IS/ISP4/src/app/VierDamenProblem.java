package app;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ac3_la.AC3_LA;
import constraints.Constraint;
import constraints.Diff1Constraint;
import constraints.Diff2Constraint;
import constraints.Diff3Constraint;
import constraints.UngleichConstraint;
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
				if((i == 0 && j == 1) || (i == 1 && j == 2) || (i == 2 && j == 3))
					graph.addEdge(vertices[i], vertices[j], cL1);
				if((i == 0 && j == 2) || (i == 1 && j == 3))
					graph.addEdge(vertices[i], vertices[j], cL2);
				if((i == 0 && j == 3))
					graph.addEdge(vertices[i], vertices[j], cL3);
			}
		}

//		// display the initial setup- all vertices adjacent to each other
//		for (int i = 0; i < vertices.length; i++) {
//			System.out.println(vertices[i]);
//
//			System.out.println(vertices[i].getNeighbors());
////			for (int j = 0; j < vertices[i].getNeighborCount(); j++) {
////				System.out.println(vertices[i].getNeighbor(j));
////			}
//
//			System.out.println();
//		}
		
		
		AC3_LA ac3_la_alg = new AC3_LA(graph);
		ac3_la_alg.ac3_la_procedure(0);
		System.out.println(ac3_la_alg.getConstraintNetz().getEdges().toString());
		
	}

}
