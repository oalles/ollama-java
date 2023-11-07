package es.omarall.ollama;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class SimpleStringStreamResponseProcessor implements StreamResponseProcessor<String> {
    private boolean done = false;

    @Override
    public void processStreamItem(String item) {
        log.info("Item: {}", item);
    }

    @Override
    public void processCompletion(String fullResponse) {
        log.info("Full Response: {}", fullResponse);
        done = true;
    }

    @Override
    public void processError(Throwable throwable) {
        log.error("", throwable);
    }
}
