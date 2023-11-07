package es.omarall.ollama;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.omarall.ollama.impl.OllamaApi;
import es.omarall.ollama.impl.OllamaServiceImpl;
import es.omarall.ollama.properties.OllamaProperties;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.time.Duration;

import static java.time.Duration.ofMillis;

public class OllamaServiceFactory {

    public static OllamaService create(OllamaProperties properties, Gson gson) {
        // Validate input
        if (properties == null) {
            throw new IllegalArgumentException("Ollama properties is required");
        }
        if (properties.getBaseUrl() == null || properties.getBaseUrl().isBlank()) {
            //TODO; Validate URL
            throw new IllegalArgumentException("baseUrl is required");
        }

        Duration callTimeout = ofMillis(properties.getCallTimeout());
        Duration connectTimeout = ofMillis(properties.getConnectTimeout());
        Duration readTimeout = ofMillis(properties.getReadTimeout());
        Duration writeTimeout = ofMillis(properties.getWriteTimeout());
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .callTimeout(callTimeout)
                .connectTimeout(connectTimeout)
                .readTimeout(readTimeout)
                .writeTimeout(writeTimeout)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(properties.getBaseUrl())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        OllamaApi ollamaApi = retrofit.create(OllamaApi.class);
        return new OllamaServiceImpl(ollamaApi, gson);
    }

    public static OllamaService create(OllamaProperties properties) {
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        return create(properties, gson);
    }
}