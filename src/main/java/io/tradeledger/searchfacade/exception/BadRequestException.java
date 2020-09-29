package io.tradeledger.searchfacade.exception;

public class BadRequestException extends RuntimeException {
    private String errorMessage;
    private Throwable cause;

    public BadRequestException( String errorMessage){

        super(errorMessage);
        this.errorMessage = errorMessage;

    }

    public BadRequestException( Throwable cause){

        super(cause);
        this.cause = cause;

    }

    public BadRequestException(String errorMessage, Throwable cause){

        super(errorMessage, cause);
        this.errorMessage = errorMessage;
        this.cause = cause;

    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }
}
