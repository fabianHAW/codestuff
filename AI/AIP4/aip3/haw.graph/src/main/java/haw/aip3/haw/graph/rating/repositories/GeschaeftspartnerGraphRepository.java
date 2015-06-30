package haw.aip3.haw.graph.rating.repositories;

import haw.aip3.haw.graph.rating.dto.SalesData;
import haw.aip3.haw.graph.rating.dto.SalesDataForOftenSalesPerRegion;
import haw.aip3.haw.graph.rating.dto.SalesDataForRelatedProducts;
import haw.aip3.haw.graph.rating.nodes.AuftragsRelation;
import haw.aip3.haw.graph.rating.nodes.BauteilNode;
import haw.aip3.haw.graph.rating.nodes.GeschaeftspartnerNode;

import java.util.Collection;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.annotation.QueryResult;
import org.springframework.data.neo4j.annotation.ResultColumn;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

public interface GeschaeftspartnerGraphRepository extends
		GraphRepository<GeschaeftspartnerNode> {

	// TODO: Replace with @Param("stadt") when Spring Data Neo4j supports names
	// vs. positional arguments
	Collection<GeschaeftspartnerNode> findByStadt(@Param("0") String stadt);

	@Query("MATCH (b:count) RETURN b")
	Iterable<SalesDataImpl> showProduct(String stadt);

	@Query("MATCH (geschaeftspartner:GeschaeftspartnerNode{stadt:{0}}), geschaeftspartner-[bestellung:"
			+ AuftragsRelation.RELATIONSHIP_TYPE
			+ "]->(bauteil) RETURN bauteil, SUM(bestellung.preis)")
	Iterable<SalesDataImpl> showProductSalesByCity(String stadt);

	@Query("MATCH(produkt)<-[r]-(geschaeftspartner) "
			+ "where r.stadt=\"Berlin\" RETURN r.stadt as Stadt, "
			+ "produkt.name as Produkt, count(produkt) as Verkäufe "
			+ "ORDER BY count(produkt) DESC  UNION ALL  "
			+ "MATCH(produkt)<-[r]-(geschaeftspartner) where r.stadt=\"Hamburg\" "
			+ "RETURN r.stadt as Stadt, produkt.name as Produkt, "
			+ "count(produkt) as Verkäufe")
	Iterable<SalesDataForOftenSalesPerRegionImpl> showOftenProductsalesByCity();
	
	@Query("match(gp1)-->(pr1) match(gp2)-->(pr2) "
			+ "where not(pr1.name=pr2.name) and gp1.name=gp2.name "
			+ "return pr1.name as ProduktA, pr2.name as ProduktB, count(pr2) as ProduktA_Und_ProduktB "
			+ "order by pr1.name, pr2.name, ProduktA_Und_ProduktB")
	Iterable<SalesDataForRelatedProductsImpl> showRelatedProducts();

	@QueryResult
	public class SalesDataImpl implements SalesData {

		public SalesDataImpl() {

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
			return "SalesData [bauteil=" + getBauteil() + ", count=" + count
					+ "]";
		}

	}

	public GeschaeftspartnerNode findByDbid(Long id);

	@QueryResult
	public class SalesDataForOftenSalesPerRegionImpl implements
			SalesDataForOftenSalesPerRegion {

		@ResultColumn("Stadt")
		private String stadt;

		@ResultColumn("Produkt")
		private String produkt;

		@ResultColumn("Verkäufe")
		private String verkaeufe;

		public String getStadt() {
			return stadt;
		}

		public void setStadt(String stadt) {
			this.stadt = stadt;
		}

		public String getProdukt() {
			return produkt;
		}

		public void setProdukt(String produkt) {
			this.produkt = produkt;
		}

		public String getVerkaeufe() {
			return verkaeufe;
		}

		public void setVerkaeufe(String verkaeufe) {
			this.verkaeufe = verkaeufe;
		}

		@Override
		public String toString() {
			return "SalesData [Stadt= " + getStadt() + " Produkt= "
					+ getProdukt() + " Verkäufe= " + getVerkaeufe() + "]";
		}

	}
	
	@QueryResult
	public class SalesDataForRelatedProductsImpl implements
			SalesDataForRelatedProducts {

		@ResultColumn("ProduktA")
		String productA;
		
		@ResultColumn("ProduktB")
		String productB;
		
		public void setProductA(String productA) {
			this.productA = productA;
		}

		public void setProductB(String productB) {
			this.productB = productB;
		}

		public void setProductA_with_productB(Integer productA_with_productB) {
			this.productA_with_productB = productA_with_productB;
		}

		@ResultColumn("ProduktA_Und_ProduktB")
		Integer productA_with_productB;

		@Override
		public String toString() {
			return "SalesData [ProduktA= " + getPrduktA() + " ProduktB= "
					+ getProduktB() + " ProduktA_Und_ProduktB= " + getProduktA_With_B() + "]";
		}

		@Override
		public String getPrduktA() {
			// TODO Auto-generated method stub
			return productA;
		}

		@Override
		public String getProduktB() {
			// TODO Auto-generated method stub
			return productB;
		}

		@Override
		public Integer getProduktA_With_B() {
			// TODO Auto-generated method stub
			return productA_with_productB;
		}

	}
}