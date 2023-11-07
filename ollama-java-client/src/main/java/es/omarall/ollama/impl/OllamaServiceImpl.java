package es.omarall.ollama.impl;

import com.google.gson.Gson;
import es.omarall.ollama.OllamaService;
import es.omarall.ollama.StreamResponseProcessor;
import es.omarall.ollama.exceptions.OllamaException;
import es.omarall.ollama.model.CompletionRequest;
import es.omarall.ollama.model.CompletionResponse;
import es.omarall.ollama.model.CopyRequest;
import es.omarall.ollama.model.DeleteRequest;
import es.omarall.ollama.model.EmbeddingRequest;
import es.omarall.ollama.model.EmbeddingResponse;
import es.omarall.ollama.model.ShowRequest;
import es.omarall.ollama.model.ShowResponse;
import es.omarall.ollama.model.TagsResponse;
import es.omarall.ollama.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

@Slf4j
public class OllamaServiceImpl implements OllamaService {

    private final OllamaApi ollamaApi;
    private final Gson gson;

    private final Utils utils = new Utils();

    public OllamaServiceImpl(final OllamaApi ollamaApi, Gson gson) {
        this.ollamaApi = ollamaApi;
        this.gson = gson;
    }

    @Override
    public CompletionResponse completion(CompletionRequest request) {
        try {
            request.setStream(false);
            Response<CompletionResponse> retrofitResponse
                    = ollamaApi.completion(request).execute();

            if (retrofitResponse.isSuccessful()) {
                return retrofitResponse.body();
            }
            throw utils.processErrorResponse(retrofitResponse);
        } catch (IOException e) {
            throw new OllamaException("IO error", e);
        }
    }

    @Override
    public TagsResponse getTags() {
        try {
            retrofit2.Response<TagsResponse> retrofitResponse = ollamaApi.getTags().execute();
            if (retrofitResponse.isSuccessful()) {
                return retrofitResponse.body();
            }
            throw utils.processErrorResponse(retrofitResponse);
        } catch (IOException e) {
            throw new OllamaException("IO error", e);
        }
    }

    @Override
    public ShowResponse show(ShowRequest showRequest) {
        try {
            retrofit2.Response<ShowResponse> retrofitResponse = ollamaApi.show(showRequest).execute();
            if (retrofitResponse.isSuccessful()) {
                return retrofitResponse.body();
            }
            throw utils.processErrorResponse(retrofitResponse);
        } catch (IOException e) {
            throw new OllamaException("IO error", e);
        }
    }

    @Override
    public void copy(CopyRequest copyRequest) {
        try {
            retrofit2.Response<Void> retrofitResponse = ollamaApi.copy(copyRequest).execute();
            if (!retrofitResponse.isSuccessful()) {
                throw utils.processErrorResponse(retrofitResponse);
            }
        } catch (IOException e) {
            throw new OllamaException("IO error", e);
        }
    }

    @Override
    public void delete(String modelName) {
        try {
            retrofit2.Response<Void> retrofitResponse = ollamaApi.delete(DeleteRequest.builder().name(modelName).build()).execute();
            if (!retrofitResponse.isSuccessful()) {
                throw utils.processErrorResponse(retrofitResponse);
            }
        } catch (IOException e) {
            throw new OllamaException("IO error", e);
        }
    }

    @Override
    public void streamingCompletion(CompletionRequest request, StreamResponseProcessor<String> responseProcessor) {
        request.setStream(true);
        ollamaApi.streamingCompletion(request)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        try (ResponseBody body = response.body();
                             InputStream inputStream = Objects.requireNonNull(body).byteStream();
                             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                            StringBuilder fullResponse = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                CompletionResponse completionResponse = gson.fromJson(line, CompletionResponse.class);
                                log.debug("Completion response item: {}", completionResponse);
                                if (completionResponse.getDone()) {
                                    String responseContent = fullResponse.toString();
                                    log.debug("Full response: {}", responseContent);
                                    responseProcessor.processCompletion(responseContent);
                                    break;
                                }
                                fullResponse.append(completionResponse.getResponse());
                                responseProcessor.processStreamItem(completionResponse.getResponse());
                            }
                        } catch (IOException e) {
                            throw new OllamaException("IO error", e);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable throwable) {
                        log.debug("Streaming completion error", throwable);
                        responseProcessor.processError(throwable);
                    }
                });
    }

    public EmbeddingResponse embed(EmbeddingRequest request) {
        try {
            retrofit2.Response<EmbeddingResponse> retrofitResponse = ollamaApi.embed(request).execute();
            if (retrofitResponse.isSuccessful()) {
                return retrofitResponse.body();
            }
            throw utils.processErrorResponse(retrofitResponse);
        } catch (IOException e) {
            throw new OllamaException("IO error", e);
        }
    }
}
