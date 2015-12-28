package problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.SimpleGraph;

import constraint.AllDiffConstraint;
import constraint.BinaryConstraint;
import constraint.EinsteinUnaryConstraint;
import constraint.EqualConstraint;
import constraint.NeighboorMinusOneConstraint;
import constraint.NeighboorPlusMinusOneConstraint;
import constraint.UnaryConstraint;
import datastructs.Edge;
import datastructs.Vertex;
import solver.CSolver;

public class Einstein {

	public static void main(String[] args) {
		@SuppressWarnings("unchecked")
		UndirectedGraph<Vertex, Edge<Vertex>> graphOrig = new SimpleGraph<Vertex, Edge<Vertex>>(
				(Class<? extends Edge<Vertex>>) Edge.class);

		// Domaenen generieren
		Set<Integer> norwegianDomain = new HashSet<Integer>();
		Set<Integer> milkDomain = new HashSet<Integer>();

		Set<Integer> colorDomain = new HashSet<Integer>();
		Set<Integer> nationalityDomain = new HashSet<Integer>();
		Set<Integer> animalDomain = new HashSet<Integer>();
		Set<Integer> drinkDomain = new HashSet<Integer>();
		Set<Integer> cigaretteDomain = new HashSet<Integer>();

		for (int i = 1; i < 6; i++) {
			colorDomain.add(i);
			nationalityDomain.add(i);
			animalDomain.add(i);
			drinkDomain.add(i);
			cigaretteDomain.add(i);
		}

		norwegianDomain.add(1);
		milkDomain.add(3);

		// Knoten erzeugen
		Vertex rot = new Vertex(1, "rot", colorDomain);
		graphOrig.addVertex(rot);
		Vertex blau = new Vertex(2, "blau", colorDomain);
		graphOrig.addVertex(blau);
		Vertex weiss = new Vertex(3, "weiss", colorDomain);
		graphOrig.addVertex(weiss);
		Vertex gelb = new Vertex(4, "gelb", colorDomain);
		graphOrig.addVertex(gelb);
		Vertex gruen = new Vertex(5, "gruen", colorDomain);
		graphOrig.addVertex(gruen);

		Vertex brite = new Vertex(6, "brite", nationalityDomain);
		graphOrig.addVertex(brite);
//		Vertex norweger = new Vertex(7, "norweger", norwegianDomain);
		Vertex norweger = new Vertex(7, "norweger", nationalityDomain);
		graphOrig.addVertex(norweger);
		Vertex schwede = new Vertex(8, "schwede", nationalityDomain);
		graphOrig.addVertex(schwede);
		Vertex daene = new Vertex(9, "daene", nationalityDomain);
		graphOrig.addVertex(daene);
		Vertex deutscher = new Vertex(10, "deutscher", nationalityDomain);
		graphOrig.addVertex(deutscher);

		Vertex hund = new Vertex(11, "hund", animalDomain);
		graphOrig.addVertex(hund);
		Vertex katze = new Vertex(12, "katze", animalDomain);
		graphOrig.addVertex(katze);
		Vertex vogel = new Vertex(13, "vogel", animalDomain);
		graphOrig.addVertex(vogel);
		Vertex pferd = new Vertex(14, "pferd", animalDomain);
		graphOrig.addVertex(pferd);
		Vertex fisch = new Vertex(15, "fisch", animalDomain);
		graphOrig.addVertex(fisch);

		Vertex kaffee = new Vertex(16, "kaffee", drinkDomain);
		graphOrig.addVertex(kaffee);
		Vertex tee = new Vertex(17, "tee", drinkDomain);
		graphOrig.addVertex(tee);
//		Vertex milch = new Vertex(18, "milch", milkDomain);
		Vertex milch = new Vertex(18, "milch", drinkDomain);
		graphOrig.addVertex(milch);
		Vertex wasser = new Vertex(19, "wasser", drinkDomain);
		graphOrig.addVertex(wasser);
		Vertex bier = new Vertex(20, "bier", drinkDomain);
		graphOrig.addVertex(bier);

		Vertex dunhill = new Vertex(21, "dunhill", cigaretteDomain);
		graphOrig.addVertex(dunhill);
		Vertex malboro = new Vertex(22, "malboro", cigaretteDomain);
		graphOrig.addVertex(malboro);
		Vertex winfield = new Vertex(23, "winfield", cigaretteDomain);
		graphOrig.addVertex(winfield);
		Vertex pallmall = new Vertex(24, "pallmall", cigaretteDomain);
		graphOrig.addVertex(pallmall);
		Vertex rothmanns = new Vertex(25, "rothmanns", cigaretteDomain);
		graphOrig.addVertex(rothmanns);

		// Constraint-Listen erzeugen
		List<BinaryConstraint> allDiff = new ArrayList<BinaryConstraint>();
		List<BinaryConstraint> allDiffAndNeighboorMinusOneList = new ArrayList<BinaryConstraint>();
		List<BinaryConstraint> equalList = new ArrayList<BinaryConstraint>();
		List<BinaryConstraint> neighboorPlusMinusOneList = new ArrayList<BinaryConstraint>();
		
		List<UnaryConstraint> einsteinList = new ArrayList<UnaryConstraint>();

		allDiff.add(new AllDiffConstraint());

		allDiffAndNeighboorMinusOneList.add(new AllDiffConstraint());
		allDiffAndNeighboorMinusOneList.add(new NeighboorMinusOneConstraint());

		equalList.add(new EqualConstraint());

		neighboorPlusMinusOneList.add(new NeighboorPlusMinusOneConstraint());
		
		einsteinList.add(new EinsteinUnaryConstraint());
		
		norweger.setUnaryConstraintList(einsteinList);
		milch.setUnaryConstraintList(einsteinList);

		// Kanten hinzufuegen mit den jeweiligen Constraint-Listen
		graphOrig.addEdge(rot, blau, new Edge<Vertex>(rot, blau, allDiff));
		graphOrig.addEdge(rot, weiss, new Edge<Vertex>(rot, weiss, allDiff));
		graphOrig.addEdge(rot, gelb, new Edge<Vertex>(rot, gelb, allDiff));
		graphOrig.addEdge(rot, gruen, new Edge<Vertex>(rot, gruen, allDiff));
		graphOrig.addEdge(blau, weiss, new Edge<Vertex>(blau, weiss, allDiff));
		graphOrig.addEdge(blau, gelb, new Edge<Vertex>(blau, gelb, allDiff));
		graphOrig.addEdge(blau, gruen, new Edge<Vertex>(blau, gruen, allDiff));
		graphOrig.addEdge(weiss, gelb, new Edge<Vertex>(weiss, gelb, allDiff));
		graphOrig.addEdge(gruen, weiss, new Edge<Vertex>(gruen, weiss, allDiffAndNeighboorMinusOneList));
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

		graphOrig.addEdge(rot, brite, new Edge<Vertex>(rot, brite, equalList));
		graphOrig.addEdge(gelb, dunhill, new Edge<Vertex>(gelb, dunhill, equalList));
		graphOrig.addEdge(hund, schwede, new Edge<Vertex>(hund, schwede, equalList));
		graphOrig.addEdge(rothmanns, deutscher, new Edge<Vertex>(rothmanns, deutscher, equalList));
		graphOrig.addEdge(kaffee, gruen, new Edge<Vertex>(kaffee, gruen, equalList));
		graphOrig.addEdge(daene, tee, new Edge<Vertex>(daene, tee, equalList));
		graphOrig.addEdge(winfield, bier, new Edge<Vertex>(winfield, bier, equalList));
		graphOrig.addEdge(vogel, pallmall, new Edge<Vertex>(vogel, pallmall, equalList));

		graphOrig.addEdge(norweger, blau, new Edge<Vertex>(norweger, blau, neighboorPlusMinusOneList));
		graphOrig.addEdge(malboro, wasser, new Edge<Vertex>(malboro, wasser, neighboorPlusMinusOneList));
		graphOrig.addEdge(dunhill, pferd, new Edge<Vertex>(dunhill, pferd, neighboorPlusMinusOneList));
		graphOrig.addEdge(malboro, katze, new Edge<Vertex>(malboro, katze, neighboorPlusMinusOneList));

		CSolver solver = new CSolver();

		solver.solve(graphOrig);

		// Loesung formatieren
		List<String> one = new ArrayList<String>();
		List<String> two = new ArrayList<String>();
		List<String> three = new ArrayList<String>();
		List<String> four = new ArrayList<String>();
		List<String> five = new ArrayList<String>();

		for (Entry<String, Integer> item : solver.getSolutionMap().entrySet()) {
			if (item.getValue() == 1)
				one.add(item.getKey());
			else if (item.getValue() == 2)
				two.add(item.getKey());
			else if (item.getValue() == 3)
				three.add(item.getKey());
			else if (item.getValue() == 4)
				four.add(item.getKey());
			else if (item.getValue() == 5)
				five.add(item.getKey());
		}

		System.out.println("\n***SOLUTION***");
		System.out.println(Arrays.deepToString(one.toArray()));
		System.out.println(Arrays.deepToString(two.toArray()));
		System.out.println(Arrays.deepToString(three.toArray()));
		System.out.println(Arrays.deepToString(four.toArray()));
		System.out.println(Arrays.deepToString(five.toArray()));

		Map<String, Integer> rightSolution = generateMapWithRightSolution();

		// Loesung mit korrekter Loesung vergleichen
		System.out.println("\nSOLUTION is right? " + rightSolution.equals(solver.getSolutionMap()));
	}

	private static Map<String, Integer> generateMapWithRightSolution() {
		Map<String, Integer> rightSolution = new HashMap<String, Integer>();
		rightSolution.put("wasser", 1);
		rightSolution.put("norweger", 1);
		rightSolution.put("katze", 1);
		rightSolution.put("gelb", 1);
		rightSolution.put("dunhill", 1);

		rightSolution.put("malboro", 2);
		rightSolution.put("blau", 2);
		rightSolution.put("daene", 2);
		rightSolution.put("tee", 2);
		rightSolution.put("pferd", 2);

		rightSolution.put("milch", 3);
		rightSolution.put("vogel", 3);
		rightSolution.put("brite", 3);
		rightSolution.put("rot", 3);
		rightSolution.put("pallmall", 3);

		rightSolution.put("rothmanns", 4);
		rightSolution.put("deutscher", 4);
		rightSolution.put("fisch", 4);
		rightSolution.put("gruen", 4);
		rightSolution.put("kaffee", 4);

		rightSolution.put("winfield", 5);
		rightSolution.put("weiss", 5);
		rightSolution.put("bier", 5);
		rightSolution.put("schwede", 5);
		rightSolution.put("hund", 5);

		return rightSolution;
	}

}