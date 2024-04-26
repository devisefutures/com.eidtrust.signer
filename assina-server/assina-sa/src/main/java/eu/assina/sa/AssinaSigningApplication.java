package eu.assina.sa;

import eu.assina.sa.config.FileStorageConfig;
import eu.assina.sa.config.RSSPClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/** Main Spring Boot application class for Assina application */
@EnableConfigurationProperties({FileStorageConfig.class, RSSPClientConfig.class})
// disable security on the Signing App - all security is on the RSSP
@SpringBootApplication(scanBasePackages = "eu.assina.sa", exclude = SecurityAutoConfiguration.class)
public class AssinaSigningApplication {

	public static void main(String[] args) {
		//		https://stackoverflow.com/questions/26547532/how-to-shutdown-a-spring-boot-application-in-a-correct-way
		SpringApplication application = new SpringApplication(AssinaSigningApplication.class);
		// write a PID to allow for shutdown
		application.addListeners(new ApplicationPidFileWriter("./sa.pid"));
		application.run(args);
	}
}
