# OLLAMA Java Client

## Modules

* [ollama-java-client](ollama-java-client) - The Ollama Java Client library
* [ollama-java-client-starter](ollama-java-client-starter) - The Ollama Java Client Spring Boot 3 Starter
* [spring-boot-ollama-sample](spring-boot-ollama-sample) - A sample Spring Boot 3 application using the Ollama Java
  Client Spring Boot 3 Starter

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

### How to use

Just create an instance of the `OllamaService` with the factory and use it.

Have a look at [here](./ollama-java-client/src/test/java/es/omarall/ollama/HowToUse.java)

or have a look at the [spring-boot-ollama-sample](spring-boot-ollama-sample) project.

## Useful Resources

### API DOC

https://github.com/jmorganca/ollama/blob/main/docs/api.md

### Linux install

https://github.com/jmorganca/ollama/blob/main/docs/linux.md

```bash
$ curl https://ollama.ai/install.sh | sh
>>> Installing ollama to /usr/local/bin...
>>> Creating ollama user...
>>> Adding current user to ollama group...
>>> Creating ollama systemd service...
>>> Enabling and starting ollama service...
Created symlink /etc/systemd/system/default.target.wants/ollama.service → /etc/systemd/system/ollama.service.
>>> NVIDIA GPU installed.
```

#### Test URL

```text
# open http://localhost:11434/ 
# or via curl    
$ curl http://localhost:11434/api/tags
```

#### Instal Mistral 7B model

```bash
$ ollama run mistral
```

#### Viewing logs

To view logs of Ollama running as a startup service, run:

``` bash
$ journalctl -u ollama
```

### Uninstall

Remove the ollama service:

```bash
sudo systemctl stop ollama
sudo systemctl disable ollama
sudo rm /etc/systemd/system/ollama.service
```

Remove the ollama binary from your bin directory (either /usr/local/bin, /usr/bin, or /bin):

```bash
sudo rm $(which ollama)
```

Remove the downloaded models and Ollama service user:

```bash
sudo rm -r /usr/share/ollama
sudo userdel ollama
```
