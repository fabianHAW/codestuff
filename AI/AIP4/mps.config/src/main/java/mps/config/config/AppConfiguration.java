package mps.config.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Base configuration of Application referenced in web.xml or via spring setup
 *
 */
@Configuration
@ComponentScan(
		basePackages={AppConfiguration.PKG_PREFIX+".*.config",// all configurations need to be in config
					  AppConfiguration.PKG_PREFIX+".*.tasks",// all complex orchestration services
					  AppConfiguration.PKG_PREFIX+".*.policies",// all policy components
					  AppConfiguration.PKG_PREFIX+".*.services",// all service components
					  AppConfiguration.PKG_PREFIX+".*.values"}) // all value object factories
public class AppConfiguration {
	// This is the base package all modules will use, i.e. PKG_PREFIX+<MODULE>+components, e.g. com.mondula.training.spring.auftrag.services
	public static final String PKG_PREFIX 	= "mps";
}