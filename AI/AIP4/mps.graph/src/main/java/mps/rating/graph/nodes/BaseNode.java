package mps.rating.graph.nodes;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class BaseNode {
	
	public static final String DB_ID_PROPERTY = "dbID";
	
	@GraphId 
	protected Long id;
	
	@Indexed
	protected Long dbid;

	protected BaseNode(){}
	
	protected BaseNode(Long dbID){
		this.dbid = dbID;
	}

	public Long getId() {
		return id;
	}
	
	public Long getDbid(){
		return dbid;
	}
	

	@Override
	public boolean equals(Object other) {
		if(!(other instanceof BaseNode)) return false;
		if(id==null) throw new RuntimeException("Entity has not been saved.");
		return id.equals(((BaseNode)other).getId());
	}
	
	@Override
	public int hashCode() {
		if(id==null) throw new RuntimeException("Entity has not been saved.");
		return id.hashCode();
	}
}
