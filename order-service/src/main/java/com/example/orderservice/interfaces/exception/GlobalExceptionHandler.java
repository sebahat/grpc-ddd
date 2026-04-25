package com.example.orderservice.interfaces.exception;

import com.example.orderservice.domain.exception.OrderNotFoundException;
import com.example.orderservice.domain.exception.ProductNotFoundException;
import com.example.orderservice.interfaces.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrderNotFound(OrderNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage(), 404));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleValidation(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getMessage(), 400));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Internal server error", 500));
    }

    @ExceptionHandler(ProductNotFoundException.class)

    public ResponseEntity<ErrorResponse> handleProductNotFound(ProductNotFoundException ex) {

        return ResponseEntity

                .status(HttpStatus.NOT_FOUND)

                .body(new ErrorResponse(ex.getMessage(), 404));

    }
}