# Ollama Java Client Spring Boot Starter

This projects serves as the Spring Boot 3 starter project for the `ollama-java-client` library.

This starter simplifies the setup process, allowing the library to be imported and automatically configured within a
Spring Boot project.

## Steps:

1. Write an Auto Configuration class providing the required bean definitions

```java

@AutoConfiguration
public class OllamaJavaClientAutoconfiguration {

    @ConditionalOnMissingBean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @ConfigurationProperties(prefix = "ollama")
    @Bean
    public OllamaProperties ollamaProperties() {
        return new OllamaProperties();
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public OllamaService ollamaService(OllamaProperties ollamaProperties) {
        return OllamaServiceFactory.create(ollamaProperties);
    }
}
```

2. A file `resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` that instructs
   Spring Boot about the Configuration classes to scan.

```text[org.springframework.boot.autoconfigure.AutoConfiguration.imports]
es.omarall.ollama.OllamaJavaClientAutoconfiguration
```

## Usage

Check the [spring-boot-ollama-sample](../spring-boot-ollama-sample) project for a sample usage of the starter.