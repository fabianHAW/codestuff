package mps.rating.config;

import mps.config.config.AppConfiguration;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.JtaTransactionManagerFactoryBean;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
@EnableNeo4jRepositories(basePackages = AppNeo4JConfiguration.BASE_PKG+".repository")
public class AppNeo4JConfiguration extends Neo4jConfiguration {
	
	public static final String BASE_PKG = AppConfiguration.PKG_PREFIX+".rating.graph";
	
	public AppNeo4JConfiguration() {
		super.setBasePackage(BASE_PKG+".nodes");
	}

	@Bean(destroyMethod = "shutdown")
	public GraphDatabaseService graphDatabaseService() {
		return new GraphDatabaseFactory().newEmbeddedDatabase("target/mps.db");
	}
	
	// in case the database should be external 
//    @Bean
//    public GraphDatabaseService graphDatabaseService() {
//        return new SpringRestGraphDatabase("http://localhost:7474/db/data/");
//    }
	

    @Autowired
    LocalContainerEntityManagerFactoryBean entityManagerFactory;


    @Override // this is important! We need a ChainedTransactionManager otherwise JPA Commands will fail as no commit will be done.
    @Bean(name = "transactionManager")
    public PlatformTransactionManager neo4jTransactionManager() throws Exception {
        return new ChainedTransactionManager(new JpaTransactionManager(entityManagerFactory.getObject()),
                new JtaTransactionManagerFactoryBean(graphDatabaseService()).getObject());
    }
	
}
