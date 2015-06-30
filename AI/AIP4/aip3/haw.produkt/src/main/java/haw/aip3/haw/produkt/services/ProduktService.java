package haw.aip3.haw.produkt.services;

import haw.aip3.haw.base.entities.IArbeitsplan;
import haw.aip3.haw.base.entities.IBauteil;
import haw.aip3.haw.base.entities.IFertigungsauftrag;
import haw.aip3.haw.base.entities.IStueckliste;
import haw.aip3.haw.base.entities.IStuecklistenPosition;
import haw.aip3.haw.base.entities.IVorgang;
import haw.aip3.haw.produkt.entities.Arbeitsplan;
import haw.aip3.haw.produkt.entities.Bauteil;
import haw.aip3.haw.produkt.entities.Stueckliste;
import haw.aip3.haw.produkt.entities.StuecklistenPosition;
import haw.aip3.haw.produkt.entities.Vorgang;
import haw.aip3.haw.produkt.entities.Vorgang.VorgangArtTyp;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface ProduktService {

	public Bauteil erstelleKomplexesBauteil(String name,
			IStueckliste stueckliste, IArbeitsplan arbeitsplan);

	public Bauteil erstelleEinfachesBauteil(String name);

	public Bauteil findeBauteil(String name);
//
//	public Bauteil getBauteil(IFertigungsauftrag fa);

	public List<IArbeitsplan> erstelleArbeitsplaene(IFertigungsauftrag fa);

	public Bauteil getBauteil(IBauteil b);

	public void komplexesBauteilBauen(List<IArbeitsplan> lap);

	public Vorgang erstelleVorgang(VorgangArtTyp typ, long ruestzeit,
			long maschinenzeit, long personenzeit);

	public Arbeitsplan erstelleArbeitsplan(List<IVorgang> vorgaenge);

	public Arbeitsplan getArbeitsplan(Long iD);

	public StuecklistenPosition erstelleStuecklistenPosition(String name,
			int menge, IBauteil bauteil);

	public StuecklistenPosition getStuecklistenPosition(String name);

	public Stueckliste erstelleStueckliste(String name, Date gueltigAb,
			Date gueltigBis, Set<IStuecklistenPosition> position);

	public Stueckliste getStueckliste(String name);

}
