package com.example.ZubrilkaEnglishServer.controllers.exeptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ExeptionsController {
    /**
     * отправит обычную ошибку
     */
    @ExceptionHandler
    private ResponseEntity<?>handleExeption(MyExeption e){
        System.out.println("handleExeption called");
//обязательно геттеры и сеттеры у передаваемого обьекта
    MyExeptionResponse response=new MyExeptionResponse(e.getMessage());

    return new ResponseEntity<>(response,e.getHttpStatus());
}

    /**
     * отправит сообщение с ошибкой
     * и массив сообщений ошибок
     */
    @ExceptionHandler
    private ResponseEntity<?>validationExeption(MyValidationExeption e){
    return new ResponseEntity<>(new MyValidationExeptionResponse(
            e.getMessage(),
            e.getValidationErrors()
    ),e.getHttpStatus());
}
}
