package es.omarall.ollama.exceptions;

import lombok.Getter;

@Getter
public class OllamaHttpException extends OllamaException {

    private int statusCode;

    public OllamaHttpException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public OllamaHttpException(int statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public OllamaHttpException(int statusCode, Exception e) {
        super(e);
        this.statusCode = statusCode;
    }
}
