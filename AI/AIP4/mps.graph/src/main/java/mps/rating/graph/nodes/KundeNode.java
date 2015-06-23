package mps.rating.graph.nodes;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedToVia;

@NodeEntity
public class KundeNode extends BaseNode {

	@Indexed 
	private String stadt;
	
	@Indexed 
	private String kundenName;
	
	@RelatedToVia(type = AuftragsPositionRelation.RELATIONSHIP_TYPE, direction = Direction.OUTGOING)
    Set<AuftragsPositionRelation> bestellungen = new HashSet<>();
	
	protected KundeNode(){}
	
	public KundeNode(Long id, String name, String stadt){
		super(id);
		this.kundenName = name;
		this.stadt = stadt;
	}

	public String getStadt() {
		return stadt;
	}

	public String getKundenName() {
		return kundenName;
	}

	public Set<AuftragsPositionRelation> getBestellt() {
		return bestellungen;
	}

	public void setBestellt(Set<AuftragsPositionRelation> bestellungen) {
		this.bestellungen = bestellungen;
	}

	public AuftragsPositionRelation addBestellung(ProduktNode produkt, int anzahl) {
		for(AuftragsPositionRelation r: getBestellt()) {
			if(r.getProdukt().equals(produkt)) {
				r.setAnzahl(r.getAnzahl()+anzahl);
				return r;
			}
		}
		AuftragsPositionRelation r = new AuftragsPositionRelation(this, produkt);
		r.setAnzahl(anzahl);
		getBestellt().add(r);
		return r;
	}

	@Override
	public String toString() {
		return "KundeNode [id=" + id + ", stadt=" + stadt + ", name=" + kundenName
				+ ", bestellungen=" + bestellungen + "]";
	}

}
