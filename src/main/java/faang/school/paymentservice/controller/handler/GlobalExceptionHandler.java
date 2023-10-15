package faang.school.paymentservice.controller.handler;

import java.sql.SQLException;
import java.time.LocalDateTime;

import faang.school.paymentservice.exception.NotEnoughMoneyOnBalanceException;
import faang.school.paymentservice.exception.IncorrectCurrencyException;
import faang.school.paymentservice.exception.IdempotencyException;
import faang.school.paymentservice.exception.PaymentException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NotEnoughMoneyOnBalanceException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleBalanceException(NotEnoughMoneyOnBalanceException e) {
        log.error("BalanceException ", e);
        return ErrorResponse.builder()
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(IncorrectCurrencyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleCurrencyException(IncorrectCurrencyException e) {
        log.error("CurrencyException ", e);
        return ErrorResponse.builder()
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(IdempotencyException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleIdempotencyException(IdempotencyException e) {
        log.error("IdempotencyException ", e);
        return ErrorResponse.builder()
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.CONFLICT)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(PaymentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlePaymentException(PaymentException e) {
        log.error("PaymentException ", e);
        return ErrorResponse.builder()
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(FeignException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleFeignException(FeignException e) {
        log.error("FeignException ", e);
        return ErrorResponse.builder()
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleSQLException(SQLException e) {
        log.error("SQLException ", e);
        return ErrorResponse.builder()
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException ", e);
        return ErrorResponse.builder()
                .dateTime(LocalDateTime.now())
                .message(e.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("HttpMessageNotReadableException ", e);
        return ErrorResponse.builder()
                .dateTime(LocalDateTime.now())
                .message(e.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .build();
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleRuntimeException(RuntimeException e) {
        log.error("RuntimeException ", e);
        return ErrorResponse.builder()
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(e.getMessage())
                .build();
    }
}
