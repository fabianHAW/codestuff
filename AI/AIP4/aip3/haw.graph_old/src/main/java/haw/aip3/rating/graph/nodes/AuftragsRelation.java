package haw.aip3.rating.graph.nodes;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
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
	private BauteilNode produkt;

	private String stadt;
	private double preis;
	
	// internal use only
	protected AuftragsRelation() {
	}

	public AuftragsRelation(GeschaeftspartnerNode kunde, BauteilNode produkt) {
		this.kunde = kunde;
		this.produkt = produkt;
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

	public BauteilNode getProdukt() {
		return produkt;
	}

	protected void setProdukt(BauteilNode produkt) {
		this.produkt = produkt;
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
				+ ", produkt=" + getProdukt().getProduktName() + ", stadt="
				+ stadt + "]";
	}
}