package haw.aip3.haw.geschaeftspartner.services;

import haw.aip3.haw.geschaeftspartner.entities.Geschaeftspartner;


public interface GeschaeftspartnerService {



	Geschaeftspartner createGeschaeftspartner(String name, String stadt);

	Geschaeftspartner findGeschaeftspartner(long gpNr);

	boolean deleteGescheaftspartner(long gpNr);

}
