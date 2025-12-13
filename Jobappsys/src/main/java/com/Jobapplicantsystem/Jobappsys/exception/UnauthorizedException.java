package com.Jobapplicantsystem.Jobappsys.exception;
//Sends 403 forbideen error to GlobalExceptionHandler
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
