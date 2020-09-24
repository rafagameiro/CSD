package app.exceptionhandlers;

import app.exceptions.InsufficientBalanceException;
import app.exceptions.ServerErrorException;
import app.exceptions.UserAlreadyExistsException;
import app.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class WalletExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity handleUserException() {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity handleUserNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity handleInsufficientBalanceException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(ServerErrorException.class)
    public ResponseEntity handleServerErrorException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
