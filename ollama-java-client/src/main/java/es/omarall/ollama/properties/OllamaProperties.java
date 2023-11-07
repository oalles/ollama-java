package es.omarall.ollama.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OllamaProperties {
    private String baseUrl;
    private long callTimeout = 30000;
    private long connectTimeout = 30000;
    private long readTimeout = 30000;
    private long writeTimeout = 30000;
}
