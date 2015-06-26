package haw.aip3.haw.graph.rating.nodes;

import org.springframework.data.neo4j.annotation.GraphProperty;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class BauteilNode extends BaseNode {
	
	@Indexed 
	@GraphProperty
	private String name;

	protected BauteilNode() {}
	
	public BauteilNode(Long dbID, String name) {
		super(dbID);
		this.name = name;
	}

	public String getProduktName() {
		return name;
	}
	
	public void setProduktName(String bauteilName) {
		this.name = bauteilName;
	}

	@Override
	public String toString() {
		return "BauteilNode [id=" + id + ", name=" + getProduktName() + "]";
	}
	
}
