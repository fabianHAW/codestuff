package haw.aip3.haw.services.produkt;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import haw.aip3.haw.entities.fertigungsverwaltung.Fertigungsauftrag;
import haw.aip3.haw.entities.produkt.Arbeitsplan;
import haw.aip3.haw.entities.produkt.Bauteil;
import haw.aip3.haw.entities.produkt.EinfachesBauteil;
import haw.aip3.haw.entities.produkt.KomplexesBauteil;
import haw.aip3.haw.entities.produkt.Stueckliste;
import haw.aip3.haw.entities.produkt.StuecklistenPosition;
import haw.aip3.haw.repositories.fertigungsverwaltung.FertigungsauftragRepository;
import haw.aip3.haw.repositories.produkt.ArbeitsplanRepository;
import haw.aip3.haw.repositories.produkt.BauteilRepository;

@Service
public class ProduktServiceImpl implements ProduktService {

	@Autowired
	private BauteilRepository bauteilRepo;
	
	@Autowired ArbeitsplanRepository arbeitsplanRepo;
	
	@Autowired
	private FertigungsauftragRepository fertigungsRepo;


	@Override
	public void erstelleKomplexesBauteil(String name, Stueckliste stueckliste, Arbeitsplan arbeitsplan) {
		bauteilRepo.save(new KomplexesBauteil(name, stueckliste, arbeitsplan));
	}
	
	@Override
	public void erstelleEinfachesBauteil(String name) {
		this.bauteilRepo.save(new EinfachesBauteil(name));
	}

	@Override
	public Bauteil findeBauteil(String name) {
		return this.bauteilRepo.findByName(name);
	}

	@Override
	public Bauteil getBauteil(Fertigungsauftrag fa) {
		return this.fertigungsRepo.findOne(fa.getNr()).getBauteil();
	}

	@Override
	public Bauteil getBauteil(Bauteil b) {
		return this.bauteilRepo.findOne(b.getBauteilNr());
	}
	
	@Override
	public void komplexesBauteilBauen(List<Arbeitsplan> lap) {

		for(int i = 0; i < lap.size(); i++){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<Arbeitsplan> erstelleArbeitsplaene(Fertigungsauftrag fa) {
		// TODO Auto-generated method stub
		return new ArrayList<Arbeitsplan>(getPlaene(fa.getBauteil()));
	}
	
	private List<Arbeitsplan> getPlaene(Bauteil b){
		List<Arbeitsplan> plaene = new ArrayList<Arbeitsplan>();
		if(b != null && b.hasStueckliste()){
			plaene.add(((KomplexesBauteil)b).getArbeitsplan());
			for(StuecklistenPosition p : ((KomplexesBauteil)b).getStueckliste().getPosition()){
				plaene.addAll(getPlaene(p.getBauteil()));
			}
		}
		return plaene;
	}
	


}
