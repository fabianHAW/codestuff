package haw.aip3.haw.graph.rating.nodes;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.GraphProperty;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

@RelationshipEntity(type = AuftragsRelation.RELATIONSHIP_TYPE)
public class AuftragsRelation {

	public static final String RELATIONSHIP_TYPE = "HAT_GEKAUFT";

	@GraphId
	private Long id;

	@StartNode
	private GeschaeftspartnerNode kunde;

	@EndNode
	private BauteilNode bauteil;

	@GraphProperty
	private String stadt;
	
	@GraphProperty
	private double preis;
	
	// internal use only
	protected AuftragsRelation() {
	}

	public AuftragsRelation(GeschaeftspartnerNode kunde, BauteilNode bauteil) {
		this.kunde = kunde;
		this.bauteil = bauteil;
	}

	public String getStadt() {
		return stadt;
	}
	
	public void setPreis(double preis){
		this.preis = preis;
	}
	
	public double getPreis(){
		return preis;
	}

	public void setStadt(String stadt) {
		this.stadt = stadt;
	}

	public BauteilNode getBauteil() {
		return bauteil;
	}

	protected void setBauteil(BauteilNode bauteil) {
		this.bauteil = bauteil;
	}

	public GeschaeftspartnerNode getKunde() {
		return kunde;
	}

	protected void setKunde(GeschaeftspartnerNode kunde) {
		this.kunde = kunde;
	}

	@Override
	public String toString() {
		return "AuftragsPositionNode [kunde=" + getKunde().getKundenName()
				+ ", produkt=" + getBauteil().getProduktName() + ", stadt="
				+ stadt + "]";
	}
}