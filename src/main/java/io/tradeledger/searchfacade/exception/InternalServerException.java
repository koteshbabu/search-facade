package io.tradeledger.searchfacade.exception;

public class InternalServerException extends RuntimeException {

    private String errorMessage;
    private Throwable cause;

    public InternalServerException( Throwable cause){

        super(cause);
        this.cause = cause;

    }

    public InternalServerException(String errorMessage, Throwable cause){

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
