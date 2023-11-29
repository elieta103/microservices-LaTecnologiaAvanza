package com.elhg.calificacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class CalificacionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalificacionServiceApplication.class, args);
	}

}
