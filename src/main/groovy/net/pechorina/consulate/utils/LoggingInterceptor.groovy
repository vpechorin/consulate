package net.pechorina.consulate.utils

import groovy.util.logging.Slf4j
import okhttp3.Request
import okhttp3.Response
import okhttp3.Interceptor

@Slf4j
class LoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        log.debug(String.format("Sending request %s on %s", request.url(), chain.connection()));

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();
        log.debug(String.format("Received response for %s in %.1fms", response.request().url(), (t2 - t1) / 1e6d));

        return response;
    }
}