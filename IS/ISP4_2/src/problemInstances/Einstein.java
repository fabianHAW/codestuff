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
		EquiConstraint c1 = new EquiConstraint("c1", new Type(Nationalitaet.Britisch), new Type(Hausfarbe.Rot));
		EquiConstraint c2 = new EquiConstraint("c2", new Type(Nationalitaet.Schwedisch), new Type(Haustier.Hund));
		EquiConstraint c3 = new EquiConstraint("c3", new Type(Nationalitaet.Daenisch), new Type(Getraenk.Tee));
	     
		ImplicationNegativeConstraint c4_1 = new ImplicationNegativeConstraint("c4_1", new Type(Hausfarbe.Weiss), new Type(Hausposition.Eins));
		ImplicationNegativeConstraint c4_2 = new ImplicationNegativeConstraint("c4_2", new Type(Hausfarbe.Weiss), new Type(Hausposition.Zwei));
		ImplicationNegativeConstraint c4_3 = new ImplicationNegativeConstraint("c4_3", new Type(Hausfarbe.Weiss), new Type(Hausposition.Drei));
		ImplicationNegativeConstraint c4_4 = new ImplicationNegativeConstraint("c4_4", new Type(Hausposition.Eins), new Type(Hausfarbe.Weiss));
		ImplicationNegativeConstraint c4_5 = new ImplicationNegativeConstraint("c4_5", new Type(Hausposition.Zwei), new Type(Hausfarbe.Weiss));
		ImplicationNegativeConstraint c4_6 = new ImplicationNegativeConstraint("c4_6", new Type(Hausposition.Drei), new Type(Hausfarbe.Weiss));
		ImplicationNegativeConstraint c4_7 = new ImplicationNegativeConstraint("c4_7", new Type(Hausposition.Eins), new Type(Hausfarbe.Gruen));
		ImplicationNegativeConstraint c4_8 = new ImplicationNegativeConstraint("c4_8", new Type(Hausposition.Zwei), new Type(Hausfarbe.Gruen));
		ImplicationNegativeConstraint c4_9 = new ImplicationNegativeConstraint("c4_9", new Type(Hausposition.Fuenf), new Type(Hausfarbe.Gruen));
		ImplicationNegativeConstraint c4_10 = new ImplicationNegativeConstraint("c4_10", new Type(Hausfarbe.Gruen), new Type(Hausposition.Eins));
		ImplicationNegativeConstraint c4_11 = new ImplicationNegativeConstraint("c4_11", new Type(Hausfarbe.Gruen), new Type(Hausposition.Zwei));
		ImplicationNegativeConstraint c4_12 = new ImplicationNegativeConstraint("c4_12", new Type(Hausfarbe.Gruen), new Type(Hausposition.Fuenf));
		
		EquiConstraint c5_1 = new EquiConstraint("c5_1", new Type(Hausfarbe.Gruen), new Type(Getraenk.Kaffee));
		ImplicationNegativeConstraint c5_2 = new ImplicationNegativeConstraint("c5_2", new Type(Hausfarbe.Gruen), new Type(Nationalitaet.Daenisch));
		ImplicationNegativeConstraint c5_3 = new ImplicationNegativeConstraint("c5_3", new Type(Nationalitaet.Daenisch), new Type(Hausfarbe.Gruen));
		EquiConstraint c5_4 = new EquiConstraint("c5_4", new Type(Hausfarbe.Gruen), new Type(Hausposition.Vier));
		EquiConstraint c5_5 = new EquiConstraint("c5_5", new Type(Hausfarbe.Weiss), new Type(Hausposition.Fuenf));
		
		EquiConstraint c6_1 = new EquiConstraint("c6_1", new Type(Zigarettenmarke.Pallmall), new Type(Haustier.Vogel));
		ImplicationNegativeConstraint c6_2 = new ImplicationNegativeConstraint("c6_2", new Type(Zigarettenmarke.Pallmall), new Type(Nationalitaet.Schwedisch));
		ImplicationNegativeConstraint c6_3 = new ImplicationNegativeConstraint("c6_3", new Type(Nationalitaet.Schwedisch), new Type(Zigarettenmarke.Pallmall));
		
		EquiConstraint c7_1 = new EquiConstraint("c7_1", new Type(Hausposition.Drei), new Type(Getraenk.Milch));
		ImplicationNegativeConstraint c7_2 = new ImplicationNegativeConstraint("c7_2", new Type(Hausposition.Drei), new Type(Nationalitaet.Daenisch));
		ImplicationNegativeConstraint c7_3 = new ImplicationNegativeConstraint("c7_3", new Type(Hausposition.Drei), new Type(Hausfarbe.Gruen));
		ImplicationNegativeConstraint c7_4 = new ImplicationNegativeConstraint("c7_4", new Type(Nationalitaet.Daenisch), new Type(Hausposition.Drei));
		ImplicationNegativeConstraint c7_5 = new ImplicationNegativeConstraint("c7_5", new Type(Hausfarbe.Gruen), new Type(Hausposition.Drei));
		
		EquiConstraint c8_1 = new EquiConstraint("c8_1", new Type(Hausfarbe.Gelb), new Type(Zigarettenmarke.Dunhill));
		ImplicationNegativeConstraint c8_2 = new ImplicationNegativeConstraint("c8_2", new Type(Hausfarbe.Gelb), new Type(Haustier.Vogel));
		ImplicationNegativeConstraint c8_3 = new ImplicationNegativeConstraint("c8_3", new Type(Hausfarbe.Gelb), new Type(Nationalitaet.Schwedisch));
		ImplicationNegativeConstraint c8_4 = new ImplicationNegativeConstraint("c8_4", new Type(Haustier.Vogel), new Type(Hausfarbe.Gelb));
		ImplicationNegativeConstraint c8_5 = new ImplicationNegativeConstraint("c8_5", new Type(Nationalitaet.Schwedisch), new Type(Hausfarbe.Gelb));
		
		EquiConstraint c9_1 = new EquiConstraint("c9_1", new Type(Nationalitaet.Norwegisch), new Type(Hausposition.Eins));
		ImplicationNegativeConstraint c9_2 = new ImplicationNegativeConstraint("c9_2", new Type(Nationalitaet.Norwegisch), new Type(Hausfarbe.Weiss));
		ImplicationNegativeConstraint c9_3 = new ImplicationNegativeConstraint("c9_3", new Type(Hausfarbe.Weiss), new Type(Nationalitaet.Norwegisch));
		
		ImplicationNegativeConstraint c10_1 = new ImplicationNegativeConstraint("c10_1", new Type(Zigarettenmarke.Malboro), new Type(Haustier.Katze));
		ImplicationNegativeConstraint c10_2 = new ImplicationNegativeConstraint("c10_2", new Type(Haustier.Katze), new Type(Zigarettenmarke.Malboro));
		ImplicationNegativeConstraint c10_3 = new ImplicationNegativeConstraint("c10_3", new Type(Zigarettenmarke.Malboro), new Type(Hausfarbe.Gelb));
		ImplicationNegativeConstraint c10_4 = new ImplicationNegativeConstraint("c10_4", new Type(Hausfarbe.Gelb), new Type(Zigarettenmarke.Malboro));
		
		ImplicationNegativeConstraint c11_1 = new ImplicationNegativeConstraint("c11_1", new Type(Haustier.Pferd), new Type(Zigarettenmarke.Dunhill));
		ImplicationNegativeConstraint c11_2 = new ImplicationNegativeConstraint("c11_2", new Type(Zigarettenmarke.Dunhill), new Type(Haustier.Pferd));
		ImplicationNegativeConstraint c11_3 = new ImplicationNegativeConstraint("c11_3", new Type(Haustier.Pferd), new Type(Hausfarbe.Gelb));
		ImplicationNegativeConstraint c11_4 = new ImplicationNegativeConstraint("c11_4", new Type(Hausfarbe.Gelb), new Type(Haustier.Pferd));
		
		EquiConstraint c12_1 = new EquiConstraint("c12_1", new Type(Zigarettenmarke.Winfield), new Type(Getraenk.Bier));
		ImplicationNegativeConstraint c12_2 = new ImplicationNegativeConstraint("c12_2", new Type(Hausposition.Drei), new Type(Getraenk.Bier));
		ImplicationNegativeConstraint c12_3 = new ImplicationNegativeConstraint("c12_3", new Type(Getraenk.Bier), new Type(Hausposition.Drei));
		ImplicationNegativeConstraint c12_4 = new ImplicationNegativeConstraint("c12_4", new Type(Hausposition.Drei), new Type(Zigarettenmarke.Winfield));
		ImplicationNegativeConstraint c12_5 = new ImplicationNegativeConstraint("c12_5", new Type(Zigarettenmarke.Winfield), new Type(Hausposition.Drei));
		ImplicationNegativeConstraint c12_6 = new ImplicationNegativeConstraint("c12_6", new Type(Zigarettenmarke.Winfield), new Type(Nationalitaet.Daenisch));
		ImplicationNegativeConstraint c12_7 = new ImplicationNegativeConstraint("c12_7", new Type(Nationalitaet.Daenisch), new Type(Zigarettenmarke.Winfield));
		ImplicationNegativeConstraint c12_8 = new ImplicationNegativeConstraint("c12_8", new Type(Zigarettenmarke.Winfield), new Type(Hausfarbe.Gruen));
		ImplicationNegativeConstraint c12_9 = new ImplicationNegativeConstraint("c12_9", new Type(Hausfarbe.Gruen), new Type(Zigarettenmarke.Winfield));
		
		
		ImplicationNegativeConstraint c13_1 = new ImplicationNegativeConstraint("c13_1", new Type(Nationalitaet.Norwegisch), new Type(Hausfarbe.Blau));
		ImplicationNegativeConstraint c13_2 = new ImplicationNegativeConstraint("c13_2", new Type(Hausposition.Eins), new Type(Hausfarbe.Blau));
		ImplicationNegativeConstraint c13_3 = new ImplicationNegativeConstraint("c13_3", new Type(Hausfarbe.Blau), new Type(Hausposition.Eins));
		ImplicationNegativeConstraint c13_4 = new ImplicationNegativeConstraint("c13_4", new Type(Hausfarbe.Blau), new Type(Nationalitaet.Norwegisch));
		EquiConstraint c13_5 = new EquiConstraint("c13_5", new Type(Hausfarbe.Blau), new Type(Hausposition.Zwei));
		
		EquiConstraint c14_1 = new EquiConstraint("c14_1", new Type(Nationalitaet.Deutsch), new Type(Zigarettenmarke.Rothmanns));
		
		ImplicationNegativeConstraint c15_1 = new ImplicationNegativeConstraint("c15_1", new Type(Zigarettenmarke.Malboro), new Type(Getraenk.Wasser));
		ImplicationNegativeConstraint c15_2 = new ImplicationNegativeConstraint("c15_2", new Type(Getraenk.Wasser), new Type(Zigarettenmarke.Malboro));
	
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
		ArrayList<Constraint> nation_farbe = new ArrayList<>();
		nation_farbe.add(c1);
		nation_farbe.add(c5_2);
		nation_farbe.add(c5_3);
		nation_farbe.add(c8_3);
		nation_farbe.add(c8_5);
		nation_farbe.add(c9_2);
		nation_farbe.add(c9_3);
		nation_farbe.add(c13_1);
		nation_farbe.add(c13_4);
		
		ArrayList<Constraint> nation_haustier = new ArrayList<>();
		nation_haustier.add(c2);
		
		ArrayList<Constraint> nation_getraenk = new ArrayList<>();
		nation_getraenk.add(c3);
		
		ArrayList<Constraint> farbe_pos = new ArrayList<>();
		farbe_pos.add(c4_1);
		farbe_pos.add(c4_2);
		farbe_pos.add(c4_3);
		farbe_pos.add(c4_4);
		farbe_pos.add(c4_5);
		farbe_pos.add(c4_6);
		farbe_pos.add(c4_7);
		farbe_pos.add(c4_8);
		farbe_pos.add(c4_9);
		farbe_pos.add(c4_10);
		farbe_pos.add(c4_11);
		farbe_pos.add(c4_12);
		farbe_pos.add(c7_3);
		farbe_pos.add(c7_5);
		farbe_pos.add(c13_2);
		farbe_pos.add(c13_3);
		farbe_pos.add(c13_5);
		farbe_pos.add(c5_4);
		farbe_pos.add(c5_5);
		
		ArrayList<Constraint> farbe_getraenk = new ArrayList<>();
		farbe_getraenk.add(c5_1);
		
		ArrayList<Constraint> zigaretten_tier = new ArrayList<>();
		zigaretten_tier.add(c6_1);
		zigaretten_tier.add(c10_1);
		zigaretten_tier.add(c10_2);
		zigaretten_tier.add(c11_1);
		zigaretten_tier.add(c11_2);
		
		ArrayList<Constraint> zigaretten_nation = new ArrayList<>();
		zigaretten_nation.add(c6_2);
		zigaretten_nation.add(c6_3);
		zigaretten_nation.add(c12_6);
		zigaretten_nation.add(c12_7);
		zigaretten_nation.add(c14_1);
		
		ArrayList<Constraint> pos_getraenk = new ArrayList<>();
		pos_getraenk.add(c7_1);
		pos_getraenk.add(c12_2);
		pos_getraenk.add(c12_3);
		
		ArrayList<Constraint> pos_nation = new ArrayList<>();
		pos_nation.add(c7_2);
		pos_nation.add(c7_4);
		pos_nation.add(c9_1);
		
		ArrayList<Constraint> farbe_zigaretten = new ArrayList<>();
		farbe_zigaretten.add(c8_1);
		farbe_zigaretten.add(c10_3);
		farbe_zigaretten.add(c10_4);
		farbe_zigaretten.add(c12_8);
		farbe_zigaretten.add(c12_9);
		
		ArrayList<Constraint> farbe_tier = new ArrayList<>();
		farbe_tier.add(c8_2);
		farbe_tier.add(c8_4);
		farbe_tier.add(c11_3);
		farbe_tier.add(c11_4);
		
		ArrayList<Constraint> zigaretten_getraenk = new ArrayList<>();
		zigaretten_getraenk.add(c12_1);
		zigaretten_getraenk.add(c15_1);
		zigaretten_getraenk.add(c15_2);
		
		ArrayList<Constraint> zigaretten_pos = new ArrayList<>();
		zigaretten_pos.add(c12_4);
		zigaretten_pos.add(c12_5);
		
		//Kanten mit Constraints
		ConstraintNet constraintNet = new ConstraintNet();
		constraintNet.setConstraint(hausfarbe, nationalitaet, nation_farbe);
		constraintNet.setConstraint(zigaretten, nationalitaet, zigaretten_nation);
		constraintNet.setConstraint(hausposition, nationalitaet, pos_nation);
		constraintNet.setConstraint(getraenk, nationalitaet, nation_getraenk);
		constraintNet.setConstraint(haustier, nationalitaet, nation_haustier);
		
		constraintNet.setConstraint(zigaretten, hausfarbe, farbe_zigaretten);
		constraintNet.setConstraint(hausposition, hausfarbe, farbe_pos);
		constraintNet.setConstraint(getraenk, hausfarbe, farbe_getraenk);
		constraintNet.setConstraint(haustier, hausfarbe, farbe_tier);
		
		constraintNet.setConstraint(getraenk, zigaretten, zigaretten_getraenk);
		constraintNet.setConstraint(haustier, zigaretten, zigaretten_tier);
		
		constraintNet.setConstraint(getraenk, hausposition, pos_getraenk);
		
		constraintNet.setConstraint(zigaretten, hausposition, zigaretten_pos);
	
		
		//
		
		constraintNet.setConstraint(nationalitaet, hausfarbe, nation_farbe);
		constraintNet.setConstraint(nationalitaet, zigaretten, zigaretten_nation);
		constraintNet.setConstraint(nationalitaet, hausposition, pos_nation);
		constraintNet.setConstraint(nationalitaet, getraenk, nation_getraenk);
		constraintNet.setConstraint(nationalitaet, haustier, nation_haustier);
		
		constraintNet.setConstraint(hausfarbe, zigaretten, farbe_zigaretten);
		constraintNet.setConstraint(hausfarbe, hausposition, farbe_pos);
		constraintNet.setConstraint(hausfarbe, getraenk, farbe_getraenk);
		constraintNet.setConstraint(hausfarbe, haustier, farbe_tier);
		
		constraintNet.setConstraint(zigaretten, getraenk, zigaretten_getraenk);
		constraintNet.setConstraint(zigaretten, haustier, zigaretten_tier);
		
		constraintNet.setConstraint(hausposition, getraenk, pos_getraenk);
		
		constraintNet.setConstraint(hausposition, zigaretten, zigaretten_pos);
		
		return constraintNet;
	}

}
