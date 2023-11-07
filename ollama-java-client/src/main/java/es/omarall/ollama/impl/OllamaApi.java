package es.omarall.ollama.impl;

import es.omarall.ollama.model.CompletionRequest;
import es.omarall.ollama.model.CompletionResponse;
import es.omarall.ollama.model.CopyRequest;
import es.omarall.ollama.model.DeleteRequest;
import es.omarall.ollama.model.EmbeddingRequest;
import es.omarall.ollama.model.EmbeddingResponse;
import es.omarall.ollama.model.ShowRequest;
import es.omarall.ollama.model.ShowResponse;
import es.omarall.ollama.model.TagsResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Streaming;

/**
 * {@code @See} <a href="https://github.com/jmorganca/ollama/blob/main/docs/api.md">Ollama API docs</a>
 */
public interface OllamaApi {

    @POST("/api/generate")
    @Headers({"Content-Type: application/json"})
    Call<CompletionResponse> completion(@Body CompletionRequest completionRequest);

    @GET("/api/tags")
    @Headers({"Content-Type: application/json"})
    Call<TagsResponse> getTags();

    @POST("/api/show")
    @Headers({"Content-Type: application/json"})
    Call<ShowResponse> show(@Body ShowRequest showRequest);

    @POST("/api/copy")
    @Headers({"Content-Type: application/json"})
    Call<Void> copy(@Body CopyRequest copyRequest);

    @POST("/api/delete")
    @Headers({"Content-Type: application/json"})
    Call<Void> delete(@Body DeleteRequest deleteRequest);

    @POST("/api/generate")
    @Headers({"Content-Type: application/json"})
    @Streaming
    Call<ResponseBody> streamingCompletion(@Body CompletionRequest completionRequest);

    @POST("/api/embeddings")
    @Headers({"Content-Type: application/json"})
    Call<EmbeddingResponse> embed(@Body EmbeddingRequest embeddingRequest);
}