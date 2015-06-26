package haw.aip3.haw.graph.rating.nodes;

import java.util.HashSet;
import java.util.Set;


import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedToVia;

@NodeEntity
public class GeschaeftspartnerNode extends BaseNode {

	@Indexed 
	private String stadt;
	
	@Indexed 
	private String name;
	
	@RelatedToVia(type = AuftragsRelation.RELATIONSHIP_TYPE, direction = Direction.OUTGOING)
    Set<AuftragsRelation> bestellungen = new HashSet<>();
	
	protected GeschaeftspartnerNode(){}
	
	public GeschaeftspartnerNode(Long id, String name, String stadt){
		super(id);
		this.name = name;
		this.stadt = stadt;
	}

	public String getStadt() {
		return stadt;
	}

	public String getKundenName() {
		return name;
	}

	public Set<AuftragsRelation> getBestellt() {
		return bestellungen;
	}

	public void setBestellt(Set<AuftragsRelation> bestellungen) {
		this.bestellungen = bestellungen;
	}
	
	public AuftragsRelation addBestellung(BauteilNode produkt, double preis) {
		AuftragsRelation r = new AuftragsRelation(this, produkt);
		r.setPreis(preis);
		getBestellt().add(r);
		return r;
	}

	@Override
	public String toString() {
		return "GeschaeftspartnerNode [id=" + id + ", stadt=" + stadt + ", name=" + name
				+ ", bestellungen=" + bestellungen + "]";
	}

}
