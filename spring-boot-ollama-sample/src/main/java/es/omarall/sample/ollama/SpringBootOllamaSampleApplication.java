package es.omarall.sample.ollama;

import es.omarall.ollama.OllamaService;
import es.omarall.ollama.model.CompletionRequest;
import es.omarall.ollama.model.CompletionResponse;
import es.omarall.ollama.model.EmbeddingRequest;
import es.omarall.ollama.model.EmbeddingResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
@Slf4j
public class SpringBootOllamaSampleApplication {

    @Value("${application.model-name}")
    private String MODEL_NAME;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootOllamaSampleApplication.class, args);
    }


    @Bean
    SimpleStringStreamResponseProcessor streamResponseProcessor() {
        return new SimpleStringStreamResponseProcessor();
    }

    @Bean
    ApplicationRunner runner(OllamaService ollamaService, SimpleStringStreamResponseProcessor streamResponseProcessor) {
        return args -> {

            // Embedding request
            EmbeddingResponse embeddingResponse = ollamaService.embed(EmbeddingRequest.builder()
                    .model(MODEL_NAME)
                    .prompt("Dare to embed this text?")
                    .build());
            log.info("******* Ollama Embedding response: {}", embeddingResponse.getEmbedding());

            log.info("******* (wait for it)");

            // Completion request
            Arrays.asList("What is the capital city of Spain?",
                            "Translate this text to Spanish: 'I love cookies!'")
                    .forEach(prompt -> {
                        CompletionResponse response = ollamaService.completion(CompletionRequest.builder()
                                .model(MODEL_NAME).prompt(prompt).build());
                        log.info("******* Ollama Completion response: {}", response.getResponse());
                    });

            // Streaming completion
            ollamaService.streamingCompletion(CompletionRequest.builder()
                    .model(MODEL_NAME)
                    .prompt("What is the meaning of life?")
                    .build(), streamResponseProcessor);

        };
    }

}
