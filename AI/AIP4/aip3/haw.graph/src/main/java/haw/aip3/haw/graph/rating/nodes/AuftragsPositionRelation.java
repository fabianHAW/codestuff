package haw.aip3.haw.graph.rating.nodes;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

@RelationshipEntity(type = AuftragsPositionRelation.RELATIONSHIP_TYPE)
public class AuftragsPositionRelation {

	public static final String RELATIONSHIP_TYPE = "HAT_GEKAUFT";

	@GraphId
	private Long id;

	@StartNode
	private KundeNode kunde;

	@EndNode
	private ProduktNode produkt;

	private String stadt;

	// internal use only
	protected AuftragsPositionRelation() {
	}

	public AuftragsPositionRelation(KundeNode kunde, ProduktNode produkt) {
		this.kunde = kunde;
		this.produkt = produkt;
	}

	public String getStadt() {
		return stadt;
	}

	public void setStadt(String stadt) {
		this.stadt = stadt;
	}

	public ProduktNode getProdukt() {
		return produkt;
	}

	protected void setProdukt(ProduktNode produkt) {
		this.produkt = produkt;
	}

	public KundeNode getKunde() {
		return kunde;
	}

	protected void setKunde(KundeNode kunde) {
		this.kunde = kunde;
	}

	@Override
	public String toString() {
		return "AuftragsPositionNode [kunde=" + getKunde().getKundenName()
				+ ", produkt=" + getProdukt().getProduktName() + ", stadt="
				+ stadt + "]";
	}
}