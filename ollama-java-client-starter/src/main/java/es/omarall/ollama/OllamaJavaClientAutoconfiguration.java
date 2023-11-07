package es.omarall.ollama;

import es.omarall.ollama.properties.OllamaProperties;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;

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
