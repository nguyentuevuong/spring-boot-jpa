package com.nittsu.kinjirou;

import java.io.IOException;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EntityScan(basePackageClasses = { UniversalApplication.class })
public class UniversalApplication implements ApplicationListener<ApplicationReadyEvent> {
	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	public static void main(final String[] args) {
		SpringApplication.run(UniversalApplication.class, args);
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		Runtime rt = Runtime.getRuntime();
		ConfigurableApplicationContext context = event.getApplicationContext();
		Environment environment = context.getBean(Environment.class);
		Integer port = environment.getProperty("server.port", Integer.class, 8080);

		try {
			rt.exec("rundll32 url.dll,FileProtocolHandler " + "http://localhost:" + port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
