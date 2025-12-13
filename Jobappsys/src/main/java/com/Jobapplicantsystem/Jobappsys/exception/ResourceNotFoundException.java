package com.Jobapplicantsystem.Jobappsys.exception;
// Send 404 error to GlobalExceptionHandler
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
