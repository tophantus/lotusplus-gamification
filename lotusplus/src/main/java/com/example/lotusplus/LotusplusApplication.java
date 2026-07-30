package com.example.lotusplus;

import com.example.lotusplus.checkin.config.CheckInProperties;
import com.example.lotusplus.common.config.RedissonProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableConfigurationProperties({CheckInProperties.class, RedissonProperties.class})
@EnableCaching
public class LotusplusApplication {

	public static void main(String[] args) {
		SpringApplication.run(LotusplusApplication.class, args);
	}

}
