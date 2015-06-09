package haw.aip3.haw.config;

import haw.aip3.haw.services.auftragsverwaltung.AuftragsService;
import haw.aip3.haw.services.auftragsverwaltung.AuftragsServiceImpl;
import haw.aip3.haw.services.fertigungsverwaltung.FertigungService;
import haw.aip3.haw.services.fertigungsverwaltung.FertiungServiceImpl;
import haw.aip3.haw.services.produkt.ProduktService;
import haw.aip3.haw.services.produkt.ProduktServiceImpl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Base configuration of Application referenced in web.xml or via spring setup
 *
 */
@Configuration
@ComponentScan(basePackages = "haw.aip3.haw")
public class AppConfiguration {
	
}