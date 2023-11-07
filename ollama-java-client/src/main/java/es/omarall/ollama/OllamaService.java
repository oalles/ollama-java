package es.omarall.ollama;

import es.omarall.ollama.model.CompletionRequest;
import es.omarall.ollama.model.CompletionResponse;
import es.omarall.ollama.model.CopyRequest;
import es.omarall.ollama.model.EmbeddingRequest;
import es.omarall.ollama.model.EmbeddingResponse;
import es.omarall.ollama.model.ShowRequest;
import es.omarall.ollama.model.ShowResponse;
import es.omarall.ollama.model.TagsResponse;

public interface OllamaService {

    CompletionResponse completion(CompletionRequest completionRequest);

    TagsResponse getTags();

    ShowResponse show(ShowRequest showRequest);

    void copy(CopyRequest copyRequest);

    void delete(String modelName);

    void streamingCompletion(CompletionRequest completionRequest, StreamResponseProcessor<String> handler);

    EmbeddingResponse embed(EmbeddingRequest embeddingRequest);
}