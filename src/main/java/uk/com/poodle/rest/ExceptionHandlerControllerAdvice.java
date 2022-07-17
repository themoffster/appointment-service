package uk.com.poodle.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@ControllerAdvice
class ExceptionHandlerControllerAdvice {

    @ResponseBody
    @ExceptionHandler
    ResponseEntity<Map<String, Object>> handle(NoSuchElementException e) {
        log.error("Handling NoSuchElementException.", e);
        return buildResponse(BAD_REQUEST, e);
    }

    @ResponseBody
    @ExceptionHandler
    ResponseEntity<Map<String, Object>> handle(IllegalArgumentException e) {
        log.error("Handling IllegalArgumentException.", e);
        return buildResponse(BAD_REQUEST, e);
    }

    @SuppressWarnings("SameParameterValue")
    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, Exception exception) {
        Map<String, Object> body = new HashMap<>();
        body.put("code", status.value());
        body.put("message", exception.getMessage());
        return ResponseEntity.status(status).body(body);
    }
}
