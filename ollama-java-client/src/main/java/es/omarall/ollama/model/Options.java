package es.omarall.ollama.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class Options {
    /**
     * The temperature of the model. Increasing the temperature will make the model answer more creatively. (Default: 0.8)
     */
    private Double temperature;

    /**
     * Enable Mirostat sampling for controlling perplexity. (default: 0, 0 = disabled, 1 = Mirostat, 2 = Mirostat 2.0)
     */
    private Integer mirostat;

    /**
     * Sets the size of the context window used to generate the next token. (Default: 2048)
     */
    private Integer numCtx;

    /**
     * The number of layers to send to the GPU(s). On macOS it defaults to 1 to enable metal support, 0 to disable.
     */
    private Integer numGpu;

    /**
     * Sets the number of threads to use during computation. By default, Ollama will detect this for optimal performance. It is recommended to set this value to the number of physical CPU cores your system has (as opposed to the logical number of cores).
     */
    private Integer numThread;

    /**
     * Sets the random number seed to use for generation. Setting this to a specific number will make the model generate the same text for the same prompt. (Default: 0)
     */
    private Integer seed;

    /**
     * Sets the stop sequences to use.
     */
    private String stop;

    /**
     * Maximum number of tokens to predict when generating text. (Default: 128, -1 = infinite generation, -2 = fill context)
     */
    private Integer numPredict;
}