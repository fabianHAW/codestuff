package haw.aip3.rating.graph.repository;



import haw.aip3.haw.graph.rating.nodes.BauteilNode;

import org.springframework.data.neo4j.repository.GraphRepository;

public interface BauteilGraphRepository extends GraphRepository<BauteilNode> {

	BauteilNode findByDbid(Long id);



}