package es.omarall.ollama;


import es.omarall.ollama.model.CompletionRequest;
import es.omarall.ollama.model.CompletionResponse;
import es.omarall.ollama.model.CopyRequest;
import es.omarall.ollama.model.EmbeddingRequest;
import es.omarall.ollama.model.EmbeddingResponse;
import es.omarall.ollama.model.ShowRequest;
import es.omarall.ollama.model.ShowResponse;
import es.omarall.ollama.model.TagsResponse;
import es.omarall.ollama.properties.OllamaProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
@Slf4j
public class HowToUse {

    /*
     Follow installation instructions, install mistral:latest and play ...
     */

    private static final String LOCAL_URL = "http://localhost:11434";
    private static final String MODEL_NAME = "mistral:latest";
    private static final int DEFAULT_TIMEOUT = 60000;

    private OllamaService ollamaService;

    @Before
    public void setup() {
        // Instantiate the ollama service from properties
        ollamaService = OllamaServiceFactory.create(OllamaProperties.builder().baseUrl(LOCAL_URL)
                .callTimeout(DEFAULT_TIMEOUT).connectTimeout(DEFAULT_TIMEOUT).readTimeout(DEFAULT_TIMEOUT)
                .writeTimeout(DEFAULT_TIMEOUT).build());
    }

    //    @Test
    public void tryCopyAndGetAndDelete() {
        String newModelName = "my-mistral:latest";
        ollamaService.copy(CopyRequest.builder().source(MODEL_NAME).destination(newModelName).build());
        TagsResponse response = ollamaService.getTags();
        Assert.assertNotNull(response);
        response.getModels().stream().filter(model -> model.getName().equals(newModelName)).findFirst().orElseThrow();

        ShowResponse showResponse = ollamaService.show(ShowRequest.builder().name(newModelName).build());
        ollamaService.delete(newModelName);
    }

    //    @Test
    public void tryCompletion() {
        CompletionResponse response = ollamaService.completion(CompletionRequest.builder().model(MODEL_NAME).prompt("What is the capital city of Spain?").build());
        assertTrue(response.getDone());
    }

    //    @Test
    public void tryEmbed() {
        EmbeddingResponse response = ollamaService.embed(EmbeddingRequest.builder()
                .model(MODEL_NAME)
                .prompt("Dare to embed this text?")
                .build());
        assertNotNull(response);
        assertNotNull(response.getEmbedding());
        assertTrue(response.getEmbedding().length > 0);
    }

    //    @Test
    public void tryStreamingCompletion() throws InterruptedException {

        CountDownLatch completionLatch = new CountDownLatch(1);
        long timeoutInSeconds = 30;
        long startTime = System.currentTimeMillis();

        SimpleStringStreamResponseProcessor responseProcessor = new SimpleStringStreamResponseProcessor();

        ollamaService.streamingCompletion(CompletionRequest.builder().model(MODEL_NAME).prompt("What is the capital city of Spain?").build(), responseProcessor);

        while (true) {
            if (responseProcessor.isDone()) {
                completionLatch.countDown();
                break;
            }

            long currentTime = System.currentTimeMillis();
            long elapsedTime = TimeUnit.MILLISECONDS.toSeconds(currentTime - startTime);
            if (elapsedTime >= timeoutInSeconds) {
                Assert.fail("Timeout exceeded");
            }

            TimeUnit.SECONDS.sleep(1);
        }
    }

}
