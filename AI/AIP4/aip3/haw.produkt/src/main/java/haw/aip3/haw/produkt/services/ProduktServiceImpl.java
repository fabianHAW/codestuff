package haw.aip3.haw.produkt.services;

import haw.aip3.haw.base.entities.IArbeitsplan;
import haw.aip3.haw.base.entities.IBauteil;
import haw.aip3.haw.base.entities.IFertigungsauftrag;
import haw.aip3.haw.base.entities.IStueckliste;
import haw.aip3.haw.base.entities.IStuecklistenPosition;
import haw.aip3.haw.base.entities.IVorgang;
import haw.aip3.haw.produkt.entities.Arbeitsplan;
import haw.aip3.haw.produkt.entities.Bauteil;
import haw.aip3.haw.produkt.entities.EinfachesBauteil;
import haw.aip3.haw.produkt.entities.KomplexesBauteil;
import haw.aip3.haw.produkt.entities.Stueckliste;
import haw.aip3.haw.produkt.entities.StuecklistenPosition;
import haw.aip3.haw.produkt.entities.Vorgang;
import haw.aip3.haw.produkt.entities.Vorgang.VorgangArtTyp;
import haw.aip3.haw.produkt.repositories.ArbeitsplanRepository;
import haw.aip3.haw.produkt.repositories.BauteilRepository;
import haw.aip3.haw.produkt.repositories.StuecklisteRepository;
import haw.aip3.haw.produkt.repositories.StuecklistenPostionRepository;
import haw.aip3.haw.produkt.repositories.VorgangRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProduktServiceImpl implements ProduktService {

	@Autowired
	private BauteilRepository bauteilRepo;

	@Autowired
	ArbeitsplanRepository arbeitsplanRepo;
//
//	@Autowired
//	private FertigungsauftragRepository fertigungsRepo;

	@Autowired
	private VorgangRepository vorgangRepo;

	@Autowired
	private StuecklistenPostionRepository stuecklistenPositionRepo;

	@Autowired
	private StuecklisteRepository stuecklisteRepo;

	@Override
	public Bauteil erstelleKomplexesBauteil(String name,
			IStueckliste stueckliste, IArbeitsplan arbeitsplan) {
		return this.bauteilRepo.save(new KomplexesBauteil(name, stueckliste,
				arbeitsplan));
	}

	@Override
	public Bauteil erstelleEinfachesBauteil(String name) {
		return this.bauteilRepo.save(new EinfachesBauteil(name));
	}

	@Override
	public Bauteil findeBauteil(String name) {
		return this.bauteilRepo.findByName(name).get(0);
	}
//
//	@Override
//	public Bauteil getBauteil(IFertigungsauftrag fa) {
//		return this.fertigungsRepo.findOne(fa.getNr()).getKundenAuftrag()
//				.getAngebot().getBauteil();
//	}

	@Override
	public Bauteil getBauteil(IBauteil b) {
		return this.bauteilRepo.findOne(b.getBauteilNr());
	}

	@Override
	public void komplexesBauteilBauen(List<IArbeitsplan> lap) {

		for (int i = 0; i < lap.size(); i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<IArbeitsplan> erstelleArbeitsplaene(IFertigungsauftrag fa) {
		if (fa.getBauteil() == null)
			return new ArrayList<IArbeitsplan>(getPlaene(fa.getKundenAuftrag()
					.getAngebot().getBauteil()));
		return new ArrayList<IArbeitsplan>(getPlaene(fa.getBauteil()));
	}

	private List<IArbeitsplan> getPlaene(IBauteil iBauteil) {
		List<IArbeitsplan> plaene = new ArrayList<IArbeitsplan>();
		if (iBauteil != null && iBauteil.hasStueckliste()) {
			plaene.add(((KomplexesBauteil) iBauteil).getArbeitsplan());
			for (IStuecklistenPosition p : ((KomplexesBauteil) iBauteil)
					.getStueckliste().getPosition()) {
				plaene.addAll(getPlaene(p.getBauteil()));
			}
		}
		return plaene;
	}

	@Override
	public Arbeitsplan erstelleArbeitsplan(List<IVorgang> vorgaenge) {
		return this.arbeitsplanRepo.save(new Arbeitsplan(vorgaenge));
	}

	@Override
	public Arbeitsplan getArbeitsplan(Long iD) {
		return this.arbeitsplanRepo.findOne(iD);
	}

	@Override
	public Vorgang erstelleVorgang(VorgangArtTyp typ, long ruestzeit,
			long maschinenzeit, long personenzeit) {
		return this.vorgangRepo.save(new Vorgang(typ, ruestzeit, maschinenzeit,
				personenzeit));
	}

	@Override
	public StuecklistenPosition erstelleStuecklistenPosition(String name,
			int menge, IBauteil b) {
		return this.stuecklistenPositionRepo.save(new StuecklistenPosition(
				name, menge, b));
	}

	@Override
	public StuecklistenPosition getStuecklistenPosition(String name) {
		return this.stuecklistenPositionRepo.findByName(name).get(0);
	}

	@Override
	public Stueckliste erstelleStueckliste(String name, Date gueltigAb,
			Date gueltigBis, Set<IStuecklistenPosition> position) {
		return this.stuecklisteRepo.save(new Stueckliste(name, gueltigAb,
				gueltigBis, position));
	}

	@Override
	public Stueckliste getStueckliste(String name) {
		return this.stuecklisteRepo.findByName(name);
	}

}
