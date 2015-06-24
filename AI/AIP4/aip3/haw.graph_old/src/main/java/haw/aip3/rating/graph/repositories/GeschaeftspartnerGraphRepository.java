package haw.aip3.rating.graph.repositories;

import haw.aip3.rating.dto.SalesData;
import haw.aip3.rating.graph.nodes.AuftragsRelation;
import haw.aip3.rating.graph.nodes.BauteilNode;
import haw.aip3.rating.graph.nodes.GeschaeftspartnerNode;

import java.util.Collection;








import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.annotation.QueryResult;
import org.springframework.data.neo4j.annotation.ResultColumn;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

public interface GeschaeftspartnerGraphRepository extends GraphRepository<GeschaeftspartnerNode> {

	// TODO: Replace with @Param("stadt") when Spring Data Neo4j supports names vs. positional arguments
	Collection<GeschaeftspartnerNode> findByStadt(@Param("0") String stadt);
	
	

	@Query("MATCH (geschaeftspartner:GeschaeftspartnerNode{stadt:{0}}), geschaeftspartner-[bestellung:"+AuftragsRelation.RELATIONSHIP_TYPE+"]->(bauteil) RETURN bauteil, SUM(bestellung.preis)")
	Iterable<SalesDataImpl> showProductSalesByCity(String stadt);
	
	@QueryResult
	public class SalesDataImpl implements SalesData {
		@ResultColumn("bauteil")
		private BauteilNode bauteil;

		@ResultColumn("SUM(bestellung.preis)")
		private Integer count;

	
		public void setProdukt(BauteilNode bauteil) {
			this.bauteil = bauteil;
		}

		public void setCount(Integer count) {
			this.count = count;
		}

		public Integer getCount() {
			return count;
		}

		@Override
		public String toString() {
			return "SalesData [bauteil=" + getBauteil() + ", count=" + count + "]";
		}

		@Override
		public BauteilNode getBauteil() {
			// TODO Auto-generated method stub
			return bauteil;
		}
		
		
	}

	public GeschaeftspartnerNode findByDbid(Long id);
}