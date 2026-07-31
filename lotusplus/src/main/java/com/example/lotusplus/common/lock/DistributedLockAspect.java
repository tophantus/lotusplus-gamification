package com.example.lotusplus.common.lock;

import com.example.lotusplus.common.exception.BusinessException;
import com.example.lotusplus.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
@Order(Ordered.LOWEST_PRECEDENCE)
public class DistributedLockAspect {

    private final RedissonClient redissonClient;
    private final ExpressionParser parser = new SpelExpressionParser();

    @Around("@annotation(lock)")
    public Object execute(ProceedingJoinPoint joinPoint, DistributedLock lock) throws Throwable {

        String key = buildLockKey(joinPoint, lock);
        RLock rLock = redissonClient.getLock(key);

        boolean acquired = false;
        boolean unlockRegistered = false;

        try {
            log.debug("[LOCK] Try acquire key={}", key);

            acquired = rLock.tryLock(lock.waitTime(), -1, lock.unit());

            if (!acquired) {
                throw new BusinessException(ErrorCode.REQUEST_IN_PROGRESS);
            }

            log.debug("[LOCK] Acquired key={}", key);

            registerUnlock(rLock, key);
            unlockRegistered = true;

            return joinPoint.proceed();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException(ErrorCode.INTERNAL_ERROR);
        } finally {
            if (acquired && !unlockRegistered) {
                unlock(rLock, key);
            }
        }
    }

    private void registerUnlock(RLock lock, String key) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            unlock(lock, key);
            return;
        }

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                unlock(lock, key);
            }

            @Override
            public void afterCompletion(int status) {
                if (status != STATUS_COMMITTED) {
                    unlock(lock, key);
                }
            }
        });
    }

    private void unlock(RLock lock, String key) {
        try {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.debug("[LOCK] Released key={}", key);
            }
        } catch (Exception e) {
            log.error("[LOCK] Unlock failed key={}", key, e);
        }
    }

    private String buildLockKey(ProceedingJoinPoint joinPoint, DistributedLock lock) {
        return lock.prefix() + parseKey(joinPoint, lock.key());
    }

    private String parseKey(ProceedingJoinPoint joinPoint, String expression) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        StandardEvaluationContext context = new StandardEvaluationContext();

        String[] names = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < names.length; i++) {
            context.setVariable(names[i], args[i]);
        }

        return parser.parseExpression(expression).getValue(context, String.class);
    }
}