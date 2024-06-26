package com.eidtrust.signer.rssp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.eidtrust.signer.rssp.common.config.AppProperties;
import com.eidtrust.signer.rssp.common.config.CSCProperties;
import com.eidtrust.signer.rssp.common.config.DemoProperties;

/** Main Spring Boot application class for Assina application */
@SpringBootApplication(scanBasePackages = "com.eidtrust.signer.rssp")
@EnableConfigurationProperties({AppProperties.class, CSCProperties.class, DemoProperties.class})
public class AssinaRSSPApplication {

	public static void main(String[] args) {
//		https://stackoverflow.com/questions/26547532/how-to-shutdown-a-spring-boot-application-in-a-correct-way
		SpringApplication application = new SpringApplication(AssinaRSSPApplication.class);
		// write a PID to allow for shutdown
		application.addListeners(new ApplicationPidFileWriter("./rssp.pid"));
		application.run(args);
	}

}
