package problem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import constraints.AllDiffConstraint;
import constraints.Constraint;
import constraints.GleichConstraint;
import constraints.UngleichConstraint;
import datastructs.Graph;
import datastructs.Vertex;
import solver.CSolver;

public class Einstein {

	public static void main(String[] args) {
		Graph graphOrig = new Graph();
		CSolver solver = new CSolver();

		Set<Integer> fullDomain = new HashSet<Integer>();
		Set<Integer> gruenDomain = new HashSet<Integer>();
		Set<Integer> weissDomain = new HashSet<Integer>();
		Set<Integer> norwegerDomain = new HashSet<Integer>();
		Set<Integer> milchDomain = new HashSet<Integer>();

		for (int i = 1; i < 6; i++) {
			fullDomain.add(i);
			if (i < 5)
				gruenDomain.add(i);
			if (i > 1)
				weissDomain.add(i);
		}
		norwegerDomain.add(1);
		milchDomain.add(3);

		Vertex rot = new Vertex("1", "rot", fullDomain);
		graphOrig.addVertex(rot, true);
		Vertex blau = new Vertex("2", "blau", fullDomain);
		graphOrig.addVertex(blau, true);
		Vertex weiss = new Vertex("3", "weiss", weissDomain);
		graphOrig.addVertex(weiss, true);
		Vertex gelb = new Vertex("4", "gelb", fullDomain);
		graphOrig.addVertex(gelb, true);
		Vertex gruen = new Vertex("5", "gruen", gruenDomain);
		graphOrig.addVertex(gruen, true);

		Vertex brite = new Vertex("6", "brite", fullDomain);
		graphOrig.addVertex(brite, true);
		Vertex norweger = new Vertex("7", "norweger", norwegerDomain);
		graphOrig.addVertex(norweger, true);
		Vertex schwede = new Vertex("8", "schwede", fullDomain);
		graphOrig.addVertex(schwede, true);
		Vertex daene = new Vertex("9", "daene", fullDomain);
		graphOrig.addVertex(daene, true);
		Vertex deutscher = new Vertex("10", "deutscher", fullDomain);
		graphOrig.addVertex(deutscher, true);

		Vertex hund = new Vertex("11", "hund", fullDomain);
		graphOrig.addVertex(hund, true);
		Vertex katze = new Vertex("12", "katze", fullDomain);
		graphOrig.addVertex(katze, true);
		Vertex vogel = new Vertex("13", "vogel", fullDomain);
		graphOrig.addVertex(vogel, true);
		Vertex pferd = new Vertex("14", "pferd", fullDomain);
		graphOrig.addVertex(pferd, true);
		Vertex fisch = new Vertex("15", "fisch", fullDomain);
		graphOrig.addVertex(fisch, true);

		Vertex kaffee = new Vertex("16", "kaffee", fullDomain);
		graphOrig.addVertex(kaffee, true);
		Vertex tee = new Vertex("17", "tee", fullDomain);
		graphOrig.addVertex(tee, true);
		Vertex milch = new Vertex("18", "milch", milchDomain);
		graphOrig.addVertex(milch, true);
		Vertex wasser = new Vertex("19", "wasser", fullDomain);
		graphOrig.addVertex(wasser, true);
		Vertex bier = new Vertex("20", "bier", fullDomain);
		graphOrig.addVertex(bier, true);

		Vertex dunhill = new Vertex("21", "dunhill", fullDomain);
		graphOrig.addVertex(dunhill, true);
		Vertex malboro = new Vertex("22", "malboro", fullDomain);
		graphOrig.addVertex(malboro, true);
		Vertex winfield = new Vertex("23", "winfield", fullDomain);
		graphOrig.addVertex(winfield, true);
		Vertex pallmall = new Vertex("24", "pallmall", fullDomain);
		graphOrig.addVertex(pallmall, true);
		Vertex rothmanns = new Vertex("25", "rothmanns", fullDomain);
		graphOrig.addVertex(rothmanns, true);

		List<Constraint> allDiffList = new ArrayList<Constraint>();
		List<Constraint> ungleichList = new ArrayList<Constraint>();
		List<Constraint> gleichList = new ArrayList<Constraint>();
		
		allDiffList.add(new AllDiffConstraint("Alldifferent"));
		ungleichList.add(new UngleichConstraint("Ungleich"));
		gleichList.add(new GleichConstraint("Gleich"));

		graphOrig.addEdge(rot, blau, allDiffList);
		graphOrig.addEdge(rot, gelb, allDiffList);
		graphOrig.addEdge(rot, weiss, allDiffList);
		graphOrig.addEdge(rot, gruen, allDiffList);
		graphOrig.addEdge(blau, gelb, allDiffList);
		graphOrig.addEdge(blau, weiss, allDiffList);
		graphOrig.addEdge(blau, gruen, allDiffList);
		graphOrig.addEdge(gelb, weiss, allDiffList);
		graphOrig.addEdge(gelb, gruen, allDiffList);
		graphOrig.addEdge(weiss, gruen, allDiffList);
		
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
		graphOrig.addEdge(hund, schwede, gleichList);
		graphOrig.addEdge(deutscher, rothmanns, gleichList);
		graphOrig.addEdge(gruen, kaffee, gleichList);
		graphOrig.addEdge(tee, daene, gleichList);
		graphOrig.addEdge(bier, winfield, gleichList);
		graphOrig.addEdge(vogel, pallmall, gleichList);
		
		//ungleich constraints anlegen..
		
	}

}
