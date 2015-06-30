package haw.aip3.haw.auftragsverwaltung;

import haw.aip3.haw.config.AppConfiguration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

//@Configuration
@PropertySource("classpath:application.properties") // this allows us to configure our database via the application.properties file
@ComponentScan(basePackageClasses={
		AppConfiguration.class,
		StartupInitializerAuftrag.class
		})
public class TestConfig {
}
