package com.example.ZubrilkaEnglishServer.controllers.exeptions;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class MyValidationExeption extends RuntimeException{
    private Map<String,String> validationErrors;
    private HttpStatus httpStatus;

    public MyValidationExeption(String message, Map validationErrors, HttpStatus httpStatus) {
        super(message);
        this.validationErrors = validationErrors;
        this.httpStatus = httpStatus;
    }

    public Map<String, String> getValidationErrors() {return validationErrors;}
    public void setValidationErrors(Map<String, String> validationErrors) {this.validationErrors = validationErrors;}
    public HttpStatus getHttpStatus() {return httpStatus;}
    public void setHttpStatus(HttpStatus httpStatus) {this.httpStatus = httpStatus;}
}
class MyValidationExeptionResponse{
    private String message;
    private Map<String,String> validationErrors;

    public MyValidationExeptionResponse(String message, Map<String, String> validationErrors) {
        this.message = message;
        this.validationErrors = validationErrors;
    }

    public String getMessage() {return message;}
    public void setMessage(String message) {this.message = message;}

    public Map<String, String> getValidationErrors() {return validationErrors;}
    public void setValidationErrors(Map<String, String> validationErrors) {this.validationErrors = validationErrors;}
}
