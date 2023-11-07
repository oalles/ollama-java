package es.omarall.ollama;

import com.google.gson.Gson;
import es.omarall.ollama.exceptions.BadRequestException;
import es.omarall.ollama.exceptions.InternalServerException;
import es.omarall.ollama.exceptions.NotFoundException;
import es.omarall.ollama.impl.OllamaApi;
import es.omarall.ollama.impl.OllamaServiceImpl;
import es.omarall.ollama.model.CompletionRequest;
import es.omarall.ollama.model.CompletionResponse;
import es.omarall.ollama.model.CopyRequest;
import es.omarall.ollama.model.DeleteRequest;
import es.omarall.ollama.model.EmbeddingRequest;
import es.omarall.ollama.model.EmbeddingResponse;
import es.omarall.ollama.model.ShowRequest;
import es.omarall.ollama.model.ShowResponse;
import es.omarall.ollama.model.TagsResponse;
import okhttp3.ResponseBody;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OllamaServiceImplTest {
    @Mock
    private OllamaApi ollamaApi;

    @Mock
    private Gson gson;

    private OllamaServiceImpl ollamaService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ollamaService = new OllamaServiceImpl(ollamaApi, gson);
    }

    @Test
    public void testCompletionSuccess() throws IOException {
        CompletionRequest request = new CompletionRequest();
        Response<CompletionResponse> retrofitResponse = Response.success(new CompletionResponse());

        when(ollamaApi.completion(request)).thenReturn(mock(Call.class));
        when(ollamaApi.completion(request).execute()).thenReturn(retrofitResponse);

        CompletionResponse response = ollamaService.completion(request);
        verify(ollamaApi.completion(request), times(1)).execute();
    }

    @Test(expected = InternalServerException.class)
    public void testCompletion500Failure() throws IOException {
        CompletionRequest request = new CompletionRequest();
        Response<CompletionResponse> retrofitResponse = Response.error(500, mock(ResponseBody.class));

        when(ollamaApi.completion(request)).thenReturn(mock(Call.class));
        when(ollamaApi.completion(request).execute()).thenReturn(retrofitResponse);

        ollamaService.completion(request);
    }

    @Test(expected = BadRequestException.class)
    public void testCompletion400Failure() throws IOException {
        CompletionRequest request = new CompletionRequest();
        Response<CompletionResponse> retrofitResponse = Response.error(400, mock(ResponseBody.class));

        when(ollamaApi.completion(request)).thenReturn(mock(Call.class));
        when(ollamaApi.completion(request).execute()).thenReturn(retrofitResponse);

        ollamaService.completion(request);
    }

    @Test
    public void testEmbedSuccess() throws IOException {
        EmbeddingRequest request = new EmbeddingRequest();
        Response<EmbeddingResponse> retrofitResponse = Response.success(new EmbeddingResponse());

        when(ollamaApi.embed(request)).thenReturn(mock(Call.class));
        when(ollamaApi.embed(request).execute()).thenReturn(retrofitResponse);

        EmbeddingResponse response = ollamaService.embed(request);
        verify(ollamaApi.embed(request), times(1)).execute();
    }


    @Test
    public void testGetTagsSuccess() throws IOException {
        TagsResponse expectedResponse = new TagsResponse();
        Response<TagsResponse> retrofitResponse = Response.success(expectedResponse);

        when(ollamaApi.getTags()).thenReturn(mock(Call.class));
        when(ollamaApi.getTags().execute()).thenReturn(retrofitResponse);

        TagsResponse response = ollamaService.getTags();
        verify(ollamaApi.getTags(), times(1)).execute();
    }

    @Test
    public void testShowSuccess() throws IOException {
        ShowRequest request = new ShowRequest();
        Response<ShowResponse> retrofitResponse = Response.success(new ShowResponse());

        when(ollamaApi.show(request)).thenReturn(mock(Call.class));
        when(ollamaApi.show(request).execute()).thenReturn(retrofitResponse);

        ShowResponse response = ollamaService.show(request);
        verify(ollamaApi.show(request), times(1)).execute();
    }

    @Test(expected = InternalServerException.class)
    public void testShowFailure() throws IOException {
        ShowRequest request = new ShowRequest();
        Response<ShowResponse> retrofitResponse = Response.error(500, mock(ResponseBody.class));

        when(ollamaApi.show(request)).thenReturn(mock(Call.class));
        when(ollamaApi.show(request).execute()).thenReturn(retrofitResponse);
        ollamaService.show(request);
    }

    @Test
    public void testCopySuccess() throws IOException {
        CopyRequest copyRequest = new CopyRequest("llama2:7b", "llama2-backup");

        Response<Void> successfulResponse = Response.success(null);
        Call<Void> call = mock(Call.class);
        when(call.execute()).thenReturn(successfulResponse);
        when(ollamaApi.copy(copyRequest)).thenReturn(call);

        ollamaService.copy(copyRequest);

        verify(ollamaApi).copy(copyRequest);
        verify(call).execute();
    }

    @Test(expected = NotFoundException.class)
    public void testCopyNotFoundFailure() throws IOException {
        CopyRequest copyRequest = new CopyRequest("imaginary-model:latest", "anyway:latest");

        Response<Void> errorResponse = Response.error(404, mock(ResponseBody.class));
        Call<Void> call = mock(Call.class);
        when(call.execute()).thenReturn(errorResponse);
        when(ollamaApi.copy(copyRequest)).thenReturn(call);

        ollamaService.copy(copyRequest);
    }

    @Test
    public void testDeleteSuccess() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest("llama2:7b");
        Response<Void> successfulResponse = Response.success(null);
        Call<Void> call = mock(Call.class);
        when(call.execute()).thenReturn(successfulResponse);
        when(ollamaApi.delete(deleteRequest)).thenReturn(call);

        ollamaService.delete("llama2:7b");

        verify(ollamaApi).delete(deleteRequest);
        verify(call).execute();
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteNotFoundFailure() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest("non-existent:7b");
        Response<Void> errorResponse = Response.error(404, mock(ResponseBody.class));
        Call<Void> call = mock(Call.class);
        when(call.execute()).thenReturn(errorResponse);
        when(ollamaApi.delete(deleteRequest)).thenReturn(call);
        ollamaService.delete("non-existent:7b");
    }
}
