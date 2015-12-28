package problem;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.SimpleGraph;

public class EinsteinNeu {
	
	public static void main(String[] args) {
		
		UndirectedGraph<String, DefaultEdge> g =
		        new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
		
		String v1 = "v1";
	    String v2 = "v2";
	    String v3 = "v3";
	    String v4 = "v4";

	    // add the vertices
	    g.addVertex(v1);
	    g.addVertex(v2);
	    g.addVertex(v3);
	    g.addVertex(v4);

	    // add edges to create a circuit
	    g.addEdge(v1, v2, new DefaultEdge());
	    g.addEdge(v2, v3, new DefaultEdge());
	    g.addEdge(v3, v4, new DefaultEdge());
	    g.addEdge(v4, v1, new DefaultEdge());
	    System.out.println(g.edgesOf("v3"));
//	    System.out.println(g.toString());
		
	}

}
