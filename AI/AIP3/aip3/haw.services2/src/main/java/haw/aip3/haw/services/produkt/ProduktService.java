package haw.aip3.haw.services.produkt;

import haw.aip3.haw.entities.fertigungsverwaltung.Fertigungsauftrag;
import haw.aip3.haw.entities.produkt.Arbeitsplan;
import haw.aip3.haw.entities.produkt.Bauteil;
import haw.aip3.haw.entities.produkt.Stueckliste;
import haw.aip3.haw.entities.produkt.StuecklistenPosition;
import haw.aip3.haw.entities.produkt.Vorgang;
import haw.aip3.haw.entities.produkt.Vorgang.VorgangArtTyp;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface ProduktService {

	public Bauteil erstelleKomplexesBauteil(String name,
			Stueckliste stueckliste, Arbeitsplan arbeitsplan);

	public Bauteil erstelleEinfachesBauteil(String name);

	public Bauteil findeBauteil(String name);

	public Bauteil getBauteil(Fertigungsauftrag fa);

	public List<Arbeitsplan> erstelleArbeitsplaene(Fertigungsauftrag fa);

	public Bauteil getBauteil(Bauteil b);

	public void komplexesBauteilBauen(List<Arbeitsplan> lap);

	public Vorgang erstelleVorgang(VorgangArtTyp typ, long ruestzeit,
			long maschinenzeit, long personenzeit);

	public Arbeitsplan erstelleArbeitsplan(List<Vorgang> vorgaenge);

	public Arbeitsplan getArbeitsplan(Long iD);

	public StuecklistenPosition erstelleStuecklistenPosition(String name,
			int menge, Bauteil bauteil);

	public StuecklistenPosition getStuecklistenPosition(String name);

	public Stueckliste erstelleStueckliste(String name, Date gueltigAb,
			Date gueltigBis, Set<StuecklistenPosition> position);

	public Stueckliste getStueckliste(String name);

}
