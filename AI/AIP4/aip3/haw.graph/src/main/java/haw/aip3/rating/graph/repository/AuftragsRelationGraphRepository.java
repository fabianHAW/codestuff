package haw.aip3.rating.graph.repository;



import haw.aip3.haw.graph.rating.nodes.AuftragsRelation;

import org.springframework.data.neo4j.repository.GraphRepository;

public interface AuftragsRelationGraphRepository extends GraphRepository<AuftragsRelation> {

//    // TODO: Replace with @Param("name") when Spring Data Neo4j supports names vs. positional arguments
//    List<AuftragsPositionNode> findByLastName(@Param("0") String name);

}