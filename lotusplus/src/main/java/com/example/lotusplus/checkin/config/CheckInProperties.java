package com.example.lotusplus.checkin.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.LocalTime;

@Getter
@Setter
@ConfigurationProperties(prefix = "checkin")
public class CheckInProperties {

    private TimeRange morning;

    private TimeRange evening;

    @Getter
    @Setter
    public static class TimeRange {

        private LocalTime start;

        private LocalTime end;

    }

}