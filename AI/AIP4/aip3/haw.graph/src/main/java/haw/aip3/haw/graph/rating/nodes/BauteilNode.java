package haw.aip3.haw.graph.rating.nodes;

import org.springframework.data.neo4j.annotation.GraphProperty;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class BauteilNode extends BaseNode {
	
	@Indexed 
	@GraphProperty
	private String bauteilName;

	protected BauteilNode() {}
	
	public BauteilNode(Long dbID, String name) {
		super(dbID);
		this.bauteilName = name;
	}

	public String getProduktName() {
		return bauteilName;
	}
	
	public void setProduktName(String bauteilName) {
		this.bauteilName = bauteilName;
	}

	@Override
	public String toString() {
		return "BauteilNode [id=" + id + ", name=" + getProduktName() + "]";
	}
	
}
