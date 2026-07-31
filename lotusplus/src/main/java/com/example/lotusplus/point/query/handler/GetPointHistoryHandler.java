package com.example.lotusplus.point.query.handler;

import com.example.lotusplus.point.entity.PointHistory;
import com.example.lotusplus.point.mapper.PointHistoryMapper;
import com.example.lotusplus.point.query.dto.PointHistoryItemResponse;
import com.example.lotusplus.point.query.dto.PointHistoryResponse;
import com.example.lotusplus.point.query.repository.PointQueryRepository;
import com.example.lotusplus.user.query.handler.ValidateUserHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetPointHistoryHandler {

    private final ValidateUserHandler validateUserHandler;

    private final PointQueryRepository pointRepository;

    @Transactional(readOnly = true)
    public PointHistoryResponse handle(
            UUID userId,
            int page,
            int size
    ) {

        validateUserHandler.handle(userId);

        Pageable pageable = PageRequest.of(page, size);

        Page<PointHistory> historyPage =
                pointRepository.findByUserIdOrderByCreatedAtDesc(
                        userId,
                        pageable
                );

        List<PointHistoryItemResponse> items =
                historyPage.getContent()
                        .stream()
                        .map(PointHistoryMapper::toResponse)
                        .toList();

        return PointHistoryResponse.builder()
                .items(items)
                .page(historyPage.getNumber())
                .size(historyPage.getSize())
                .totalElements(historyPage.getTotalElements())
                .totalPages(historyPage.getTotalPages())
                .build();
    }

}