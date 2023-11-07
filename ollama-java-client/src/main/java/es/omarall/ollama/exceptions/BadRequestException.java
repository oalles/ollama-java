package es.omarall.ollama.exceptions;

public class BadRequestException extends OllamaHttpException {
    public BadRequestException(String message) {
        super(400, message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(400, message, cause);
    }

    public BadRequestException(Exception e) {
        super(400, e);
    }
}
