package com.example.lotusplus.checkin.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckInMonthSnapshot {

    private long checkedCount;

    private List<CheckInDayStatusResponse> days;

}