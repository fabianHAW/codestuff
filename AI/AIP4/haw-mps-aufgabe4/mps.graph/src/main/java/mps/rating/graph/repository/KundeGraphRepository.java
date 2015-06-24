package mps.rating.graph.repository;

import java.util.Collection;

import mps.rating.dto.SalesData;
import mps.rating.graph.nodes.AuftragsPositionRelation;
import mps.rating.graph.nodes.KundeNode;
import mps.rating.graph.nodes.ProduktNode;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.annotation.QueryResult;
import org.springframework.data.neo4j.annotation.ResultColumn;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

public interface KundeGraphRepository extends GraphRepository<KundeNode> {

	// TODO: Replace with @Param("stadt") when Spring Data Neo4j supports names vs. positional arguments
	Collection<KundeNode> findByStadt(@Param("0") String stadt);
	
	

	@Query("MATCH (kunde:KundeNode{stadt:{0}}), kunde-[bestellung:"+AuftragsPositionRelation.RELATIONSHIP_TYPE+"]->(produkt) RETURN produkt, SUM(bestellung.anzahl)")
	Iterable<SalesDataImpl> showProductSalesByCity(String stadt);
	
	@QueryResult
	public class SalesDataImpl implements SalesData {
		@ResultColumn("produkt")
		private ProduktNode produkt;

		@ResultColumn("SUM(bestellung.anzahl)")
		private Integer count;

		public ProduktNode getProdukt() {
			return produkt;
		}

		public void setProdukt(ProduktNode produkt) {
			this.produkt = produkt;
		}

		public void setCount(Integer count) {
			this.count = count;
		}

		public Integer getCount() {
			return count;
		}

		@Override
		public String toString() {
			return "SalesData [produkt=" + getProdukt() + ", count=" + count + "]";
		}
		
		
	}

	public KundeNode findByDbid(Long id);
}