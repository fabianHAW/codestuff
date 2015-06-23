package mps.rating.graph.repository;

import mps.rating.graph.nodes.AuftragsPositionRelation;

import org.springframework.data.neo4j.repository.GraphRepository;

public interface AuftragsPositionGraphRepository extends GraphRepository<AuftragsPositionRelation> {

//    // TODO: Replace with @Param("name") when Spring Data Neo4j supports names vs. positional arguments
//    List<AuftragsPositionNode> findByLastName(@Param("0") String name);

}