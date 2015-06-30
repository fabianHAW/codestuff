package haw.aip3.haw.services.fertigungsverwaltung;

import haw.aip3.haw.entities.auftragsverwaltung.KundenAuftrag;
import haw.aip3.haw.entities.fertigungsverwaltung.Fertigungsauftrag;


public interface FertigungService {

	/**
	 * Liefert einen Fertigungsauftrag
	 * @param a KundeAuftrag
	 * @return f Fertigungsauftrag
	 */
	public Fertigungsauftrag createFertigungsAuftrag(KundenAuftrag a);
	
	/**
	 * Speichert einen Fertigungsauftrag im Repository
	 * @param f Fertigungsauftrag
	 * @return true wenn erfolgreich, sonst false
	 */
	public boolean saveFertigungsAuftrag(Fertigungsauftrag f);
	
	/**
	 * Findet den Fertigungsauftrag mit der ID faNr
	 * @param faNr ID des Fertigungsauftrags
	 * @return true wenn vorhanden, sonst false
	 */
	public Fertigungsauftrag findFertigungsauftrag(long faNr);
	
	/**
	 * Löscht den Fertigungsauftrag mit der ID faNr
	 * @param faNr ID des Fertigungsauftrags
	 * @return true wenn erfolgreich, sonst false
	 */
	public boolean deleteFertigungsauftrag(long faNr);

}
