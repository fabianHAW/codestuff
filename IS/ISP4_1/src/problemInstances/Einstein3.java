package problemInstances;

import java.util.ArrayList;
import java.util.HashSet;

import constsraintnet.Constraint;
import constsraintnet.ConstraintNet;
import constsraintnet.DifferentConstraint;
import constsraintnet.Edge;
import constsraintnet.EqualConstraint;
import constsraintnet.EquiConstraint;
import constsraintnet.ImplicationNegativeConstraint;
import constsraintnet.Node;
import constsraintnet.Type;
import domains.Getraenk;
import domains.Hausfarbe;
import domains.Hausposition;
import domains.Haustier;
import domains.Nationalitaet;
import domains.Zigarettenmarke;

public class Einstein3 {
	public static ConstraintNet generateConstraintNet(){
		ArrayList<Type> domain = new ArrayList<Type>();
		domain.add(new Type(Hausposition.Eins));
		domain.add(new Type(Hausposition.Zwei));
		domain.add(new Type(Hausposition.Drei));
		domain.add(new Type(Hausposition.Vier));
		domain.add(new Type(Hausposition.Fuenf));
		
		Node n1 = new Node("Britisch", (ArrayList<Type>)domain.clone(), 1);
		Node n2 = new Node("Schwedisch", (ArrayList<Type>)domain.clone(), 2);
		Node n3 = new Node("Norwegisch", (ArrayList<Type>)domain.clone(), 3);
		Node n4 = new Node("Daenisch", (ArrayList<Type>)domain.clone(), 4);
		Node n5 = new Node("Deutsch", (ArrayList<Type>)domain.clone(), 5);
		
		Node n6 = new Node("Bier", (ArrayList<Type>)domain.clone(), 6);
		Node n7 = new Node("Milch", (ArrayList<Type>)domain.clone(), 7);
		Node n8 = new Node("Wasser", (ArrayList<Type>)domain.clone(), 8);
		Node n9 = new Node("Tee", (ArrayList<Type>)domain.clone(), 9);
		Node n10 = new Node("Kaffee", (ArrayList<Type>)domain.clone(), 10);
		
		Node n11 = new Node("Malboro", (ArrayList<Type>)domain.clone(), 11);
		Node n12 = new Node("Dunhill", (ArrayList<Type>)domain.clone(), 12);
		Node n13 = new Node("Pallmall", (ArrayList<Type>)domain.clone(), 13);
		Node n14 = new Node("Rothmanns", (ArrayList<Type>)domain.clone(), 14);
		Node n15 = new Node("Winfield", (ArrayList<Type>)domain.clone(), 15);
		
		Node n16 = new Node("Blau", (ArrayList<Type>)domain.clone(), 16);
		Node n17 = new Node("Gelb", (ArrayList<Type>)domain.clone(), 17);
		Node n18 = new Node("Gruen", (ArrayList<Type>)domain.clone(), 18);
		Node n19 = new Node("Rot", (ArrayList<Type>)domain.clone(), 19);
		Node n20 = new Node("Weiss", (ArrayList<Type>)domain.clone(), 20);
		
		Node n21 = new Node("Hund", (ArrayList<Type>)domain.clone(), 21);
		Node n22 = new Node("Katze", (ArrayList<Type>)domain.clone(), 22);
		Node n23 = new Node("Pferd", (ArrayList<Type>)domain.clone(), 23);
		Node n24 = new Node("Vogel", (ArrayList<Type>)domain.clone(), 24);
		Node n25 = new Node("Fisch", (ArrayList<Type>)domain.clone(), 25);
		
		Node n26 = new Node("Eins", (ArrayList<Type>)domain.clone(), 26);
		Node n27 = new Node("Zwei", (ArrayList<Type>)domain.clone(), 27);
		Node n28 = new Node("Drei", (ArrayList<Type>)domain.clone(), 28);
		Node n29 = new Node("Vier", (ArrayList<Type>)domain.clone(), 29);
		Node n30 = new Node("Fuenf", (ArrayList<Type>)domain.clone(), 30);
		
		DifferentConstraint different = new DifferentConstraint("Different Constraint", "", "");
		ArrayList<Constraint> constraint1 = new ArrayList<Constraint>();
		constraint1.add(different);
		ArrayList<Node> nodes = new ArrayList<Node>();
		nodes.add(n1);
		nodes.add(n2);
		nodes.add(n3);
		nodes.add(n4);
		nodes.add(n5);
		nodes.add(n6);
		nodes.add(n7);
		nodes.add(n8);
		nodes.add(n9);
		nodes.add(n10);
		nodes.add(n11);
		nodes.add(n12);
		nodes.add(n13);
		nodes.add(n14);
		nodes.add(n15);
		nodes.add(n16);
		nodes.add(n17);
		nodes.add(n18);
		nodes.add(n19);
		nodes.add(n20);
		nodes.add(n21);
		nodes.add(n22);
		nodes.add(n23);
		nodes.add(n24);
		nodes.add(n25);
		nodes.add(n26);
		nodes.add(n27);
		nodes.add(n28);
		nodes.add(n29);
		nodes.add(n30);
		
		
		ArrayList<Edge> edges = new ArrayList<Edge>();
		
		//Different-Constraint zwischen Nodes selber Gattung (z.B. Getränk, Tier,...)
		for(int i = 0; i < 30; i++){
			for(int j = 0; j < 30; j++){					
					if((i < 5 && j < 5 && i != j)
						|| (i > 4 && i < 10 && j > 4 && j < 10 && i != j)
						|| (i > 9 && i < 15 && j > 9 && j < 15 && i != j)
						|| (i > 14 && i < 20 && j > 14 && j < 20 && i != j)
						|| (i > 19 && i < 25 && j > 19 && j < 25 && i != j)
						|| (i > 24 && i < 30 && j > 24 && j < 30 && i != j)){
						edges.add(new Edge(nodes.get(i), 
								nodes.get(j), 
								(ArrayList<Constraint>)constraint1.clone()));
					}
//					if((i < 5 && j > 4 && j < 10)
//					    || (i > 4 && i < 10 && j > 14 && j < 20)){
//						System.out.println((i+1) + ", " + (j+1));
//					}
			}
		}
		
		
		
		EqualConstraint equaln = new EqualConstraint("equal", "", "");
		ArrayList<Constraint> constraint3 = new ArrayList<Constraint>();
		constraint3.add(equaln);
		edges.add(new Edge(n1, n19, (ArrayList<Constraint>)constraint3.clone()));
		edges.add(new Edge(n2, n21, (ArrayList<Constraint>)constraint3.clone()));
		edges.add(new Edge(n4, n9, (ArrayList<Constraint>)constraint3.clone()));
		edges.add(new Edge(n18, n10, (ArrayList<Constraint>)constraint3.clone()));
		edges.add(new Edge(n18, n29, (ArrayList<Constraint>)constraint3.clone()));
		edges.add(new Edge(n20, n30, (ArrayList<Constraint>)constraint3.clone()));
		edges.add(new Edge(n13, n24, (ArrayList<Constraint>)constraint3.clone()));
		edges.add(new Edge(n28, n7, (ArrayList<Constraint>)constraint3.clone()));
		edges.add(new Edge(n17, n12, (ArrayList<Constraint>)constraint3.clone()));
		edges.add(new Edge(n3, n26, (ArrayList<Constraint>)constraint3.clone()));
		edges.add(new Edge(n15, n6, (ArrayList<Constraint>)constraint3.clone()));
		edges.add(new Edge(n16, n27, (ArrayList<Constraint>)constraint3.clone()));
		edges.add(new Edge(n5, n14, (ArrayList<Constraint>)constraint3.clone()));

		edges.add(new Edge(n19, n1, (ArrayList<Constraint>)constraint3.clone()));
		edges.add(new Edge(n21, n2, (ArrayList<Constraint>)constraint3.clone()));
		edges.add(new Edge(n9, n4, (ArrayList<Constraint>)constraint3.clone()));
		edges.add(new Edge(n10, n18, (ArrayList<Constraint>)constraint3.clone()));
		edges.add(new Edge(n29, n18, (ArrayList<Constraint>)constraint3.clone()));
		edges.add(new Edge(n30, n20, (ArrayList<Constraint>)constraint3.clone()));
		edges.add(new Edge(n24, n13, (ArrayList<Constraint>)constraint3.clone()));
		edges.add(new Edge(n7, n28, (ArrayList<Constraint>)constraint3.clone()));
		edges.add(new Edge(n12, n17, (ArrayList<Constraint>)constraint3.clone()));
		edges.add(new Edge(n26, n3, (ArrayList<Constraint>)constraint3.clone()));
		edges.add(new Edge(n6, n15, (ArrayList<Constraint>)constraint3.clone()));
		edges.add(new Edge(n27, n16, (ArrayList<Constraint>)constraint3.clone()));
		edges.add(new Edge(n14, n5, (ArrayList<Constraint>)constraint3.clone()));

		
		edges.add(new Edge(n20, n26, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n20, n27, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n20, n28, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n26, n20, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n27, n20, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n28, n20, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n18, n26, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n18, n27, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n18, n30, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n26, n18, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n27, n18, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n30, n18, (ArrayList<Constraint>)constraint1.clone()));

		edges.add(new Edge(n4, n18, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n18, n4, (ArrayList<Constraint>)constraint1.clone()));

		edges.add(new Edge(n2, n13, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n13, n2, (ArrayList<Constraint>)constraint1.clone()));

		edges.add(new Edge(n28, n18, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n28, n4, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n18, n28, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n4, n28, (ArrayList<Constraint>)constraint1.clone()));
	
		edges.add(new Edge(n24, n17, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n2, n17, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n17, n24, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n17, n2, (ArrayList<Constraint>)constraint1.clone()));
	
		edges.add(new Edge(n3, n20, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n20, n3, (ArrayList<Constraint>)constraint1.clone()));

		edges.add(new Edge(n11, n22, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n22, n11, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n17, n11, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n11, n17, (ArrayList<Constraint>)constraint1.clone()));

		edges.add(new Edge(n12, n23, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n23, n12, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n17, n23, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n23, n17, (ArrayList<Constraint>)constraint1.clone()));

		edges.add(new Edge(n28, n6, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n6, n28, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n28, n15, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n15, n28, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n4, n15, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n15, n4, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n18, n15, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n15, n18, (ArrayList<Constraint>)constraint1.clone()));
			
		edges.add(new Edge(n3, n16, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n16, n3, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n3, n26, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n26, n3, (ArrayList<Constraint>)constraint1.clone()));
	
		edges.add(new Edge(n11, n8, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n8, n11, (ArrayList<Constraint>)constraint1.clone()));

		////////////////////weiter mit zweitem implicationnegativeconstraint
		////////////////////
//		EqualConstraint equal = new EqualConstraint("equal", "", "");
//		ArrayList<Constraint> constraint2 = new ArrayList<Constraint>();
//		constraint2.add(equal);
//		for(int i = 0; i < 30; i++){
//			if(i+5<30){
//				edges.add(new Edge(nodes.get(i), 
//						nodes.get(i+5), 
//						(ArrayList<Constraint>)constraint2.clone()));
//				edges.add(new Edge(nodes.get(i+5), 
//						nodes.get(i), 
//						(ArrayList<Constraint>)constraint2.clone()));
//			}
//		}
		
		
//		EquiConstraint c1 = new EquiConstraint("c1", new Type(Nationalitaet.Britisch), new Type(Hausfarbe.Rot));
//		EquiConstraint c2 = new EquiConstraint("c2", new Type(Nationalitaet.Schwedisch), new Type(Haustier.Hund));
//		EquiConstraint c3 = new EquiConstraint("c3", new Type(Nationalitaet.Daenisch), new Type(Getraenk.Tee));
//	     
//		ImplicationNegativeConstraint c4_1 = new ImplicationNegativeConstraint("c4_1", new Type(Hausfarbe.Weiss), new Type(Hausposition.Eins));
//		ImplicationNegativeConstraint c4_2 = new ImplicationNegativeConstraint("c4_2", new Type(Hausfarbe.Weiss), new Type(Hausposition.Zwei));
//		ImplicationNegativeConstraint c4_3 = new ImplicationNegativeConstraint("c4_3", new Type(Hausfarbe.Weiss), new Type(Hausposition.Drei));
//		ImplicationNegativeConstraint c4_4 = new ImplicationNegativeConstraint("c4_4", new Type(Hausposition.Eins), new Type(Hausfarbe.Weiss));
//		ImplicationNegativeConstraint c4_5 = new ImplicationNegativeConstraint("c4_5", new Type(Hausposition.Zwei), new Type(Hausfarbe.Weiss));
//		ImplicationNegativeConstraint c4_6 = new ImplicationNegativeConstraint("c4_6", new Type(Hausposition.Drei), new Type(Hausfarbe.Weiss));
//		ImplicationNegativeConstraint c4_7 = new ImplicationNegativeConstraint("c4_7", new Type(Hausposition.Eins), new Type(Hausfarbe.Gruen));
//		ImplicationNegativeConstraint c4_8 = new ImplicationNegativeConstraint("c4_8", new Type(Hausposition.Zwei), new Type(Hausfarbe.Gruen));
//		ImplicationNegativeConstraint c4_9 = new ImplicationNegativeConstraint("c4_9", new Type(Hausposition.Fuenf), new Type(Hausfarbe.Gruen));
//		ImplicationNegativeConstraint c4_10 = new ImplicationNegativeConstraint("c4_10", new Type(Hausfarbe.Gruen), new Type(Hausposition.Eins));
//		ImplicationNegativeConstraint c4_11 = new ImplicationNegativeConstraint("c4_11", new Type(Hausfarbe.Gruen), new Type(Hausposition.Zwei));
//		ImplicationNegativeConstraint c4_12 = new ImplicationNegativeConstraint("c4_12", new Type(Hausfarbe.Gruen), new Type(Hausposition.Fuenf));
//		
//		EquiConstraint c5_1 = new EquiConstraint("c5_1", new Type(Hausfarbe.Gruen), new Type(Getraenk.Kaffee));
//		ImplicationNegativeConstraint c5_2 = new ImplicationNegativeConstraint("c5_2", new Type(Hausfarbe.Gruen), new Type(Nationalitaet.Daenisch));
//		ImplicationNegativeConstraint c5_3 = new ImplicationNegativeConstraint("c5_3", new Type(Nationalitaet.Daenisch), new Type(Hausfarbe.Gruen));
//		EquiConstraint c5_4 = new EquiConstraint("c5_4", new Type(Hausfarbe.Gruen), new Type(Hausposition.Vier));
//		EquiConstraint c5_5 = new EquiConstraint("c5_5", new Type(Hausfarbe.Weiss), new Type(Hausposition.Fuenf));
//		
//		EquiConstraint c6_1 = new EquiConstraint("c6_1", new Type(Zigarettenmarke.Pallmall), new Type(Haustier.Vogel));
//		ImplicationNegativeConstraint c6_2 = new ImplicationNegativeConstraint("c6_2", new Type(Zigarettenmarke.Pallmall), new Type(Nationalitaet.Schwedisch));
//		ImplicationNegativeConstraint c6_3 = new ImplicationNegativeConstraint("c6_3", new Type(Nationalitaet.Schwedisch), new Type(Zigarettenmarke.Pallmall));
//		
//		EquiConstraint c7_1 = new EquiConstraint("c7_1", new Type(Hausposition.Drei), new Type(Getraenk.Milch));
//		ImplicationNegativeConstraint c7_2 = new ImplicationNegativeConstraint("c7_2", new Type(Hausposition.Drei), new Type(Nationalitaet.Daenisch));
//		ImplicationNegativeConstraint c7_3 = new ImplicationNegativeConstraint("c7_3", new Type(Hausposition.Drei), new Type(Hausfarbe.Gruen));
//		ImplicationNegativeConstraint c7_4 = new ImplicationNegativeConstraint("c7_4", new Type(Nationalitaet.Daenisch), new Type(Hausposition.Drei));
//		ImplicationNegativeConstraint c7_5 = new ImplicationNegativeConstraint("c7_5", new Type(Hausfarbe.Gruen), new Type(Hausposition.Drei));
//		
//		EquiConstraint c8_1 = new EquiConstraint("c8_1", new Type(Hausfarbe.Gelb), new Type(Zigarettenmarke.Dunhill));
//		ImplicationNegativeConstraint c8_2 = new ImplicationNegativeConstraint("c8_2", new Type(Hausfarbe.Gelb), new Type(Haustier.Vogel));
//		ImplicationNegativeConstraint c8_3 = new ImplicationNegativeConstraint("c8_3", new Type(Hausfarbe.Gelb), new Type(Nationalitaet.Schwedisch));
//		ImplicationNegativeConstraint c8_4 = new ImplicationNegativeConstraint("c8_4", new Type(Haustier.Vogel), new Type(Hausfarbe.Gelb));
//		ImplicationNegativeConstraint c8_5 = new ImplicationNegativeConstraint("c8_5", new Type(Nationalitaet.Schwedisch), new Type(Hausfarbe.Gelb));
//		
//		EquiConstraint c9_1 = new EquiConstraint("c9_1", new Type(Nationalitaet.Norwegisch), new Type(Hausposition.Eins));
//		ImplicationNegativeConstraint c9_2 = new ImplicationNegativeConstraint("c9_2", new Type(Nationalitaet.Norwegisch), new Type(Hausfarbe.Weiss));
//		ImplicationNegativeConstraint c9_3 = new ImplicationNegativeConstraint("c9_3", new Type(Hausfarbe.Weiss), new Type(Nationalitaet.Norwegisch));
		
//		ImplicationNegativeConstraint c10_1 = new ImplicationNegativeConstraint("c10_1", new Type(Zigarettenmarke.Malboro), new Type(Haustier.Katze));
//		ImplicationNegativeConstraint c10_2 = new ImplicationNegativeConstraint("c10_2", new Type(Haustier.Katze), new Type(Zigarettenmarke.Malboro));
//		ImplicationNegativeConstraint c10_3 = new ImplicationNegativeConstraint("c10_3", new Type(Zigarettenmarke.Malboro), new Type(Hausfarbe.Gelb));
//		ImplicationNegativeConstraint c10_4 = new ImplicationNegativeConstraint("c10_4", new Type(Hausfarbe.Gelb), new Type(Zigarettenmarke.Malboro));
		
//		ImplicationNegativeConstraint c11_1 = new ImplicationNegativeConstraint("c11_1", new Type(Haustier.Pferd), new Type(Zigarettenmarke.Dunhill));
//		ImplicationNegativeConstraint c11_2 = new ImplicationNegativeConstraint("c11_2", new Type(Zigarettenmarke.Dunhill), new Type(Haustier.Pferd));
//		ImplicationNegativeConstraint c11_3 = new ImplicationNegativeConstraint("c11_3", new Type(Haustier.Pferd), new Type(Hausfarbe.Gelb));
//		ImplicationNegativeConstraint c11_4 = new ImplicationNegativeConstraint("c11_4", new Type(Hausfarbe.Gelb), new Type(Haustier.Pferd));
		
//		EquiConstraint c12_1 = new EquiConstraint("c12_1", new Type(Zigarettenmarke.Winfield), new Type(Getraenk.Bier));
//		ImplicationNegativeConstraint c12_2 = new ImplicationNegativeConstraint("c12_2", new Type(Hausposition.Drei), new Type(Getraenk.Bier));
//		ImplicationNegativeConstraint c12_3 = new ImplicationNegativeConstraint("c12_3", new Type(Getraenk.Bier), new Type(Hausposition.Drei));
//		ImplicationNegativeConstraint c12_4 = new ImplicationNegativeConstraint("c12_4", new Type(Hausposition.Drei), new Type(Zigarettenmarke.Winfield));
//		ImplicationNegativeConstraint c12_5 = new ImplicationNegativeConstraint("c12_5", new Type(Zigarettenmarke.Winfield), new Type(Hausposition.Drei));
//		ImplicationNegativeConstraint c12_6 = new ImplicationNegativeConstraint("c12_6", new Type(Zigarettenmarke.Winfield), new Type(Nationalitaet.Daenisch));
//		ImplicationNegativeConstraint c12_7 = new ImplicationNegativeConstraint("c12_7", new Type(Nationalitaet.Daenisch), new Type(Zigarettenmarke.Winfield));
//		ImplicationNegativeConstraint c12_8 = new ImplicationNegativeConstraint("c12_8", new Type(Zigarettenmarke.Winfield), new Type(Hausfarbe.Gruen));
//		ImplicationNegativeConstraint c12_9 = new ImplicationNegativeConstraint("c12_9", new Type(Hausfarbe.Gruen), new Type(Zigarettenmarke.Winfield));
		
		
//		ImplicationNegativeConstraint c13_1 = new ImplicationNegativeConstraint("c13_1", new Type(Nationalitaet.Norwegisch), new Type(Hausfarbe.Blau));
//		ImplicationNegativeConstraint c13_2 = new ImplicationNegativeConstraint("c13_2", new Type(Hausposition.Eins), new Type(Hausfarbe.Blau));
//		ImplicationNegativeConstraint c13_3 = new ImplicationNegativeConstraint("c13_3", new Type(Hausfarbe.Blau), new Type(Hausposition.Eins));
//		ImplicationNegativeConstraint c13_4 = new ImplicationNegativeConstraint("c13_4", new Type(Hausfarbe.Blau), new Type(Nationalitaet.Norwegisch));
//		EquiConstraint c13_5 = new EquiConstraint("c13_5", new Type(Hausfarbe.Blau), new Type(Hausposition.Zwei));
		
//		EquiConstraint c14_1 = new EquiConstraint("c14_1", new Type(Nationalitaet.Deutsch), new Type(Zigarettenmarke.Rothmanns));
//		
//		ImplicationNegativeConstraint c15_1 = new ImplicationNegativeConstraint("c15_1", new Type(Zigarettenmarke.Malboro), new Type(Getraenk.Wasser));
//		ImplicationNegativeConstraint c15_2 = new ImplicationNegativeConstraint("c15_2", new Type(Getraenk.Wasser), new Type(Zigarettenmarke.Malboro));

		//Constraints auf gemeinsamer Kante
//				ArrayList<Constraint> nation_farbe = new ArrayList<>();
//				nation_farbe.add(c1);
//				nation_farbe.add(c5_2);
//				nation_farbe.add(c5_3);
//				nation_farbe.add(c8_3);
//				nation_farbe.add(c8_5);
//				nation_farbe.add(c9_2);
//				nation_farbe.add(c9_3);
//				nation_farbe.add(c13_1);
//				nation_farbe.add(c13_4);
//				
//				ArrayList<Constraint> nation_haustier = new ArrayList<>();
//				nation_haustier.add(c2);
//				
//				ArrayList<Constraint> nation_getraenk = new ArrayList<>();
//				nation_getraenk.add(c3);
//				
//				ArrayList<Constraint> farbe_pos = new ArrayList<>();
//				farbe_pos.add(c4_1);
//				farbe_pos.add(c4_2);
//				farbe_pos.add(c4_3);
//				farbe_pos.add(c4_4);
//				farbe_pos.add(c4_5);
//				farbe_pos.add(c4_6);
//				farbe_pos.add(c4_7);
//				farbe_pos.add(c4_8);
//				farbe_pos.add(c4_9);
//				farbe_pos.add(c4_10);
//				farbe_pos.add(c4_11);
//				farbe_pos.add(c4_12);
//				farbe_pos.add(c7_3);
//				farbe_pos.add(c7_5);
//				farbe_pos.add(c13_2);
//				farbe_pos.add(c13_3);
//				farbe_pos.add(c13_5);
//				farbe_pos.add(c5_4);
//				farbe_pos.add(c5_5);
//				
//				ArrayList<Constraint> farbe_getraenk = new ArrayList<>();
//				farbe_getraenk.add(c5_1);
//				
//				ArrayList<Constraint> zigaretten_tier = new ArrayList<>();
//				zigaretten_tier.add(c6_1);
//				zigaretten_tier.add(c10_1);
//				zigaretten_tier.add(c10_2);
//				zigaretten_tier.add(c11_1);
//				zigaretten_tier.add(c11_2);
//				
//				ArrayList<Constraint> zigaretten_nation = new ArrayList<>();
//				zigaretten_nation.add(c6_2);
//				zigaretten_nation.add(c6_3);
//				zigaretten_nation.add(c12_6);
//				zigaretten_nation.add(c12_7);
//				zigaretten_nation.add(c14_1);
//				
//				ArrayList<Constraint> pos_getraenk = new ArrayList<>();
//				pos_getraenk.add(c7_1);
//				pos_getraenk.add(c12_2);
//				pos_getraenk.add(c12_3);
//				
//				ArrayList<Constraint> pos_nation = new ArrayList<>();
//				pos_nation.add(c7_2);
//				pos_nation.add(c7_4);
//				pos_nation.add(c9_1);
//				
//				ArrayList<Constraint> farbe_zigaretten = new ArrayList<>();
//				farbe_zigaretten.add(c8_1);
//				farbe_zigaretten.add(c10_3);
//				farbe_zigaretten.add(c10_4);
//				farbe_zigaretten.add(c12_8);
//				farbe_zigaretten.add(c12_9);
//				
//				ArrayList<Constraint> farbe_tier = new ArrayList<>();
//				farbe_tier.add(c8_2);
//				farbe_tier.add(c8_4);
//				farbe_tier.add(c11_3);
//				farbe_tier.add(c11_4);
//				
//				ArrayList<Constraint> zigaretten_getraenk = new ArrayList<>();
//				zigaretten_getraenk.add(c12_1);
//				zigaretten_getraenk.add(c15_1);
//				zigaretten_getraenk.add(c15_2);
//				
//				ArrayList<Constraint> zigaretten_pos = new ArrayList<>();
//				zigaretten_pos.add(c12_4);
//				zigaretten_pos.add(c12_5);
//		
//		ArrayList<Integer> nation = new ArrayList<Integer>();
//		ArrayList<Integer> getraenk = new ArrayList<Integer>();
//		ArrayList<Integer> zigaretten = new ArrayList<Integer>();
//		ArrayList<Integer> farbe = new ArrayList<Integer>();
//		ArrayList<Integer> tier = new ArrayList<Integer>();
//		ArrayList<Integer> pos = new ArrayList<Integer>();
//		nation.add(1);
//		nation.add(2);
//		nation.add(3);
//		nation.add(4);
//		nation.add(5);
//		getraenk.add(6);
//		getraenk.add(7);
//		getraenk.add(8);
//		getraenk.add(9);
//		getraenk.add(10);
//		zigaretten.add(11);
//		zigaretten.add(12);
//		zigaretten.add(13);
//		zigaretten.add(14);
//		zigaretten.add(15);
//		farbe.add(16);
//		farbe.add(17);
//		farbe.add(18);
//		farbe.add(19);
//		farbe.add(20);
//		tier.add(21);
//		tier.add(22);
//		tier.add(23);
//		tier.add(24);
//		tier.add(25);
//		tier.add(26);
//		tier.add(27);
//		tier.add(28);
//		tier.add(29);
//		tier.add(30);
//		
//		
//		
//		for(Edge e : edges){
//			if((nation.contains(e.getN1().getNr())
//					|| nation.contains(e.getN2().getNr()))
//					&& (getraenk.contains(e.getN2().getNr())
//					   || getraenk.contains(e.getN1().getNr()))){
//				for(Constraint c : nation_getraenk){
//					e.getConstraints().add(c);
//				}
//			}
//			
//			if((nation.contains(e.getN1().getNr())
//					|| nation.contains(e.getN2().getNr()))
//					&& (farbe.contains(e.getN2().getNr())
//					   || farbe.contains(e.getN1().getNr()))){
//				for(Constraint c : nation_farbe){
//					e.getConstraints().add(c);
//				}
//			}
//			
//			if((nation.contains(e.getN1().getNr())
//					|| nation.contains(e.getN2().getNr()))
//					&& (tier.contains(e.getN2().getNr())
//					   || tier.contains(e.getN1().getNr()))){
//				for(Constraint c : nation_haustier){
//					e.getConstraints().add(c);
//				}
//			}
//			
//			if((farbe.contains(e.getN1().getNr())
//					|| farbe.contains(e.getN2().getNr()))
//					&& (pos.contains(e.getN2().getNr())
//					   || pos.contains(e.getN1().getNr()))){
//				for(Constraint c : farbe_pos){
//					e.getConstraints().add(c);
//				}
//			}
//			
//			if((farbe.contains(e.getN1().getNr())
//					|| farbe.contains(e.getN2().getNr()))
//					&& (getraenk.contains(e.getN2().getNr())
//					   || getraenk.contains(e.getN1().getNr()))){
//				for(Constraint c : farbe_getraenk){
//					e.getConstraints().add(c);
//				}
//			}
//			
//			if((zigaretten.contains(e.getN1().getNr())
//					|| zigaretten.contains(e.getN2().getNr()))
//					&& (tier.contains(e.getN2().getNr())
//					   || tier.contains(e.getN1().getNr()))){
//				for(Constraint c : zigaretten_tier){
//					e.getConstraints().add(c);
//				}
//			}
//			
//			if((zigaretten.contains(e.getN1().getNr())
//					|| zigaretten.contains(e.getN2().getNr()))
//					&& (nation.contains(e.getN2().getNr())
//					   || nation.contains(e.getN1().getNr()))){
//				for(Constraint c : zigaretten_nation){
//					e.getConstraints().add(c);
//				}
//			}
//			
//			if((pos.contains(e.getN1().getNr())
//					|| pos.contains(e.getN2().getNr()))
//					&& (getraenk.contains(e.getN2().getNr())
//					   || getraenk.contains(e.getN1().getNr()))){
//				for(Constraint c : pos_getraenk){
//					e.getConstraints().add(c);
//				}
//			}
//			
//			if((pos.contains(e.getN1().getNr())
//					|| pos.contains(e.getN2().getNr()))
//					&& (nation.contains(e.getN2().getNr())
//					   || nation.contains(e.getN1().getNr()))){
//				for(Constraint c : pos_nation){
//					e.getConstraints().add(c);
//				}
//			}
//			
//			if((farbe.contains(e.getN1().getNr())
//					|| farbe.contains(e.getN2().getNr()))
//					&& (zigaretten.contains(e.getN2().getNr())
//					   || zigaretten.contains(e.getN1().getNr()))){
//				for(Constraint c : farbe_zigaretten){
//					e.getConstraints().add(c);
//				}
//			}
//			
//			if((farbe.contains(e.getN1().getNr())
//					|| farbe.contains(e.getN2().getNr()))
//					&& (tier.contains(e.getN2().getNr())
//					   || tier.contains(e.getN1().getNr()))){
//				for(Constraint c : farbe_zigaretten){
//					e.getConstraints().add(c);
//				}
//			}
//			
//			if((zigaretten.contains(e.getN1().getNr())
//					|| zigaretten.contains(e.getN2().getNr()))
//					&& (getraenk.contains(e.getN2().getNr())
//					   || getraenk.contains(e.getN1().getNr()))){
//				for(Constraint c : zigaretten_getraenk){
//					e.getConstraints().add(c);
//				}
//			}
//			
//			if((zigaretten.contains(e.getN1().getNr())
//					|| zigaretten.contains(e.getN2().getNr()))
//					&& (pos.contains(e.getN2().getNr())
//					   || pos.contains(e.getN1().getNr()))){
//				for(Constraint c : zigaretten_getraenk){
//					e.getConstraints().add(c);
//				}
//			}
//		}
//		
//		
//		
//		
//		for(Node n : nodes){
//			nodes2.add(n);
//		}
//		
//		for(Edge e : edges){
//			edges2.add(e);
//		}
//		
//		ConstraintNet constraintNet = new ConstraintNet(nodes2, edges2);
//		
		System.out.println(edges);
		HashSet<Node> nodes2 = new HashSet<Node>();
		HashSet<Edge> edges2 = new HashSet<Edge>();
		
		for(Node n : nodes){
			nodes2.add(n);
		}
		for(Edge e : edges){
			edges2.add(e);
		}
		
		ConstraintNet constraintNet = new ConstraintNet(nodes2, edges2);

		
		return constraintNet;
	}
}
