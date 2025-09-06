package co.com.globalogic.usermanagement.domain.exception;

import lombok.Getter;

@Getter
public class ErrorException extends RuntimeException {

    private final ExceptionMessage exceptionMessage;

    public ErrorException(ExceptionMessage exceptionMessage) {
        super(exceptionMessage.getDetail());
        this.exceptionMessage = exceptionMessage;
    }

    public ErrorException(ExceptionMessage exceptionMessage,Throwable cause) {
        super(exceptionMessage.getDetail(), cause);
        this.exceptionMessage = exceptionMessage;
    }

}
