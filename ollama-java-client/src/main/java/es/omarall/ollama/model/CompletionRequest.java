package es.omarall.ollama.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompletionRequest {
    private String model;
    private String prompt;
    private Options options;
    private Boolean stream;
}