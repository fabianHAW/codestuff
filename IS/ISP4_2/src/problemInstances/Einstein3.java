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
		//Brite, Rot ok
		edges.add(new Edge(n1, n19, (ArrayList<Constraint>)constraint3.clone()));
		//Schwede, Hund ok
		edges.add(new Edge(n2, n21, (ArrayList<Constraint>)constraint3.clone()));
		//Daene, Tee ok
		edges.add(new Edge(n4, n9, (ArrayList<Constraint>)constraint3.clone()));
		//Gruen, Kaffee ok
		edges.add(new Edge(n18, n10, (ArrayList<Constraint>)constraint3.clone()));
		//Gruen, Vier ok
		edges.add(new Edge(n18, n29, (ArrayList<Constraint>)constraint3.clone()));
		//Weiss, Fuenf ok
		edges.add(new Edge(n20, n30, (ArrayList<Constraint>)constraint3.clone()));
		//Pallmall, Vogel ok
		edges.add(new Edge(n13, n24, (ArrayList<Constraint>)constraint3.clone()));
		//Drei, Milch ok
		edges.add(new Edge(n28, n7, (ArrayList<Constraint>)constraint3.clone()));
		//Gelb, Dunhill ok
		edges.add(new Edge(n17, n12, (ArrayList<Constraint>)constraint3.clone()));
		//Norwegisch, Eins ok
		edges.add(new Edge(n3, n26, (ArrayList<Constraint>)constraint3.clone()));
		//Winfield, Bier ok
		edges.add(new Edge(n15, n6, (ArrayList<Constraint>)constraint3.clone()));
		//Blau, Zwei ok
		edges.add(new Edge(n16, n27, (ArrayList<Constraint>)constraint3.clone()));
		//Deutsch, Rothmanns ok
		edges.add(new Edge(n5, n14, (ArrayList<Constraint>)constraint3.clone()));
		
		//umgekehrt zu oben
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

		//Weiss, Eins ok
		edges.add(new Edge(n20, n26, (ArrayList<Constraint>)constraint1.clone()));
		//Weiss, Zwei ok
		edges.add(new Edge(n20, n27, (ArrayList<Constraint>)constraint1.clone()));
		//Weiss, Drei ok
		edges.add(new Edge(n20, n28, (ArrayList<Constraint>)constraint1.clone()));
		//umgekehrt zu den drei zuvor
		edges.add(new Edge(n26, n20, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n27, n20, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n28, n20, (ArrayList<Constraint>)constraint1.clone()));
		//Gruen, Eins ok
		edges.add(new Edge(n18, n26, (ArrayList<Constraint>)constraint1.clone()));
		//Gruen, zwei ok
		edges.add(new Edge(n18, n27, (ArrayList<Constraint>)constraint1.clone()));
		//Gruen, fuenf ok
		edges.add(new Edge(n18, n30, (ArrayList<Constraint>)constraint1.clone()));
		//umgekehrt zu den drei zuvor
		edges.add(new Edge(n26, n18, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n27, n18, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n30, n18, (ArrayList<Constraint>)constraint1.clone()));

		//Daenisch, Gruen ok
		edges.add(new Edge(n4, n18, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n18, n4, (ArrayList<Constraint>)constraint1.clone()));

		//Schwedisch, Pallmall ok
		edges.add(new Edge(n2, n13, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n13, n2, (ArrayList<Constraint>)constraint1.clone()));

		//Drei, Gruen ok
		edges.add(new Edge(n28, n18, (ArrayList<Constraint>)constraint1.clone()));
		//Drei, Daenisch ok
		edges.add(new Edge(n28, n4, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n18, n28, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n4, n28, (ArrayList<Constraint>)constraint1.clone()));
	
		//Vogel, Gelb ok
		edges.add(new Edge(n24, n17, (ArrayList<Constraint>)constraint1.clone()));
		//Schwedisch, Gelb ok
		edges.add(new Edge(n2, n17, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n17, n24, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n17, n2, (ArrayList<Constraint>)constraint1.clone()));
	
		//Norwege, Weiss ok
		edges.add(new Edge(n3, n20, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n20, n3, (ArrayList<Constraint>)constraint1.clone()));

		//Malboro, Katze ok
		edges.add(new Edge(n11, n22, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n22, n11, (ArrayList<Constraint>)constraint1.clone()));
		//Gelb, Katze falsch
//		edges.add(new Edge(n17, n11, (ArrayList<Constraint>)constraint1.clone()));
//		edges.add(new Edge(n11, n17, (ArrayList<Constraint>)constraint1.clone()));

		//Dunhill, Pferd ok
		edges.add(new Edge(n12, n23, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n23, n12, (ArrayList<Constraint>)constraint1.clone()));
		//Gelb, Pferd ok
		edges.add(new Edge(n17, n23, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n23, n17, (ArrayList<Constraint>)constraint1.clone()));

		//Drei, Bier ok
		edges.add(new Edge(n28, n6, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n6, n28, (ArrayList<Constraint>)constraint1.clone()));
		//Drei, Winfield ok
		edges.add(new Edge(n28, n15, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n15, n28, (ArrayList<Constraint>)constraint1.clone()));
		//Daenisch, Winfield ok
		edges.add(new Edge(n4, n15, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n15, n4, (ArrayList<Constraint>)constraint1.clone()));
		//Gruen, Winfield ok
		edges.add(new Edge(n18, n15, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n15, n18, (ArrayList<Constraint>)constraint1.clone()));
			
		//Norwegisch, Blau ok
		edges.add(new Edge(n3, n16, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n16, n3, (ArrayList<Constraint>)constraint1.clone()));
		//Norwegisch, Eins falsch
//		edges.add(new Edge(n3, n26, (ArrayList<Constraint>)constraint1.clone()));
//		edges.add(new Edge(n26, n3, (ArrayList<Constraint>)constraint1.clone()));
	
		//Malboro, Wasser ok
		edges.add(new Edge(n11, n8, (ArrayList<Constraint>)constraint1.clone()));
		edges.add(new Edge(n8, n11, (ArrayList<Constraint>)constraint1.clone()));

		//System.out.println(edges);
		HashSet<Node> nodes2 = new HashSet<Node>();
		HashSet<Edge> edges2 = new HashSet<Edge>();
		
		for(Node n : nodes){
			nodes2.add(n);
		}
		for(Edge e : edges){
			edges2.add(e);
		}
		
		ConstraintNet constraintNet = new ConstraintNet((HashSet<Node>)nodes2.clone(), (HashSet<Edge>)edges2.clone());

		
		return constraintNet;
	}
}
