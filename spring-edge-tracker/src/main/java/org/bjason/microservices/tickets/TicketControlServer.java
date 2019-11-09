package org.bjason.microservices.tickets;

import org.bjason.microservices.redis.WriteToMasterReadFromReplicaConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.util.logging.Logger;

@EnableAutoConfiguration
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
@EnableCaching
@Import(WriteToMasterReadFromReplicaConfiguration.class)
public class TicketControlServer {

	protected Logger logger = Logger.getLogger(TicketControlServer.class.getName());

	public static void main(String[] args) {
		System.setProperty("spring.config.name", "ticket-control-server");

		SpringApplication.run(TicketControlServer.class, args);
	}
}
