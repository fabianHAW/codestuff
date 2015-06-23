package mps.rating.graph.nodes;

import org.springframework.data.neo4j.annotation.GraphProperty;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class ProduktNode extends BaseNode {
	
	@Indexed 
	@GraphProperty
	private String produktName;

	protected ProduktNode() {}
	
	public ProduktNode(Long dbID, String name) {
		super(dbID);
		this.produktName = name;
	}

	public String getProduktName() {
		return produktName;
	}
	
	public void setProduktName(String produktName) {
		this.produktName = produktName;
	}

	@Override
	public String toString() {
		return "ProduktNode [id=" + id + ", name=" + getProduktName() + "]";
	}
	
}
