package es.omarall.ollama.exceptions;

public class NotFoundException extends OllamaHttpException {
    public NotFoundException(String message) {
        super(404, message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(404, message, cause);
    }

    public NotFoundException(Exception e) {
        super(404, e);
    }
}
