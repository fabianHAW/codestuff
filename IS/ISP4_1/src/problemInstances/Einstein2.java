package problemInstances;

import java.util.ArrayList;
import java.util.Arrays;

import constsraintnet.Constraint;
import constsraintnet.ConstraintNet;
import constsraintnet.DifferentConstraint;
import constsraintnet.Edge;
import constsraintnet.EqualConstraint;
import constsraintnet.Node;
import constsraintnet.Type;
import domains.Hausposition;

public class Einstein2 {
	

	public static ConstraintNet generateConstraintNet(){
		ConstraintNet constraintNet = null;
		ArrayList<Type> domain = new ArrayList<Type>();
		domain.add(new Type(Hausposition.Eins));
		domain.add(new Type(Hausposition.Zwei));
		domain.add(new Type(Hausposition.Drei));
		domain.add(new Type(Hausposition.Vier));
		domain.add(new Type(Hausposition.Fuenf));
		
		Node n1 = new Node("Brite", (ArrayList<Type>)domain.clone(), 1);
		Node n2 = new Node("Schwede", (ArrayList<Type>)domain.clone(), 2);
		Node n3 = new Node("Norweger", (ArrayList<Type>)domain.clone(), 3);
		Node n4 = new Node("Däne", (ArrayList<Type>)domain.clone(), 4);
		Node n5 = new Node("Deutscher", (ArrayList<Type>)domain.clone(), 5);
		
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
		Node n18 = new Node("Grün", (ArrayList<Type>)domain.clone(), 18);
		Node n19 = new Node("Rot", (ArrayList<Type>)domain.clone(), 19);
		Node n20 = new Node("Weiss", (ArrayList<Type>)domain.clone(), 20);
		
		Node n21 = new Node("Hund", (ArrayList<Type>)domain.clone(), 21);
		Node n22 = new Node("Katze", (ArrayList<Type>)domain.clone(), 22);
		Node n23 = new Node("Pferd", (ArrayList<Type>)domain.clone(), 23);
		Node n24 = new Node("Vogel", (ArrayList<Type>)domain.clone(), 24);
		Node n25 = new Node("Fisch", (ArrayList<Type>)domain.clone(), 25);
		
		DifferentConstraint different = new DifferentConstraint("c100", null, null);
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
		
		ArrayList<Edge> edges = new ArrayList<Edge>();
		
		//Different-Constraint zwischen Nodes selber Gattung (z.B. Getränk, Tier,...)
		for(int i = 0; i < 25; i++){
			for(int j = 0; j < 25; j++){					
					if((i < 5 && j < 5 && i != j)
						|| (i > 4 && i < 10 && j > 4 && j < 10 && i != j)
						|| (i > 9 && i < 15 && j > 9 && j < 15 && i != j)
						|| (i > 14 && i < 20 && j > 14 && j < 20 && i != j)
						|| (i > 19 && i < 25 && j > 19 && j < 25 && i != j)){
						edges.add(new Edge(nodes.get(i), 
								nodes.get(j), 
								(ArrayList<Constraint>)constraint1.clone()));
					}
					if((i < 5 && j > 4 && j < 10)
					    || (i > 4 && i < 10 && j > 14 && j < 20)){
						System.out.println((i+1) + ", " + (j+1));
					}
			}
		}
		
		EqualConstraint equal = new EqualConstraint("equal", null, null);
		ArrayList<Constraint> constraint2 = new ArrayList<Constraint>();
		constraint2.add(equal);
		
	
		
		return constraintNet;
	}

}
