package problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.event.ListSelectionEvent;

import java.util.Set;

import constraints.AllDiffConstraint;
import constraints.BinaryConstraint;
import constraints.GleichConstraint;
import constraints.NachbarMinusEinsConstraint;
import constraints.UngleichConstraint;
import datastructs.Graph;
import datastructs.Vertex;
import solver.CSolver;

/**
 * Lösung: 1.In the yellow house the Norwegian drinks water, smokes Dunhills,
 * and keeps cats. 2. In the blue house the Dane drinks tea, smokes Blends, and
 * keeps horses. 3. In the red house the Englishman drinks milk, smokes
 * PallMalls, and keeps birds. 4. In the green house the German drinks coffee,
 * smokes Princes, and keeps fish. 5. In the white house the Swede drinks bier,
 * smokes BlueMasters, and keeps dogs.
 * 
 * https://web.stanford.edu/~laurik/fsmbook/examples/Einstein'sPuzzle.html
 */
public class Einstein {

	public static void main(String[] args) {
		Graph graphOrig = new Graph();
		CSolver solver = new CSolver();

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
//		graphOrig.addVertex(rot, true);
//		Vertex blau = new Vertex("2", "blau", fullDomain);
//		graphOrig.addVertex(blau, true);
//		Vertex weiss = new Vertex("3", "weiss", fullDomain);
//		graphOrig.addVertex(weiss, true);
//		Vertex gelb = new Vertex("4", "gelb", fullDomain);
//		graphOrig.addVertex(gelb, true);
//		Vertex gruen = new Vertex("5", "gruen", fullDomain);
//		graphOrig.addVertex(gruen, true);
//
//		Vertex brite = new Vertex("6", "brite", fullDomain);
//		graphOrig.addVertex(brite, true);
//		Vertex norweger = new Vertex("7", "norweger", norwegerDomain);
//		graphOrig.addVertex(norweger, true);
//		Vertex schwede = new Vertex("8", "schwede", fullDomain);
//		graphOrig.addVertex(schwede, true);
//		Vertex daene = new Vertex("9", "daene", fullDomain);
//		graphOrig.addVertex(daene, true);
//		Vertex deutscher = new Vertex("10", "deutscher", fullDomain);
//		graphOrig.addVertex(deutscher, true);
//
//		Vertex hund = new Vertex("11", "hund", fullDomain);
//		graphOrig.addVertex(hund, true);
//		Vertex katze = new Vertex("12", "katze", fullDomain);
//		graphOrig.addVertex(katze, true);
//		Vertex vogel = new Vertex("13", "vogel", fullDomain);
//		graphOrig.addVertex(vogel, true);
//		Vertex pferd = new Vertex("14", "pferd", fullDomain);
//		graphOrig.addVertex(pferd, true);
//		Vertex fisch = new Vertex("15", "fisch", fullDomain);
//		graphOrig.addVertex(fisch, true);
//
//		Vertex kaffee = new Vertex("16", "kaffee", fullDomain);
//		graphOrig.addVertex(kaffee, true);
//		Vertex tee = new Vertex("17", "tee", fullDomain);
//		graphOrig.addVertex(tee, true);
//		Vertex milch = new Vertex("18", "milch", milchDomain);
//		graphOrig.addVertex(milch, true);
//		Vertex wasser = new Vertex("19", "wasser", fullDomain);
//		graphOrig.addVertex(wasser, true);
//		Vertex bier = new Vertex("20", "bier", fullDomain);
//		graphOrig.addVertex(bier, true);
//
//		Vertex dunhill = new Vertex("21", "dunhill", fullDomain);
//		graphOrig.addVertex(dunhill, true);
//		Vertex malboro = new Vertex("22", "malboro", fullDomain);
//		graphOrig.addVertex(malboro, true);
//		Vertex winfield = new Vertex("23", "winfield", fullDomain);
//		graphOrig.addVertex(winfield, true);
//		Vertex pallmall = new Vertex("24", "pallmall", fullDomain);
//		graphOrig.addVertex(pallmall, true);
//		Vertex rothmanns = new Vertex("25", "rothmanns", fullDomain);
//		graphOrig.addVertex(rothmanns, true);

		
		
		Vertex rot = new Vertex("1", "rot", rotSet);
		graphOrig.addVertex(rot, true);
		Vertex blau = new Vertex("2", "blau", blauSet);
		graphOrig.addVertex(blau, true);
		Vertex weiss = new Vertex("3", "weiss", weissSet);
		graphOrig.addVertex(weiss, true);
		Vertex gelb = new Vertex("4", "gelb", gelbSet);
		graphOrig.addVertex(gelb, true);
		Vertex gruen = new Vertex("5", "gruen", gruenSet);
		graphOrig.addVertex(gruen, true);

		Vertex brite = new Vertex("6", "brite", rotSet);
		graphOrig.addVertex(brite, true);
		Vertex norweger = new Vertex("7", "norweger", norwegerDomain);
		graphOrig.addVertex(norweger, true);
		Vertex schwede = new Vertex("8", "schwede", weissSet);
		graphOrig.addVertex(schwede, true);
		Vertex daene = new Vertex("9", "daene", blauSet);
		graphOrig.addVertex(daene, true);
		Vertex deutscher = new Vertex("10", "deutscher", gruenSet);
		graphOrig.addVertex(deutscher, true);

		Vertex hund = new Vertex("11", "hund", weissSet);
		graphOrig.addVertex(hund, true);
		Vertex katze = new Vertex("12", "katze", gelbSet);
		graphOrig.addVertex(katze, true);
		Vertex vogel = new Vertex("13", "vogel", rotSet);
		graphOrig.addVertex(vogel, true);
		Vertex pferd = new Vertex("14", "pferd", blauSet);
		graphOrig.addVertex(pferd, true);
		Vertex fisch = new Vertex("15", "fisch", gruenSet);
		graphOrig.addVertex(fisch, true);

		Vertex kaffee = new Vertex("16", "kaffee", gruenSet);
		graphOrig.addVertex(kaffee, true);
		Vertex tee = new Vertex("17", "tee", blauSet);
		graphOrig.addVertex(tee, true);
		Vertex milch = new Vertex("18", "milch", milchDomain);
		graphOrig.addVertex(milch, true);
		Vertex wasser = new Vertex("19", "wasser", gelbSet);
		graphOrig.addVertex(wasser, true);
		Vertex bier = new Vertex("20", "bier", weissSet);
		graphOrig.addVertex(bier, true);

		Vertex dunhill = new Vertex("21", "dunhill", gelbSet);
		graphOrig.addVertex(dunhill, true);
		Vertex malboro = new Vertex("22", "malboro", blauSet);
		graphOrig.addVertex(malboro, true);
		Vertex winfield = new Vertex("23", "winfield", weissSet);
		graphOrig.addVertex(winfield, true);
		Vertex pallmall = new Vertex("24", "pallmall", rotSet);
		graphOrig.addVertex(pallmall, true);
		Vertex rothmanns = new Vertex("25", "rothmanns", gruenSet);
		graphOrig.addVertex(rothmanns, true);
		
		
		List<BinaryConstraint> allDiffList = new ArrayList<BinaryConstraint>();
		List<BinaryConstraint> allDiffListAndNeighboorMinusOne = new ArrayList<BinaryConstraint>();
//		List<BinaryConstraint> ungleichList = new ArrayList<BinaryConstraint>();
		List<BinaryConstraint> gleichList = new ArrayList<BinaryConstraint>();
		List<BinaryConstraint> neighboorPlusMinusOne = new ArrayList<BinaryConstraint>();

		allDiffList.add(new AllDiffConstraint("Alldifferent"));
//		ungleichList.add(new UngleichConstraint("Ungleich"));
		gleichList.add(new GleichConstraint("Gleich"));

		allDiffListAndNeighboorMinusOne.add(new AllDiffConstraint("Alldifferent"));
		allDiffListAndNeighboorMinusOne.add(new NachbarMinusEinsConstraint("NachbarMinusEins"));

		neighboorPlusMinusOne.add(new NachbarMinusEinsConstraint("NachbarMinusEins"));

		graphOrig.addEdge(rot, blau, allDiffList);
		graphOrig.addEdge(rot, gelb, allDiffList);
		graphOrig.addEdge(rot, weiss, allDiffList);
		graphOrig.addEdge(rot, gruen, allDiffList);
		graphOrig.addEdge(blau, gelb, allDiffList);
		graphOrig.addEdge(blau, weiss, allDiffList);
		graphOrig.addEdge(blau, gruen, allDiffList);
		graphOrig.addEdge(gelb, weiss, allDiffList);
		graphOrig.addEdge(gelb, gruen, allDiffList);
		// TODO: ggf. weiss und gruen tauschen..
		graphOrig.addEdge(gruen, weiss, allDiffListAndNeighboorMinusOne);

		graphOrig.addEdge(brite, norweger, allDiffList);
		graphOrig.addEdge(brite, daene, allDiffList);
		graphOrig.addEdge(brite, schwede, allDiffList);
		graphOrig.addEdge(brite, deutscher, allDiffList);
		graphOrig.addEdge(norweger, daene, allDiffList);
		graphOrig.addEdge(norweger, schwede, allDiffList);
		graphOrig.addEdge(norweger, deutscher, allDiffList);
		graphOrig.addEdge(daene, schwede, allDiffList);
		graphOrig.addEdge(daene, deutscher, allDiffList);
		graphOrig.addEdge(schwede, deutscher, allDiffList);

		graphOrig.addEdge(hund, katze, allDiffList);
		graphOrig.addEdge(hund, vogel, allDiffList);
		graphOrig.addEdge(hund, pferd, allDiffList);
		graphOrig.addEdge(hund, fisch, allDiffList);
		graphOrig.addEdge(katze, vogel, allDiffList);
		graphOrig.addEdge(katze, pferd, allDiffList);
		graphOrig.addEdge(katze, fisch, allDiffList);
		graphOrig.addEdge(vogel, pferd, allDiffList);
		graphOrig.addEdge(vogel, fisch, allDiffList);
		graphOrig.addEdge(pferd, fisch, allDiffList);

		graphOrig.addEdge(kaffee, tee, allDiffList);
		graphOrig.addEdge(kaffee, milch, allDiffList);
		graphOrig.addEdge(kaffee, wasser, allDiffList);
		graphOrig.addEdge(kaffee, bier, allDiffList);
		graphOrig.addEdge(tee, milch, allDiffList);
		graphOrig.addEdge(tee, wasser, allDiffList);
		graphOrig.addEdge(tee, bier, allDiffList);
		graphOrig.addEdge(milch, wasser, allDiffList);
		graphOrig.addEdge(milch, bier, allDiffList);
		graphOrig.addEdge(wasser, bier, allDiffList);

		graphOrig.addEdge(malboro, winfield, allDiffList);
		graphOrig.addEdge(malboro, pallmall, allDiffList);
		graphOrig.addEdge(malboro, dunhill, allDiffList);
		graphOrig.addEdge(malboro, rothmanns, allDiffList);
		graphOrig.addEdge(winfield, pallmall, allDiffList);
		graphOrig.addEdge(winfield, dunhill, allDiffList);
		graphOrig.addEdge(winfield, rothmanns, allDiffList);
		graphOrig.addEdge(pallmall, dunhill, allDiffList);
		graphOrig.addEdge(pallmall, rothmanns, allDiffList);
		graphOrig.addEdge(dunhill, rothmanns, allDiffList);

		graphOrig.addEdge(rot, brite, gleichList);
		graphOrig.addEdge(gelb, dunhill, gleichList);
		graphOrig.addEdge(schwede, hund, gleichList);
		graphOrig.addEdge(deutscher, rothmanns, gleichList);
		graphOrig.addEdge(gruen, kaffee, gleichList);
		graphOrig.addEdge(daene, tee, gleichList);
		graphOrig.addEdge(bier, winfield, gleichList);
		graphOrig.addEdge(vogel, pallmall, gleichList);

		graphOrig.addEdge(norweger, blau, neighboorPlusMinusOne);
		graphOrig.addEdge(malboro, wasser, neighboorPlusMinusOne);
		graphOrig.addEdge(pferd, dunhill, neighboorPlusMinusOne);
		graphOrig.addEdge(malboro, katze, neighboorPlusMinusOne);
//
//		graphOrig.addEdge(norweger, blau, ungleichList);
//		graphOrig.addEdge(wasser, malboro, ungleichList);
//		graphOrig.addEdge(pferd, dunhill, ungleichList);
//		graphOrig.addEdge(katze, malboro, ungleichList);

		Vertex assumptionVertex = null;
		int assumptionValue = 0;
		List<Integer> assumptionValueList = null;
		int vertexCounter = 1;

		assumptionVertex = graphOrig.getVertex(String.valueOf(vertexCounter));
		assumptionValueList = new ArrayList<Integer>(graphOrig.getVertex(String.valueOf(vertexCounter++)).getDomain());
		assumptionValue = assumptionValueList.get(0);
		assumptionValueList.remove(0);

		Map<String, Integer> solutionMap = solver.solve(graphOrig, assumptionVertex, assumptionValueList,
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
