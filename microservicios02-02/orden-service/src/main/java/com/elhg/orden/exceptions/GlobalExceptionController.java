package com.elhg.orden.exceptions;


import com.elhg.orden.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(ItemInventoryNotFoundException.class)
    public ResponseEntity<ApiResponse> handlerResourceNotFoundException(ItemInventoryNotFoundException itemInventoryNotFoundException){
        String mensaje = itemInventoryNotFoundException.getMessage();

        ApiResponse response = new ApiResponse().builder()
                .message(mensaje)
                .success(true)
                .status(HttpStatus.NOT_FOUND)
                .build();
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

}
