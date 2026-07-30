package com.example.lotusplus.common.lock;

import com.example.lotusplus.common.exception.BusinessException;
import com.example.lotusplus.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class DistributedLockService {

    private final RedissonClient redissonClient;


    public <T> T execute(
            String key,
            Supplier<T> action
    ){

        RLock lock =
                redissonClient.getLock(key);

        boolean locked = false;

        try {

            locked = lock.tryLock(
                    5,
                    30,
                    TimeUnit.SECONDS
            );

            if(!locked){
                throw new BusinessException(
                        ErrorCode.REQUEST_IN_PROGRESS
                );
            }

            return action.get();

        } catch (InterruptedException e){

            Thread.currentThread()
                    .interrupt();

            throw new BusinessException(
                    ErrorCode.INTERNAL_ERROR
            );

        } finally {

            if(locked && lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }
    }
}
