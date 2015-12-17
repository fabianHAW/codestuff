package problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.SimpleGraph;

import constraint.AllDiffConstraint;
import constraint.Constraint;
import constraint.GleichConstraint;
import constraint.NachbarMinusEinsConstraint;
import constraint.NachbarPlusMinusEinsConstraint;
import datastructs.Edge;
import datastructs.Vertex;
import solver.CSolver;

public class Einstein {
	
	public static void main(String[] args) {
		UndirectedGraph<Vertex, Edge> graphOrig =
		        new SimpleGraph<Vertex, Edge>(Edge.class);
		
		Set<Integer> fullDomain = new HashSet<Integer>();
		// Set<Integer> blauWeissDomain = new HashSet<Integer>();
		// Set<Integer> gruenDomain = new HashSet<Integer>();
		Set<Integer> norwegerDomain = new HashSet<Integer>();
		Set<Integer> milchDomain = new HashSet<Integer>();

		for (int i = 1; i < 6; i++) {
			fullDomain.add(i);
			// if (i < 5)
			// gruenDomain.add(i);
			// if (i > 1) {
			// blauWeissDomain.add(i);
			// }
		}
		Set<Integer> rotSet = new HashSet<Integer>();
		Set<Integer> blauSet = new HashSet<Integer>();
		Set<Integer> gelbSet = new HashSet<Integer>();
		Set<Integer> gruenSet = new HashSet<Integer>();
		Set<Integer> weissSet = new HashSet<Integer>();

		gelbSet.add(1);
		blauSet.add(2);
		rotSet.add(3);;
		gruenSet.add(4);
		gruenSet.add(5);
		weissSet.add(4);
		weissSet.add(5);
		
		norwegerDomain.add(1);
		milchDomain.add(3);

		
//		Vertex rot = new Vertex("1", "rot", rotSet);
//		graphOrig.addVertex(rot);
//		Vertex blau = new Vertex("2", "blau", fullDomain);
//		graphOrig.addVertex(blau);
//		Vertex weiss = new Vertex("3", "weiss", fullDomain);
//		graphOrig.addVertex(weiss);
//		Vertex gelb = new Vertex("4", "gelb", fullDomain);
//		graphOrig.addVertex(gelb);
//		Vertex gruen = new Vertex("5", "gruen", fullDomain);
//		graphOrig.addVertex(gruen);
//
//		Vertex brite = new Vertex("6", "brite", fullDomain);
//		graphOrig.addVertex(brite);
//		Vertex norweger = new Vertex("7", "norweger", norwegerDomain);
//		graphOrig.addVertex(norweger);
//		Vertex schwede = new Vertex("8", "schwede", fullDomain);
//		graphOrig.addVertex(schwede);
//		Vertex daene = new Vertex("9", "daene", fullDomain);
//		graphOrig.addVertex(daene);
//		Vertex deutscher = new Vertex("10", "deutscher", fullDomain);
//		graphOrig.addVertex(deutscher);
//
//		Vertex hund = new Vertex("11", "hund", fullDomain);
//		graphOrig.addVertex(hund);
//		Vertex katze = new Vertex("12", "katze", fullDomain);
//		graphOrig.addVertex(katze);
//		Vertex vogel = new Vertex("13", "vogel", fullDomain);
//		graphOrig.addVertex(vogel);
//		Vertex pferd = new Vertex("14", "pferd", fullDomain);
//		graphOrig.addVertex(pferd);
//		Vertex fisch = new Vertex("15", "fisch", fullDomain);
//		graphOrig.addVertex(fisch);
//
//		Vertex kaffee = new Vertex("16", "kaffee", fullDomain);
//		graphOrig.addVertex(kaffee);
//		Vertex tee = new Vertex("17", "tee", fullDomain);
//		graphOrig.addVertex(tee);
//		Vertex milch = new Vertex("18", "milch", milchDomain);
//		graphOrig.addVertex(milch);
//		Vertex wasser = new Vertex("19", "wasser", fullDomain);
//		graphOrig.addVertex(wasser);
//		Vertex bier = new Vertex("20", "bier", fullDomain);
//		graphOrig.addVertex(bier);
//
//		Vertex dunhill = new Vertex("21", "dunhill", fullDomain);
//		graphOrig.addVertex(dunhill);
//		Vertex malboro = new Vertex("22", "malboro", fullDomain);
//		graphOrig.addVertex(malboro);
//		Vertex winfield = new Vertex("23", "winfield", fullDomain);
//		graphOrig.addVertex(winfield);
//		Vertex pallmall = new Vertex("24", "pallmall", fullDomain);
//		graphOrig.addVertex(pallmall);
//		Vertex rothmanns = new Vertex("25", "rothmanns", fullDomain);
//		graphOrig.addVertex(rothmanns);

		
		
		Vertex rot = new Vertex(1, "rot", rotSet);
		graphOrig.addVertex(rot);
		Vertex blau = new Vertex(2, "blau", blauSet);
		graphOrig.addVertex(blau);
		Vertex weiss = new Vertex(3, "weiss", weissSet);
		graphOrig.addVertex(weiss);
		Vertex gelb = new Vertex(4, "gelb", gelbSet);
		graphOrig.addVertex(gelb);
		Vertex gruen = new Vertex(5, "gruen", gruenSet);
		graphOrig.addVertex(gruen);

		Vertex brite = new Vertex(6, "brite", rotSet);
		graphOrig.addVertex(brite);
		Vertex norweger = new Vertex(7, "norweger", norwegerDomain);
		graphOrig.addVertex(norweger);
		Vertex schwede = new Vertex(8, "schwede", weissSet);
		graphOrig.addVertex(schwede);
		Vertex daene = new Vertex(9, "daene", blauSet);
		graphOrig.addVertex(daene);
		Vertex deutscher = new Vertex(10, "deutscher", gruenSet);
		graphOrig.addVertex(deutscher);

		Vertex hund = new Vertex(11, "hund", weissSet);
		graphOrig.addVertex(hund);
		Vertex katze = new Vertex(12, "katze", gelbSet);
		graphOrig.addVertex(katze);
		Vertex vogel = new Vertex(13, "vogel", rotSet);
		graphOrig.addVertex(vogel);
		Vertex pferd = new Vertex(14, "pferd", blauSet);
		graphOrig.addVertex(pferd);
		Vertex fisch = new Vertex(15, "fisch", gruenSet);
		graphOrig.addVertex(fisch);

		Vertex kaffee = new Vertex(16, "kaffee", gruenSet);
		graphOrig.addVertex(kaffee);
		Vertex tee = new Vertex(17, "tee", blauSet);
		graphOrig.addVertex(tee);
		Vertex milch = new Vertex(18, "milch", milchDomain);
		graphOrig.addVertex(milch);
		Vertex wasser = new Vertex(19, "wasser", gelbSet);
		graphOrig.addVertex(wasser);
		Vertex bier = new Vertex(20, "bier", weissSet);
		graphOrig.addVertex(bier);

		Vertex dunhill = new Vertex(21, "dunhill", gelbSet);
		graphOrig.addVertex(dunhill);
		Vertex malboro = new Vertex(22, "malboro", blauSet);
		graphOrig.addVertex(malboro);
		Vertex winfield = new Vertex(23, "winfield", weissSet);
		graphOrig.addVertex(winfield);
		Vertex pallmall = new Vertex(24, "pallmall", rotSet);
		graphOrig.addVertex(pallmall);
		Vertex rothmanns = new Vertex(25, "rothmanns", gruenSet);
		graphOrig.addVertex(rothmanns);
		

		List<Constraint> allDiff = new ArrayList<Constraint>();
		List<Constraint> allDiffListAndNeighboorMinusOne = new ArrayList<Constraint>();
		List<Constraint> gleichList = new ArrayList<Constraint>();
		List<Constraint> neighboorPlusMinusOne = new ArrayList<Constraint>();
		
		allDiff.add(new AllDiffConstraint());
		
		allDiffListAndNeighboorMinusOne.add(new AllDiffConstraint());
		allDiffListAndNeighboorMinusOne.add(new NachbarMinusEinsConstraint());
		
		gleichList.add(new GleichConstraint());
		
		neighboorPlusMinusOne.add(new NachbarPlusMinusEinsConstraint());

		graphOrig.addEdge(rot, blau, new Edge<Vertex>(rot, blau, allDiff));
		graphOrig.addEdge(rot, weiss, new Edge<Vertex>(rot, weiss, allDiff));
		graphOrig.addEdge(rot, gelb, new Edge<Vertex>(rot, gelb, allDiff));
		graphOrig.addEdge(rot, gruen, new Edge<Vertex>(rot, gruen, allDiff));
		graphOrig.addEdge(blau, weiss, new Edge<Vertex>(blau, weiss, allDiff));
		graphOrig.addEdge(blau, gelb, new Edge<Vertex>(blau, gelb, allDiff));
		graphOrig.addEdge(blau, gruen, new Edge<Vertex>(blau, gruen, allDiff));
		graphOrig.addEdge(weiss, gelb, new Edge<Vertex>(weiss, gelb, allDiff));
		graphOrig.addEdge(gruen, weiss, new Edge<Vertex>(gruen, weiss, allDiffListAndNeighboorMinusOne));
		graphOrig.addEdge(gelb, gruen, new Edge<Vertex>(gelb, gruen, allDiff));

		graphOrig.addEdge(brite, norweger, new Edge<Vertex>(brite, norweger, allDiff));
		graphOrig.addEdge(brite, schwede, new Edge<Vertex>(brite, schwede, allDiff));
		graphOrig.addEdge(brite, daene, new Edge<Vertex>(brite, daene, allDiff));
		graphOrig.addEdge(brite, deutscher, new Edge<Vertex>(brite, deutscher, allDiff));
		graphOrig.addEdge(norweger, schwede, new Edge<Vertex>(norweger, schwede, allDiff));
		graphOrig.addEdge(norweger, daene, new Edge<Vertex>(norweger, daene, allDiff));
		graphOrig.addEdge(norweger, deutscher, new Edge<Vertex>(norweger, deutscher, allDiff));
		graphOrig.addEdge(schwede, daene, new Edge<Vertex>(schwede, daene, allDiff));
		graphOrig.addEdge(deutscher, schwede, new Edge<Vertex>(deutscher, schwede, allDiff));
		graphOrig.addEdge(daene, deutscher, new Edge<Vertex>(daene, deutscher, allDiff));
		
		graphOrig.addEdge(hund, katze, new Edge<Vertex>(hund, katze, allDiff));
		graphOrig.addEdge(hund, vogel, new Edge<Vertex>(hund, vogel, allDiff));
		graphOrig.addEdge(hund, pferd, new Edge<Vertex>(hund, pferd, allDiff));
		graphOrig.addEdge(fisch, hund, new Edge<Vertex>(fisch, hund, allDiff));
		graphOrig.addEdge(katze, vogel, new Edge<Vertex>(katze, vogel, allDiff));
		graphOrig.addEdge(katze, pferd, new Edge<Vertex>(katze, pferd, allDiff));
		graphOrig.addEdge(katze, fisch, new Edge<Vertex>(katze, fisch, allDiff));
		graphOrig.addEdge(vogel, pferd, new Edge<Vertex>(vogel, pferd, allDiff));
		graphOrig.addEdge(vogel, fisch, new Edge<Vertex>(vogel, fisch, allDiff));
		graphOrig.addEdge(pferd, fisch, new Edge<Vertex>(pferd, fisch, allDiff));

		graphOrig.addEdge(kaffee, tee, new Edge<Vertex>(kaffee, tee, allDiff));
		graphOrig.addEdge(kaffee, milch, new Edge<Vertex>(kaffee, milch, allDiff));
		graphOrig.addEdge(kaffee, wasser, new Edge<Vertex>(kaffee, wasser, allDiff));
		graphOrig.addEdge(bier, kaffee, new Edge<Vertex>(bier, kaffee, allDiff));
		graphOrig.addEdge(tee, milch, new Edge<Vertex>(tee, milch, allDiff));
		graphOrig.addEdge(tee, wasser, new Edge<Vertex>(tee, wasser, allDiff));
		graphOrig.addEdge(tee, bier, new Edge<Vertex>(tee, bier, allDiff));
		graphOrig.addEdge(milch, wasser, new Edge<Vertex>(milch, wasser, allDiff));
		graphOrig.addEdge(milch, bier, new Edge<Vertex>(milch, bier, allDiff));
		graphOrig.addEdge(wasser, bier, new Edge<Vertex>(wasser, bier, allDiff));

		graphOrig.addEdge(dunhill, malboro, new Edge<Vertex>(dunhill, malboro, allDiff));
		graphOrig.addEdge(dunhill, winfield, new Edge<Vertex>(dunhill, winfield, allDiff));
		graphOrig.addEdge(dunhill, pallmall, new Edge<Vertex>(dunhill, pallmall, allDiff));
		graphOrig.addEdge(dunhill, rothmanns, new Edge<Vertex>(dunhill, rothmanns, allDiff));
		graphOrig.addEdge(malboro, winfield, new Edge<Vertex>(malboro, winfield, allDiff));
		graphOrig.addEdge(malboro, pallmall, new Edge<Vertex>(malboro, pallmall, allDiff));
		graphOrig.addEdge(malboro, rothmanns, new Edge<Vertex>(malboro, rothmanns, allDiff));
		graphOrig.addEdge(winfield, pallmall, new Edge<Vertex>(winfield, pallmall, allDiff));
		graphOrig.addEdge(rothmanns, winfield, new Edge<Vertex>(rothmanns, winfield, allDiff));
		graphOrig.addEdge(pallmall, rothmanns, new Edge<Vertex>(pallmall, rothmanns, allDiff));
		
		graphOrig.addEdge(rot, brite, new Edge<Vertex>(rot, brite, gleichList));
		graphOrig.addEdge(gelb, dunhill, new Edge<Vertex>(gelb, dunhill, gleichList));
		graphOrig.addEdge(hund, schwede, new Edge<Vertex>(hund, schwede, gleichList));
		graphOrig.addEdge(rothmanns, deutscher, new Edge<Vertex>(rothmanns, deutscher, gleichList));
		graphOrig.addEdge(kaffee, gruen, new Edge<Vertex>(kaffee, gruen, gleichList));
		graphOrig.addEdge(daene, tee, new Edge<Vertex>(daene, tee, gleichList));
		graphOrig.addEdge(winfield, bier, new Edge<Vertex>(winfield, bier, gleichList));
		graphOrig.addEdge(vogel, pallmall, new Edge<Vertex>(vogel, pallmall, gleichList));
		
		graphOrig.addEdge(norweger, blau, new Edge<Vertex>(norweger, blau, neighboorPlusMinusOne));
		graphOrig.addEdge(malboro, wasser, new Edge<Vertex>(malboro, wasser, neighboorPlusMinusOne));
		graphOrig.addEdge(dunhill, pferd, new Edge<Vertex>(dunhill, pferd, neighboorPlusMinusOne));
		graphOrig.addEdge(malboro, katze, new Edge<Vertex>(malboro, katze, neighboorPlusMinusOne));

		System.out.println(graphOrig.toString());

		CSolver solver = new CSolver();
		
		Vertex assumptionVertex = null;
		int assumptionValue = 0;
		List<Integer> assumptionValueList = null;
		int vertexCounter = 0;
		
		List<Vertex> vList = new ArrayList<Vertex>(graphOrig.vertexSet());
		Collections.sort(vList);
		
		for(Vertex item : vList){
			System.out.println(item.getName());
		}

		assumptionVertex = vList.get(vertexCounter);
		assumptionValueList = new ArrayList<Integer>(assumptionVertex.getDomain());
		assumptionValue = assumptionValueList.get(0);
		assumptionValueList.remove(0);
		vertexCounter++;

//		System.out.println(graphOrig.edgesOf(assumptionVertex));
		
		Map<String, Integer> solutionMap = solver.solve(graphOrig, vList, assumptionVertex, assumptionValueList,
				assumptionValue, vertexCounter);

		System.out.println("\n***SOLUTION***");

		List<String> eins = new ArrayList<String>();
		List<String> zwei = new ArrayList<String>();
		List<String> drei = new ArrayList<String>();
		List<String> vier = new ArrayList<String>();
		List<String> fuenf = new ArrayList<String>();
		
		
		for (Entry<String, Integer> item : solutionMap.entrySet()) {
			if(item.getValue() == 1)
				eins.add(item.getKey());
			else if(item.getValue() == 2)
				zwei.add(item.getKey());
			else if(item.getValue() == 3)
				drei.add(item.getKey());
			else if(item.getValue() == 4)
				vier.add(item.getKey());
			else if(item.getValue() == 5)
				fuenf.add(item.getKey());
			
//			System.out.println(item.getKey() + " mit dem Wert: " + item.getValue());
		}

		System.out.println(Arrays.deepToString(eins.toArray()));
		System.out.println(Arrays.deepToString(zwei.toArray()));
		System.out.println(Arrays.deepToString(drei.toArray()));
		System.out.println(Arrays.deepToString(vier.toArray()));
		System.out.println(Arrays.deepToString(fuenf.toArray()));
	}

}
