# OLLAMA Java Client

## Model Description

### OllamaService

The [OllamaService](src/main/java/es/omarall/ollama/OllamaService.java) interface provide the interaction with the
ollama web service.

```java
public interface OllamaService {
    CompletionResponse completion(CompletionRequest completionRequest);

    TagsResponse getTags();

    ShowResponse show(ShowRequest showRequest);

    void copy(CopyRequest copyRequest);

    void delete(String modelName);

    void streamingCompletion(CompletionRequest completionRequest, StreamResponseProcessor<String> handler);

    EmbeddingResponse embed(EmbeddingRequest embeddingRequest);
}
```

### OllamaServiceFactory

The [OllamaServiceFactory](src/main/java/es/omarall/ollama/OllamaServiceFactory.java) class is responsible for creating
instances of the `OllamaService`. It provides builder methods
to create an instance of the service with the specified configuration.

```java
public class OllamaServiceFactory {
    public static OllamaService create(OllamaProperties properties) { // ...
    }

    public static OllamaService create(OllamaProperties properties, Gson gson) { // ...
    }
}
```

### StreamResponseProcessor

The [StreamResponseProcessor](src/main/java/es/omarall/ollama/StreamResponseProcessor.java) interface provides methods
to process streaming completion responses.

```java
public interface StreamResponseProcessor<T> {
    void processStreamItem(T item);

    void processCompletion(T fullResponse);

    void processError(Throwable throwable);
}
```