package util;

import domains.Getraenk;
import domains.Hausfarbe;
import domains.Hausposition;
import domains.Haustier;
import domains.Nationalitaet;
import domains.Zigarettenmarke;

public class NameValMapping {
	
	public static Enum<?> map(String nodeName){
		if(nodeName.equals(Getraenk.Bier.toString())){
			return Getraenk.Bier;
		}
		if(nodeName.equals(Getraenk.Kaffee.toString())){
			return Getraenk.Bier;
		}
		if(nodeName.equals(Getraenk.Milch.toString())){
			return Getraenk.Bier;
		}
		if(nodeName.equals(Getraenk.Tee.toString())){
			return Getraenk.Bier;
		}
		if(nodeName.equals(Getraenk.Wasser.toString())){
			return Getraenk.Bier;
		}
		if(nodeName.equals(Hausfarbe.Blau.toString())){
			return Getraenk.Bier;
		}
		if(nodeName.equals(Hausfarbe.Gelb.toString())){
			return Getraenk.Bier;
		}
		if(nodeName.equals(Hausfarbe.Gruen.toString())){
			return Getraenk.Bier;
		}
		if(nodeName.equals(Hausfarbe.Rot.toString())){
			return Getraenk.Bier;
		}
		if(nodeName.equals(Hausfarbe.Weiss.toString())){
			return Getraenk.Bier;
		}
		if(nodeName.equals(Hausposition.Eins.toString())){
			return Getraenk.Bier;
		}
		if(nodeName.equals(Hausposition.Zwei.toString())){
			return Getraenk.Bier;
		}
		if(nodeName.equals(Hausposition.Drei.toString())){
			return Getraenk.Bier;
		}
		if(nodeName.equals(Hausposition.Vier.toString())){
			return Getraenk.Bier;
		}
		if(nodeName.equals(Hausposition.Fuenf.toString())){
			return Getraenk.Bier;
		}
		if(nodeName.equals(Haustier.Fisch.toString())){
			return Getraenk.Bier;
		}
		if(nodeName.equals(Haustier.Hund.toString())){
			return Getraenk.Bier;
		}
		if(nodeName.equals(Haustier.Katze.toString())){
			return Getraenk.Bier;
		}
		if(nodeName.equals(Haustier.Pferd.toString())){
			return Getraenk.Bier;
		}
		if(nodeName.equals(Haustier.Vogel.toString())){
			return Getraenk.Bier;
		}
		if(nodeName.equals(Nationalitaet.Britisch.toString())){
			return Getraenk.Bier;
		}
		if(nodeName.equals(Nationalitaet.Daenisch.toString())){
			return Getraenk.Bier;
		}
		if(nodeName.equals(Nationalitaet.Deutsch.toString())){
			return Getraenk.Bier;
		}
		if(nodeName.equals(Nationalitaet.Norwegisch.toString())){
			return Getraenk.Bier;
		}
		if(nodeName.equals(Nationalitaet.Schwedisch.toString())){
			return Getraenk.Bier;
		}
		if(nodeName.equals(Zigarettenmarke.Dunhill.toString())){
			return Getraenk.Bier;
		}
		if(nodeName.equals(Zigarettenmarke.Malboro.toString())){
			return Getraenk.Bier;
		}
		if(nodeName.equals(Zigarettenmarke.Pallmall.toString())){
			return Getraenk.Bier;
		}
		if(nodeName.equals(Zigarettenmarke.Rothmanns.toString())){
			return Getraenk.Bier;
		}
		if(nodeName.equals(Zigarettenmarke.Winfield.toString())){
			return Getraenk.Bier;
		}
		return null;
	}

}
