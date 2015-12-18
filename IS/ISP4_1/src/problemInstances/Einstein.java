package problemInstances;

import java.util.ArrayList;

import constsraintnet.Constraint;
import constsraintnet.ConstraintNet;
import constsraintnet.EquiConstraint;
import constsraintnet.ImplicationNegativeConstraint;
import constsraintnet.Node;
import constsraintnet.OrConstraint;
import constsraintnet.Type;
import domains.Getraenk;
import domains.Hausfarbe;
import domains.Hausposition;
import domains.Haustier;
import domains.Nationalitaet;
import domains.Zigarettenmarke;

public class Einstein {
	
	private Einstein(){
		
	}
	
public static ConstraintNet generateConstraintNet(){
		
		//Constraints
		EquiConstraint c1 = new EquiConstraint("c1", 
				new Type(Nationalitaet.Britisch), 
				new Type(Hausfarbe.Rot));
		EquiConstraint c2 = new EquiConstraint("c2", 
				new Type(Nationalitaet.Schwedisch), 
				new Type(Haustier.Hund));
		EquiConstraint c3 = new EquiConstraint("c3", 
				new Type(Nationalitaet.Daenisch), 
				new Type(Getraenk.Tee));
		EquiConstraint c4_x = new EquiConstraint("c4.x", 
				new Type(Hausfarbe.Gruen), 
				new Type(Hausposition.Vier));
		EquiConstraint c4_y = new EquiConstraint("c4.y", 
				new Type(Hausposition.Vier),
				new Type(Hausfarbe.Gruen));
//		ImplicationNegativeConstraint c4 = new ImplicationNegativeConstraint("c4", 
//				new Type(Hausfarbe.Gruen), 
//				new Type(Hausposition.Fuenf));
//		ImplicationNegativeConstraint c4_1_1 = new ImplicationNegativeConstraint("c4_1_1",  
//				new Type(Hausposition.Fuenf),
//				new Type(Hausfarbe.Gruen));
		ImplicationNegativeConstraint c4_1 = new ImplicationNegativeConstraint("c4_1", 
				new Type(Hausfarbe.Weiss), 
				new Type(Hausposition.Eins));
		ImplicationNegativeConstraint c4_1_2 = new ImplicationNegativeConstraint("c4_1_2",  
				new Type(Hausposition.Eins),
				new Type(Hausfarbe.Weiss));
		EquiConstraint c4_x1 = new EquiConstraint("c4.x1", 
				new Type(Hausfarbe.Weiss), 
				new Type(Hausposition.Fuenf));
		EquiConstraint c4_y1 = new EquiConstraint("c4.y1", 
				new Type(Hausposition.Fuenf),
				new Type(Hausfarbe.Weiss));
		EquiConstraint c5 = new EquiConstraint("c5", 
				new Type(Hausfarbe.Gruen), 
				new Type(Getraenk.Kaffee));
		EquiConstraint c6 = new EquiConstraint("c6", 
				new Type(Zigarettenmarke.Pallmall), 
				new Type(Haustier.Vogel));
		EquiConstraint c7 = new EquiConstraint("c7", 
				new Type(Hausposition.Drei), 
				new Type(Getraenk.Milch));
		EquiConstraint c8 = new EquiConstraint("c8", 
				new Type(Hausfarbe.Gelb), 
				new Type(Zigarettenmarke.Dunhill));
		EquiConstraint c9 = new EquiConstraint("c9", 
				new Type(Nationalitaet.Norwegisch), 
				new Type(Hausposition.Eins));
		ImplicationNegativeConstraint c10 = new ImplicationNegativeConstraint("c10", 
				new Type(Zigarettenmarke.Malboro), 
				new Type(Haustier.Katze));
		ImplicationNegativeConstraint c10_1 = new ImplicationNegativeConstraint("c10_1",  
				new Type(Haustier.Katze),
				new Type(Zigarettenmarke.Malboro));
		ImplicationNegativeConstraint c11_1 = new ImplicationNegativeConstraint("c11_1", 
				new Type(Haustier.Pferd), 
				new Type(Zigarettenmarke.Dunhill));
		ImplicationNegativeConstraint c11_2 = new ImplicationNegativeConstraint("c11_2", 
				new Type(Haustier.Pferd), 
				new Type(Hausfarbe.Gelb));
		ImplicationNegativeConstraint c11_4 = new ImplicationNegativeConstraint("c11_4",  
				new Type(Hausfarbe.Gelb),
				new Type(Haustier.Pferd));
		ImplicationNegativeConstraint c11_3 = new ImplicationNegativeConstraint("c11_3", 
				new Type(Zigarettenmarke.Dunhill), 
				new Type(Haustier.Pferd));
		EquiConstraint c12 = new EquiConstraint("c12", 
				new Type(Zigarettenmarke.Winfield), 
				new Type(Getraenk.Bier));
		EquiConstraint c13 = new EquiConstraint("c13", 
				new Type(Hausposition.Zwei), 
				new Type(Hausfarbe.Blau));
		EquiConstraint c14 = new EquiConstraint("c14", 
				new Type(Nationalitaet.Deutsch), 
				new Type(Zigarettenmarke.Rothmanns));
		ImplicationNegativeConstraint c15 = new ImplicationNegativeConstraint("c15", 
				new Type(Zigarettenmarke.Malboro), 
				new Type(Getraenk.Wasser));
		ImplicationNegativeConstraint c15_1 = new ImplicationNegativeConstraint("c15_1",  
				new Type(Getraenk.Wasser),
				new Type(Zigarettenmarke.Malboro));
		//Constraint 16 nicht modelliert da nur implizit im Raetsel.
		OrConstraint c16 = new OrConstraint("c16",
				new Type(Hausfarbe.Weiss), new Type(Hausposition.Drei), new Type(Hausposition.Vier));
		
		//Knoten
		ArrayList<Type> nationDomain = new ArrayList<Type>();
		nationDomain.add(new Type(Nationalitaet.Britisch));
		nationDomain.add(new Type(Nationalitaet.Daenisch));
		nationDomain.add(new Type(Nationalitaet.Deutsch));
		nationDomain.add(new Type(Nationalitaet.Norwegisch));
		nationDomain.add(new Type(Nationalitaet.Schwedisch));
		
		ArrayList<Type> farbeDomain = new ArrayList<Type>();
		farbeDomain.add(new Type(Hausfarbe.Blau));
		farbeDomain.add(new Type(Hausfarbe.Gelb));
		farbeDomain.add(new Type(Hausfarbe.Gruen));
		farbeDomain.add(new Type(Hausfarbe.Rot));
		farbeDomain.add(new Type(Hausfarbe.Weiss));
		
		ArrayList<Type> getraenkDomain = new ArrayList<Type>();
		getraenkDomain.add(new Type(Getraenk.Bier));
		getraenkDomain.add(new Type(Getraenk.Kaffee));
		getraenkDomain.add(new Type(Getraenk.Milch));
		getraenkDomain.add(new Type(Getraenk.Tee));
		getraenkDomain.add(new Type(Getraenk.Wasser));
		
		ArrayList<Type> haustierDomain = new ArrayList<Type>();
		haustierDomain.add(new Type(Haustier.Hund));
		haustierDomain.add(new Type(Haustier.Katze));
		haustierDomain.add(new Type(Haustier.Pferd));
		haustierDomain.add(new Type(Haustier.Vogel));
		haustierDomain.add(new Type(Haustier.Fisch));
		
		ArrayList<Type> zigarettenDomain = new ArrayList<Type>();
		zigarettenDomain.add(new Type(Zigarettenmarke.Dunhill));
		zigarettenDomain.add(new Type(Zigarettenmarke.Malboro));
		zigarettenDomain.add(new Type(Zigarettenmarke.Pallmall));
		zigarettenDomain.add(new Type(Zigarettenmarke.Rothmanns));
		zigarettenDomain.add(new Type(Zigarettenmarke.Winfield));
		
		ArrayList<Type> hauspositionDomain = new ArrayList<Type>();
		hauspositionDomain.add(new Type(Hausposition.Eins));
		hauspositionDomain.add(new Type(Hausposition.Zwei));
		hauspositionDomain.add(new Type(Hausposition.Drei));
		hauspositionDomain.add(new Type(Hausposition.Vier));
		hauspositionDomain.add(new Type(Hausposition.Fuenf));
		
		Node nationalitaet = new Node("Nationalität", nationDomain, 1);
		Node getraenk = new Node("Getränk", getraenkDomain, 2);
		Node hausfarbe = new Node("Farbe", farbeDomain, 3);
		Node haustier = new Node("Haustier", haustierDomain, 6);
		Node hausposition = new Node("Hausposition", hauspositionDomain, 4);
		Node zigaretten = new Node("Zigarettenmarke", zigarettenDomain, 5);
		
		//Constraints auf gemeinsamer Kante
		ArrayList<Constraint> nation_hausfarbe = new ArrayList<>();
		nation_hausfarbe.add(c1);
		ArrayList<Constraint> nation_zigaretten = new ArrayList<>();
		nation_zigaretten.add(c14);
		ArrayList<Constraint> nation_hausposition = new ArrayList<>();
		nation_hausposition.add(c9);
		ArrayList<Constraint> nation_getraenk = new ArrayList<>();
		nation_getraenk.add(c3);
		ArrayList<Constraint> nation_haustier = new ArrayList<>();
		nation_haustier.add(c2);
		
		ArrayList<Constraint> hausfarbe_zigaretten = new ArrayList<>();
		hausfarbe_zigaretten.add(c8);
		ArrayList<Constraint> hausfarbe_hausposition = new ArrayList<>();
		//hausfarbe_hausposition.add(c4);
		hausfarbe_hausposition.add(c4_1);
		hausfarbe_hausposition.add(c4_x);
		hausfarbe_hausposition.add(c4_y);
		hausfarbe_hausposition.add(c4_x1);
		hausfarbe_hausposition.add(c4_y1);
		//hausfarbe_hausposition.add(c4_1_1);
		hausfarbe_hausposition.add(c4_1_2);
		hausfarbe_hausposition.add(c13);
		//hausfarbe_hausposition.add(c16);
		ArrayList<Constraint> hausfarbe_getraenk = new ArrayList<>();
		hausfarbe_getraenk.add(c5);
		ArrayList<Constraint> hausfarbe_haustier = new ArrayList<>();
		hausfarbe_haustier.add(c11_2);
		
		ArrayList<Constraint> zigaretten_getraenk = new ArrayList<>();
		zigaretten_getraenk.add(c12);
		zigaretten_getraenk.add(c15);
		zigaretten_getraenk.add(c15_1);
		
		ArrayList<Constraint> zigaretten_haustier = new ArrayList<>();
		zigaretten_haustier.add(c6);
		zigaretten_haustier.add(c10);
		zigaretten_haustier.add(c10_1);
		zigaretten_haustier.add(c11_1);
		zigaretten_haustier.add(c11_3);
		zigaretten_haustier.add(c11_4);
		
		ArrayList<Constraint> hausposition_getraenk = new ArrayList<>();
		hausposition_getraenk.add(c7);
		
		//Kanten mit Constraints
		ConstraintNet constraintNet = new ConstraintNet();
		constraintNet.setConstraint(hausfarbe, nationalitaet, nation_hausfarbe);
		constraintNet.setConstraint(zigaretten, nationalitaet, nation_zigaretten);
		constraintNet.setConstraint(hausposition, nationalitaet, nation_hausposition);
		constraintNet.setConstraint(getraenk, nationalitaet, nation_getraenk);
		constraintNet.setConstraint(haustier, nationalitaet, nation_haustier);
		
		constraintNet.setConstraint(zigaretten, hausfarbe, hausfarbe_zigaretten);
		constraintNet.setConstraint(hausposition, hausfarbe, hausfarbe_hausposition);
		constraintNet.setConstraint(getraenk, hausfarbe, hausfarbe_getraenk);
		constraintNet.setConstraint(haustier, hausfarbe, hausfarbe_haustier);
		
		constraintNet.setConstraint(getraenk, zigaretten, zigaretten_getraenk);
		constraintNet.setConstraint(haustier, zigaretten, zigaretten_haustier);
		
		constraintNet.setConstraint(getraenk, hausposition, hausposition_getraenk);
		
		//
		constraintNet.setConstraint(nationalitaet, hausfarbe, nation_hausfarbe);
		constraintNet.setConstraint(nationalitaet, zigaretten, nation_zigaretten);
		constraintNet.setConstraint(nationalitaet, hausposition, nation_hausposition);
		constraintNet.setConstraint(nationalitaet, getraenk, nation_getraenk);
		constraintNet.setConstraint(nationalitaet, haustier, nation_haustier);
		
		constraintNet.setConstraint(hausfarbe, zigaretten, hausfarbe_zigaretten);
		constraintNet.setConstraint(hausfarbe, hausposition, hausfarbe_hausposition);
		constraintNet.setConstraint(hausfarbe, getraenk, hausfarbe_getraenk);
		constraintNet.setConstraint(hausfarbe, haustier, hausfarbe_haustier);
		
		constraintNet.setConstraint(zigaretten, getraenk, zigaretten_getraenk);
		constraintNet.setConstraint(zigaretten, haustier, zigaretten_haustier);
		
		constraintNet.setConstraint(hausposition, getraenk, hausposition_getraenk);
		
		return constraintNet;
	}

}
