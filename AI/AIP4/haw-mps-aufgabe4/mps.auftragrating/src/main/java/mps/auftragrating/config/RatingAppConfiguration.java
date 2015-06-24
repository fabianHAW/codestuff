package mps.auftragrating.config;

import mps.auftrag.services.AuftragService;
import mps.auftragrating.services.RatingAuftragServiceImpl;
import mps.config.config.AppConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RatingAppConfiguration extends AppConfiguration {
    
	// make sure we use the new version of our AuftragService, which updates the graphDB.
	@Bean
	public AuftragService auftragService(){
		return new RatingAuftragServiceImpl();
	}
}