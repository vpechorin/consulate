package net.pechorina.consulate.utils

import net.pechorina.consulate.exceptions.RESTException
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response

class HttpClient {
    OkHttpClient client;
    static final MediaType JSON = MediaType.parse("application/json; charset=utf-8")

    static HttpClient newClient() {
        HttpClient c = new HttpClient(
                client: new OkHttpClient.Builder().addInterceptor(new LoggingInterceptor()).build()
        )
        return c
    }

    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response
        try {
            response = client.newCall(request).execute();
        }
        catch (IOException e) {
            throw new RESTException("Error on POST $url ", e)
        }

        if (response && !response.isSuccessful()) throw new RESTException("Unexpected code " + response);

        return response.body().string();
    }

    String put(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();

        Response response
        try {
            response = client.newCall(request).execute();
        }
        catch (IOException e) {
            throw new RESTException("Error on PUT $url ", e)
        }

        if (response && !response.isSuccessful()) throw new RESTException("Unexpected code " + response);

        return response.body().string();
    }

    String get(String url) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response
        try {
            response = client.newCall(request).execute();
        }
        catch (IOException e) {
            throw new RESTException("Error on GET $url ", e)
        }

        if (response && !response.isSuccessful()) throw new RESTException("Unexpected code " + response);

        return response.body().string();
    }

    String delete(String url) {
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();
        Response response
        try {
            response = client.newCall(request).execute();
        }
        catch (IOException e) {
            throw new RESTException("Error on DELETE $url ", e)
        }

        if (response && !response.isSuccessful()) throw new RESTException("Unexpected code " + response);

        return response.body().string();
    }

}
