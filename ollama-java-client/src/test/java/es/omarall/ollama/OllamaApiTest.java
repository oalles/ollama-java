package es.omarall.ollama;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.omarall.ollama.impl.OllamaApi;
import es.omarall.ollama.model.CompletionRequest;
import es.omarall.ollama.model.CompletionResponse;
import es.omarall.ollama.model.EmbeddingRequest;
import es.omarall.ollama.model.EmbeddingResponse;
import es.omarall.ollama.model.TagsResponse;
import okhttp3.ResponseBody;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okio.BufferedSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class OllamaApiTest {
    private final Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    private OllamaApi ollamaApi;
    private MockWebServer mockWebServer;

    @Before
    public void setup() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        ollamaApi = retrofit.create(OllamaApi.class);
    }

    @After
    public void teardown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void testGetTags() throws IOException {
        // Arrange
        String jsonResponse = "{\"models\": [{\"name\": \"llama2:7b\", \"modified_at\": \"2023-08-02T17:02:23.713454393-07:00\", \"size\": 3791730596}, {\"name\": \"llama2:13b\", \"modified_at\": \"2023-08-08T12:08:38.093596297-07:00\", \"size\": 7323310500}]}";
        mockWebServer.enqueue(new MockResponse().setBody(jsonResponse));

        // Act
        Call<TagsResponse> call = ollamaApi.getTags();
        Response<TagsResponse> response = call.execute();

        // Assert
        assertTrue(response.isSuccessful());
        assertNotNull(response.body());
        assertEquals(2, response.body().getModels().size());
        assertEquals("llama2:7b", response.body().getModels().get(0).getName());
    }

    @Test
    public void testCompletion() throws IOException {
        // Arrange
        String jsonResponse = "{\"model\":\"llama2:7b\",\"created_at\":\"2023-08-04T19:22:45.499127Z\",\"response\":\"The sky is blue because it is the color of the sky.\",\"context\":[1,2,3],\"done\":true,\"total_duration\":5589157167,\"load_duration\":3013701500,\"sample_count\":114,\"sample_duration\":81442000,\"prompt_eval_count\":46,\"prompt_eval_duration\":1160282000,\"eval_count\":13,\"eval_duration\":1325948000}";
        mockWebServer.enqueue(new MockResponse().setBody(jsonResponse));

        CompletionRequest completionRequest = new CompletionRequest("llama2:7b", "Why is the sky blue?", null, false);

        // Act
        Call<CompletionResponse> call = ollamaApi.completion(completionRequest);
        Response<CompletionResponse> response = call.execute();

        // Assert
        assertTrue(response.isSuccessful());
        assertNotNull(response.body());
        assertEquals("llama2:7b", response.body().getModel());
        assertEquals("The sky is blue because it is the color of the sky.", response.body().getResponse());
        assertTrue(response.body().getDone());
        assertEquals(5589157167L, response.body().getTotalDuration().longValue());
        assertEquals(3013701500L, response.body().getLoadDuration().longValue());
        assertEquals(114, response.body().getSampleCount().intValue());
        assertEquals(81442000L, response.body().getSampleDuration().longValue());
        assertEquals(46, response.body().getPromptEvalCount().intValue());
        assertEquals(1160282000L, response.body().getPromptEvalDuration().intValue());
        assertEquals(13, response.body().getEvalCount().intValue());
        assertEquals(1325948000L, response.body().getEvalDuration().longValue());
    }

    @Test
    public void testStreamingCompletion() throws IOException {
        // Arrange
        String jsonResponse = "{\"model\":\"llama2:7b\",\"created_at\":\"2023-08-04T08:52:19.385406455-07:00\",\"response\":\"The\",\"done\":false}";
        mockWebServer.enqueue(new MockResponse().setBody(jsonResponse));

        CompletionRequest completionRequest = new CompletionRequest("llama2:7b", "Why is the sky blue?", null, true);

        // Act
        Call<ResponseBody> call = ollamaApi.streamingCompletion(completionRequest);
        Response<ResponseBody> response = call.execute();

        // Assert
        assertTrue(response.isSuccessful());
        assertNotNull(response.body());

        // Read the streaming response
        try (ResponseBody body = response.body()) {
            BufferedSource source = body.source();
            String streamingResponse = source.readUtf8();
            // Assert the streaming response
            assertEquals("{\"model\":\"llama2:7b\",\"created_at\":\"2023-08-04T08:52:19.385406455-07:00\",\"response\":\"The\",\"done\":false}", streamingResponse);
        }
    }

    @Test
    public void testEmbed() throws IOException {
        // Arrange
        String jsonResponse = "{\"embedding\":[0.5670403838157654,0.009260174818336964,0.23178744316101074,-0.2916173040866852,-0.8924556970596313,0.8785552978515625,-0.34576427936553955,0.5742510557174683,-0.04222835972905159,-0.137906014919281]}";
        mockWebServer.enqueue(new MockResponse().setBody(jsonResponse));

        EmbeddingRequest embeddingRequest = new EmbeddingRequest("llama2:7b", "Here is an article about llamas...");

        // Act
        Call<EmbeddingResponse> call = ollamaApi.embed(embeddingRequest);
        Response<EmbeddingResponse> response = call.execute();

        // Assert
        assertTrue(response.isSuccessful());
        assertNotNull(response.body());
        assertNotNull(response.body().getEmbedding());
        assertEquals(10, response.body().getEmbedding().length);
        assertEquals(0.5670403838157654, response.body().getEmbedding()[0], 0.0001);
        assertEquals(0.009260174818336964, response.body().getEmbedding()[1], 0.0001);
        assertEquals(0.23178744316101074, response.body().getEmbedding()[2], 0.0001);
        assertEquals(-0.2916173040866852, response.body().getEmbedding()[3], 0.0001);
        assertEquals(-0.8924556970596313, response.body().getEmbedding()[4], 0.0001);
        assertEquals(0.8785552978515625, response.body().getEmbedding()[5], 0.0001);
        assertEquals(-0.34576427936553955, response.body().getEmbedding()[6], 0.0001);
        assertEquals(0.5742510557174683, response.body().getEmbedding()[7], 0.0001);
        assertEquals(-0.04222835972905159, response.body().getEmbedding()[8], 0.0001);
        assertEquals(-0.137906014919281, response.body().getEmbedding()[9], 0.0001);
    }
}
