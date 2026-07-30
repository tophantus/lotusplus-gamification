package com.example.lotusplus.point.query.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PointHistoryResponse {

    private List<PointHistoryItemResponse> items;

    private int page;

    private int size;

    private long totalElements;

    private int totalPages;

}
