package es.omarall.ollama.exceptions;

public class InternalServerException extends OllamaHttpException {
    public InternalServerException(String message) {
        super(500, message);
    }

    public InternalServerException(String message, Throwable cause) {
        super(500, message, cause);
    }

    public InternalServerException(Exception e) {
        super(500, e);
    }
}
