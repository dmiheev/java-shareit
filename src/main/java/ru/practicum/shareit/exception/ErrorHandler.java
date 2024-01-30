package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice()
public class ErrorHandler {

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleUserNotFoundException(EntityNotFoundException e) {
        log.info("NOT_FOUND {}", e.getMessage());
        return String.format("Entity not found error: %s", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleEmailAlreadyExistsException(EmailIsAlreadyRegisteredException e) {
        log.info("CONFLICT {}", e.getMessage());
        return String.format("Email is already registered %s", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleEmptyFieldException(EmptyFieldException e) {
        log.info("BAD_REQUEST {}", e.getMessage());
        return String.format("Empty field exception: %s", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleGatewayHeaderException(IncorrectDataException e) {
        log.info("BAD_REQUEST {}", e.getMessage());
        return String.format("Gateway exception %s", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleUnsupportedStateException(UnsupportedStatusException e) {
        log.info("BAD_REQUEST {}", e.getMessage());
        return Map.of("error", "Unknown state: " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.info("BAD_REQUEST {}", e.getMessage());
        return String.format("Method Argument Not Valid Exception: %s", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleRuntimeException(Throwable e) {
        log.info("INTERNAL_SERVER_ERROR {}", e.getMessage());
        return String.format(e.getMessage());
    }

}