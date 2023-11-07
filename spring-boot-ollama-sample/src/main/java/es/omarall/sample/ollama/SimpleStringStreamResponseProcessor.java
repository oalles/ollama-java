package es.omarall.sample.ollama;

import es.omarall.ollama.StreamResponseProcessor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class SimpleStringStreamResponseProcessor implements StreamResponseProcessor<String> {

    @Override
    public void processStreamItem(String item) {
        log.info("Item: {}", item);
    }

    @Override
    public void processCompletion(String fullResponse) {
    }

    @Override
    public void processError(Throwable throwable) {
        log.error("", throwable);
    }
}
