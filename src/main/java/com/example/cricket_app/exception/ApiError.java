package com.example.cricket_app.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ApiError {
   private Integer status;
   private String message;
   private LocalDateTime time;
   private Map<String,String> errors;

    public ApiError(Integer status, String message, Map<String, String> errors) {
        this.status = status;
        this.message = message;
        this.time = LocalDateTime.now();
        this.errors = errors;
    }
}
