package es.omarall.ollama.utils;

import es.omarall.ollama.exceptions.BadRequestException;
import es.omarall.ollama.exceptions.InternalServerException;
import es.omarall.ollama.exceptions.NotFoundException;
import es.omarall.ollama.exceptions.OllamaException;
import es.omarall.ollama.exceptions.OllamaHttpException;
import okhttp3.ResponseBody;
import retrofit2.Response;

import java.io.IOException;

public class Utils {

    public OllamaException processErrorResponse(Response<?> response) {
        int code = response.code();
        try (ResponseBody errorBody = response.errorBody()) {
            if (errorBody == null) {
                throw new OllamaException("error body should not be null");
            }
            String body = errorBody.string();
            String errorMessage = "Response Body: " + body;
            switch (code) {
                case 400:
                    throw new BadRequestException(errorMessage);
                case 404:
                    throw new NotFoundException(errorMessage);
                case 500:
                    throw new InternalServerException(errorMessage);
                default:
                    throw new OllamaHttpException(code, errorMessage);
            }
        } catch (IOException e) {
            throw new OllamaException("Error in the API HTTP response", e);
        }
    }
}
