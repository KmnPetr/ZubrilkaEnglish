package com.example.ZubrilkaEnglishServer.controllers.exeptions;

import org.springframework.http.HttpStatus;

public class MyExeption extends RuntimeException{
    private HttpStatus httpStatus;
    public MyExeption(String message,HttpStatus httpStatus) {
        super(message);
        this.httpStatus=httpStatus;
    }

    public HttpStatus getHttpStatus() {return httpStatus;}
    public void setHttpStatus(HttpStatus httpStatus) {this.httpStatus = httpStatus;}
}
/**
 * просто класс с ошибкой, чтобы не отправлять в сеть огромный перечень ошибок из класса RuntimeException
 */
class MyExeptionResponse{
    private String massage;

    public MyExeptionResponse(String massage) {
        this.massage = massage;
    }

    public String getMassage() {return massage;}
    public void setMassage(String massage) {this.massage = massage;}
}
