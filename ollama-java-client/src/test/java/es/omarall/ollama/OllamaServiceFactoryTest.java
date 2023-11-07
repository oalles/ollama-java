package es.omarall.ollama;

import com.google.gson.Gson;
import es.omarall.ollama.properties.OllamaProperties;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

public class OllamaServiceFactoryTest {
    @Mock
    private OllamaProperties properties;

    @Mock
    private Gson gson;


    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildWithNullProperties() {
        OllamaServiceFactory.create(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildWithNullBaseUrl() {
        when(properties.getBaseUrl()).thenReturn(null);
        OllamaServiceFactory.create(properties);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildWithBlankBaseUrl() {
        when(properties.getBaseUrl()).thenReturn(" ");
        OllamaServiceFactory.create(properties);
    }

    @Test
    public void testBuildWithValidProperties() {
        when(properties.getBaseUrl()).thenReturn("http://example.com");
        OllamaService ollamaService = OllamaServiceFactory.create(properties, gson);
    }

}
