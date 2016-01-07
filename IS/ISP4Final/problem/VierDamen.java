package problem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.SimpleGraph;

import constraint.BinaryConstraint;
import constraint.Diff1Constraint;
import constraint.Diff2Constraint;
import constraint.Diff3Constraint;
import constraint.NotEqualConstraint;
import datastructs.Edge;
import datastructs.Vertex;
import solver.CSolver;

public class VierDamen {

	public static void main(String[] args) {

		@SuppressWarnings("unchecked")
		UndirectedGraph<Vertex, Edge<Vertex>> graphOrig = new SimpleGraph<Vertex, Edge<Vertex>>(
				(Class<? extends Edge<Vertex>>) Edge.class);

		Set<Integer> domain = new HashSet<Integer>();

		for (int i = 1; i < 5; i++) {
			domain.add(i);
		}

		Vertex v0 = new Vertex(0, "v0", domain);
		Vertex v1 = new Vertex(1, "v1", domain);
		Vertex v2 = new Vertex(2, "v2", domain);
		Vertex v3 = new Vertex(3, "v3", domain);

		graphOrig.addVertex(v0);
		graphOrig.addVertex(v1);
		graphOrig.addVertex(v2);
		graphOrig.addVertex(v3);

		List<BinaryConstraint> cL1 = new ArrayList<BinaryConstraint>();
		List<BinaryConstraint> cL2 = new ArrayList<BinaryConstraint>();
		List<BinaryConstraint> cL3 = new ArrayList<BinaryConstraint>();
		cL1.add(new NotEqualConstraint());
		cL1.add(new Diff1Constraint());
		cL2.add(new NotEqualConstraint());
		cL2.add(new Diff2Constraint());
		cL3.add(new NotEqualConstraint());
		cL3.add(new Diff3Constraint());

		graphOrig.addEdge(v0, v1, new Edge<Vertex>(v0, v1, cL1));
		graphOrig.addEdge(v0, v2, new Edge<Vertex>(v0, v2, cL2));
		graphOrig.addEdge(v0, v3, new Edge<Vertex>(v0, v3, cL3));

		graphOrig.addEdge(v1, v2, new Edge<Vertex>(v1, v2, cL1));
		graphOrig.addEdge(v1, v3, new Edge<Vertex>(v1, v3, cL2));

		graphOrig.addEdge(v2, v3, new Edge<Vertex>(v2, v3, cL1));

		CSolver solver = new CSolver();

		solver.solve(graphOrig);

		System.out.println("\n***SOLUTION***");
		System.out.println(solver.getSolutionMap());

	}

}
