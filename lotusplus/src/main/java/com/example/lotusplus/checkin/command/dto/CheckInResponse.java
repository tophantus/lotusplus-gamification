package com.example.lotusplus.checkin.command.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CheckInResponse {

    /**
     * Ngày check-in thứ mấy trong tháng (1 -> 7)
     */
    private Integer day;

    /**
     * Điểm vừa nhận
     */
    private Integer reward;

    /**
     * Tổng điểm Lotus+ sau khi check-in
     */
    private Long totalPoint;

}