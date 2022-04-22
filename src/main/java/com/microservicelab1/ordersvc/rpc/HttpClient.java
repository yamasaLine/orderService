package com.microservicelab1.ordersvc.rpc;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HttpClient {
    private final OkHttpClient client = new OkHttpClient();
    private RequestBody emptyJsonBody = RequestBody.create("", MediaType.get("application/json; charset=utf-8"));
    private final Moshi moshi = new Moshi.Builder().build();

    public String doGetString(final String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public <T> T doGetWithType(final String url, Class<T> type) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            JsonAdapter<T> typedJsonAdapter = moshi.adapter(type);
            return typedJsonAdapter.fromJson(response.body().source());
        }
    }

    public String doPostAndGetString(final String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(emptyJsonBody)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
    }
}
