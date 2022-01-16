package com.livi.separation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import net.unicon.cas.client.configuration.EnableCasClient;

@EnableCasClient
@SpringBootApplication
public class CasSeparationApplication {

	public static void main(String[] args) {
		SpringApplication.run(CasSeparationApplication.class, args);
	}

}
