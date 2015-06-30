package haw.aip3.haw.services.geschaeftspartner;

import haw.aip3.haw.entities.geschaeftspartner.Geschaeftspartner;


public interface GeschaeftspartnerService {



	Geschaeftspartner createGeschaeftspartner(String name, String stadt);

	Geschaeftspartner findGeschaeftspartner(long gpNr);

	boolean deleteGescheaftspartner(long gpNr);

}
