package com.example.zeapp.controllers.validation;

/**
 * ошибка выбрасываемая при ошибка в валидации входящих данных
 */
public class ValidationException extends Exception{
    public ValidationException(String message){
        super(message);
    }
}
