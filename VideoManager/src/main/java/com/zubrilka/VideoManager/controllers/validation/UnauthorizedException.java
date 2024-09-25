package com.zubrilka.VideoManager.controllers.validation;

/**
 * при ошибке аутентификации
 */
public class UnauthorizedException extends Exception{
    public UnauthorizedException(String message){
        super(message);
    }
}
