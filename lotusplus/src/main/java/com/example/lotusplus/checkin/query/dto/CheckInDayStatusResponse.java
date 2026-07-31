package com.example.lotusplus.checkin.query.dto;

import com.example.lotusplus.checkin.enums.CheckInStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CheckInDayStatusResponse {

    /**
     * Ngày thứ mấy trong tháng (1 -> 7)
     */
    private Integer day;

    /**
     * Điểm thưởng
     */
    private Integer reward;

    /**
     * Trạng thái hiển thị
     */
    private CheckInStatus status;

}