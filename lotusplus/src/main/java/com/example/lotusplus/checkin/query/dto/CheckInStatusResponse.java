package com.example.lotusplus.checkin.query.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CheckInStatusResponse {

    /**
     * Hôm nay đã check-in chưa
     */
    private boolean checkedInToday;

    /**
     * Có thể check-in ngay lúc này không
     */
    private boolean canCheckIn;

    /**
     * Đã check-in bao nhiêu ngày trong tháng
     */
    private Integer currentDay;

    /**
     * Danh sách trạng thái 7 ngày
     */
    private List<CheckInDayStatusResponse> days;

}