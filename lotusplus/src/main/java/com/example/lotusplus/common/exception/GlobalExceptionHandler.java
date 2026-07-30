package com.example.lotusplus.common.exception;

import com.example.lotusplus.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(
            BusinessException exception
    ){

        ErrorCode errorCode = exception.getErrorCode();

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(
                        ApiResponse.error(
                                errorCode.getCode(),
                                errorCode.getMessage()
                        )
                );
    }


    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ApiResponse<Void>> handleOptimisticLockException(
            ObjectOptimisticLockingFailureException exception
    ) {

        log.warn("Concurrent modification detected", exception);

        ErrorCode errorCode = ErrorCode.CONCURRENT_POINT_UPDATE;

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(
                        ApiResponse.error(
                                errorCode.getCode(),
                                errorCode.getMessage()
                        )
                );
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(
            Exception exception
    ){

        log.error("Unexpected error", exception);


        return ResponseEntity
                .internalServerError()
                .body(
                        ApiResponse.error(
                                ErrorCode.INTERNAL_ERROR.getCode(),
                                ErrorCode.INTERNAL_ERROR.getMessage()
                        )
                );
    }
}
