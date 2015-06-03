package haw.aip3.haw.services;

import haw.aip3.haw.entities.Fertigungsauftrag;
import haw.aip3.haw.entities.KundenAuftrag;


public interface FertigungService {

	/**
	 * Liefert einen Fertigungsauftrag
	 * @param a KundeAuftrag
	 * @return f Fertigungsauftrag
	 */
	public Fertigungsauftrag createFertigungsAuftrag(KundenAuftrag a);
	public boolean saveFertigungsAuftrag(Fertigungsauftrag f);
	public Fertigungsauftrag findFertigungsauftrag(long faNr);
	public boolean deleteFertigungsauftrag(long faNr);

}
