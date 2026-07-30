package com.example.lotusplus;

import com.example.lotusplus.checkin.config.CheckInProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(CheckInProperties.class)
public class LotusplusApplication {

	public static void main(String[] args) {
		SpringApplication.run(LotusplusApplication.class, args);
	}

}
