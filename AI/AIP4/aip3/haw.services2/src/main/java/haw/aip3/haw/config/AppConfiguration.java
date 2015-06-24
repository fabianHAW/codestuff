package haw.aip3.haw.config;

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