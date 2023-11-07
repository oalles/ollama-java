package es.omarall.ollama.exceptions;

public class OllamaException extends RuntimeException {
    public OllamaException(String message) {
        super(message);
    }

    public OllamaException(String message, Throwable cause) {
        super(message, cause);
    }

    public OllamaException(Exception e) {
        super(e);
    }
}
