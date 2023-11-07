package es.omarall.ollama.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompletionResponse {
    private String model;
    private String createdAt;
    private String response;
    private Boolean done;
    private Long totalDuration;
    private Long loadDuration;
    private Integer sampleCount;
    private Long sampleDuration;
    private Integer promptEvalCount;
    private Long promptEvalDuration;
    private Integer evalCount;
    private Long evalDuration;
}