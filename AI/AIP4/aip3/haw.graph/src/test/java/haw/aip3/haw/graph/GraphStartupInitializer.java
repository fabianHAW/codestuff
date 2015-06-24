package haw.aip3.haw.graph;

import haw.aip3.haw.graph.rating.nodes.AuftragsRelation;
import haw.aip3.haw.graph.rating.nodes.BauteilNode;
import haw.aip3.haw.graph.rating.nodes.GeschaeftspartnerNode;
import haw.aip3.rating.graph.repository.BauteilGraphRepository;
import haw.aip3.rating.graph.repository.GeschaeftspartnerGraphRepository;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class GraphStartupInitializer implements ApplicationListener<ContextRefreshedEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(GraphStartupInitializer.class);

	private static final int CUSTOMER_COUNT = 5;
	
	private static final int PRODUCT_COUNT 	= 10;
	
	@Autowired
	private BauteilGraphRepository produktGraphRepository;
	
	@Autowired
	private GeschaeftspartnerGraphRepository kundeGraphRepository;
	
	@Autowired 
	Neo4jTemplate template;

	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// startup of root context, refresh will trigger for initialization and refresh of context
		if (event.getApplicationContext().getParent() == null) {
			configure();
		}
	}

	private void configure() {
		LOGGER.info("setup graph.");
		//cleanup
		template.query("MATCH(n) OPTIONAL MATCH (n)-[r]-() DELETE n,r",null);

		List<GeschaeftspartnerNode> kunden = new ArrayList<>();
		List<BauteilNode> produkte = new ArrayList<>();
		
		// fixture
		for(int i=0; i<PRODUCT_COUNT; i++) {
			BauteilNode p = new BauteilNode((long)i, "Produkt "+i);
			produktGraphRepository.save(p);
			produkte.add(p);
		}
		for(int i=0; i<CUSTOMER_COUNT; i++) {
			String stadt = i%2==0?"Berlin":"Hamburg";
			GeschaeftspartnerNode k = new GeschaeftspartnerNode((long)i, "Geschaeftspartner "+i, stadt);
			kundeGraphRepository.save(k);
			kunden.add(k);
		}
		
		
		for(int i=0; i<10; i++) {
			GeschaeftspartnerNode kunde = kunden.get(i%CUSTOMER_COUNT);
			for(int j=0; j<(i%5); j++) {
				BauteilNode produkt = produkte.get((i+j)%PRODUCT_COUNT);
				AuftragsRelation bestellt = new AuftragsRelation(kunde, produkt);
				bestellt.setPreis((i+j)+2.76);
				kunde.getBestellt().add(bestellt);
			}
			kundeGraphRepository.save(kunde);
		}
	}
}
