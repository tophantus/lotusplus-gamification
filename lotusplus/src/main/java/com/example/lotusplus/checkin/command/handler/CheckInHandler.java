package com.example.lotusplus.checkin.command.handler;

import com.example.lotusplus.checkin.command.dto.CheckInResponse;
import com.example.lotusplus.common.lock.DistributedLockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CheckInHandler {

    private static final String CHECKIN_LOCK =
            "lock:checkin:user:";

    private final DistributedLockService lockService;
    private final CheckInCommandHandler commandService;


    public CheckInResponse handle(UUID userId){

        return lockService.execute(
                CHECKIN_LOCK + userId,
                () -> commandService.handle(userId)
        );
    }
}