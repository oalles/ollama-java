package es.omarall.ollama;

public interface StreamResponseProcessor<T> {
    void processStreamItem(T item);

    void processCompletion(T fullResponse);

    void processError(Throwable throwable);
}
