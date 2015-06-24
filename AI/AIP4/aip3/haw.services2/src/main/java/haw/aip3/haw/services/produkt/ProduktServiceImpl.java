package haw.aip3.haw.services.produkt;

import haw.aip3.haw.entities.fertigungsverwaltung.Fertigungsauftrag;
import haw.aip3.haw.entities.produkt.Arbeitsplan;
import haw.aip3.haw.entities.produkt.Bauteil;
import haw.aip3.haw.entities.produkt.EinfachesBauteil;
import haw.aip3.haw.entities.produkt.KomplexesBauteil;
import haw.aip3.haw.entities.produkt.Stueckliste;
import haw.aip3.haw.entities.produkt.StuecklistenPosition;
import haw.aip3.haw.entities.produkt.Vorgang;
import haw.aip3.haw.entities.produkt.Vorgang.VorgangArtTyp;
import haw.aip3.haw.repositories.fertigungsverwaltung.FertigungsauftragRepository;
import haw.aip3.haw.repositories.fertigungsverwaltung.VorgangRepository;
import haw.aip3.haw.repositories.produkt.ArbeitsplanRepository;
import haw.aip3.haw.repositories.produkt.BauteilRepository;
import haw.aip3.haw.repositories.produkt.StuecklisteRepository;
import haw.aip3.haw.repositories.produkt.StuecklistenPostionRepository;

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

	@Autowired
	private FertigungsauftragRepository fertigungsRepo;

	@Autowired
	private VorgangRepository vorgangRepo;

	@Autowired
	private StuecklistenPostionRepository stuecklistenPositionRepo;

	@Autowired
	private StuecklisteRepository stuecklisteRepo;

	@Override
	public Bauteil erstelleKomplexesBauteil(String name,
			Stueckliste stueckliste, Arbeitsplan arbeitsplan) {
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

	@Override
	public Bauteil getBauteil(Fertigungsauftrag fa) {
		return this.fertigungsRepo.findOne(fa.getNr()).getKundenAuftrag()
				.getAngebot().getBauteil();
	}

	@Override
	public Bauteil getBauteil(Bauteil b) {
		return this.bauteilRepo.findOne(b.getBauteilNr());
	}

	@Override
	public void komplexesBauteilBauen(List<Arbeitsplan> lap) {

		for (int i = 0; i < lap.size(); i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<Arbeitsplan> erstelleArbeitsplaene(Fertigungsauftrag fa) {
		if (fa.getBauteil() == null)
			return new ArrayList<Arbeitsplan>(getPlaene(fa.getKundenAuftrag()
					.getAngebot().getBauteil()));
		return new ArrayList<Arbeitsplan>(getPlaene(fa.getBauteil()));
	}

	private List<Arbeitsplan> getPlaene(Bauteil b) {
		List<Arbeitsplan> plaene = new ArrayList<Arbeitsplan>();
		if (b != null && b.hasStueckliste()) {
			plaene.add(((KomplexesBauteil) b).getArbeitsplan());
			for (StuecklistenPosition p : ((KomplexesBauteil) b)
					.getStueckliste().getPosition()) {
				plaene.addAll(getPlaene(p.getBauteil()));
			}
		}
		return plaene;
	}

	@Override
	public Arbeitsplan erstelleArbeitsplan(List<Vorgang> vorgaenge) {
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
			int menge, Bauteil b) {
		return this.stuecklistenPositionRepo.save(new StuecklistenPosition(
				name, menge, b));
	}

	@Override
	public StuecklistenPosition getStuecklistenPosition(String name) {
		return this.stuecklistenPositionRepo.findByName(name).get(0);
	}

	@Override
	public Stueckliste erstelleStueckliste(String name, Date gueltigAb,
			Date gueltigBis, Set<StuecklistenPosition> position) {
		return this.stuecklisteRepo.save(new Stueckliste(name, gueltigAb,
				gueltigBis, position));
	}

	@Override
	public Stueckliste getStueckliste(String name) {
		return this.stuecklisteRepo.findByName(name);
	}

}
