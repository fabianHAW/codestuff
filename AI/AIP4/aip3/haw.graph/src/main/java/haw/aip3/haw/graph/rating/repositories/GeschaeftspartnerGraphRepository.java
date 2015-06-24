package haw.aip3.haw.graph.rating.repositories;

import haw.aip3.haw.graph.rating.dto.SalesData;
import haw.aip3.haw.graph.rating.nodes.AuftragsRelation;
import haw.aip3.haw.graph.rating.nodes.BauteilNode;
import haw.aip3.haw.graph.rating.nodes.GeschaeftspartnerNode;

import java.util.Collection;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.annotation.QueryResult;
import org.springframework.data.neo4j.annotation.ResultColumn;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

public interface GeschaeftspartnerGraphRepository extends GraphRepository<GeschaeftspartnerNode> {

	// TODO: Replace with @Param("stadt") when Spring Data Neo4j supports names vs. positional arguments
	Collection<GeschaeftspartnerNode> findByStadt(@Param("0") String stadt);
	
	
	@Query("MATCH (b:count) RETURN b")
	Iterable<SalesDataImpl> showProduct(String stadt);
	
	@Query("MATCH (geschaeftspartner:GeschaeftspartnerNode{stadt:{0}}), geschaeftspartner-[bestellung:"+AuftragsRelation.RELATIONSHIP_TYPE+"]->(bauteil) RETURN bauteil, SUM(bestellung.preis)")
	Iterable<SalesDataImpl> showProductSalesByCity(String stadt);
	
	@QueryResult
	public class SalesDataImpl implements SalesData {
		
		public SalesDataImpl(){
			System.out.println("IN: " + this.getClass().getCanonicalName());
		}
		
		@ResultColumn("bauteil")
		private BauteilNode bauteil;

		@ResultColumn("SUM(bestellung.preis)")
		private Integer count;

		public BauteilNode getBauteil() {
			return bauteil;
		}
		
		public void setBauteil(BauteilNode bauteil) {
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
		
	}

	public GeschaeftspartnerNode findByDbid(Long id);
}