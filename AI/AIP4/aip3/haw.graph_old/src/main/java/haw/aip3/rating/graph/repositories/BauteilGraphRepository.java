package haw.aip3.rating.graph.repositories;



import haw.aip3.rating.graph.nodes.BauteilNode;

import org.springframework.data.neo4j.repository.GraphRepository;

public interface BauteilGraphRepository extends GraphRepository<BauteilNode> {

	BauteilNode findByDbid(Long id);



}