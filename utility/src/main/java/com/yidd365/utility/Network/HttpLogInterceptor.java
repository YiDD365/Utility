package com.yidd365.utility.Network;

import com.yidd365.utility.ILogger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpEngine;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by orinchen on 16/5/21.
 */

public final class HttpLogInterceptor implements Interceptor {

    private static final Charset UTF8 = Charset.forName("UTF-8");

    public enum Level {
        /** No logs. */
        NONE,
        /**
         * Logs request and response lines.
         *
         * <p>Example:
         * <pre>{@code
         * --> POST /greeting http/1.1 (3-byte body)
         *
         * <-- 200 OK (22ms, 6-byte body)
         * }</pre>
         */
        BASIC,
        /**
         * Logs request and response lines and their respective headers.
         *
         * <p>Example:
         * <pre>{@code
         * --> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         * --> END POST
         *
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         * <-- END HTTP
         * }</pre>
         */
        HEADERS,
        /**
         * Logs request and response lines and their respective headers and bodies (if present).
         *
         * <p>Example:
         * <pre>{@code
         * --> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         *
         * Hi?
         * --> END GET
         *
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         *
         * Hello!
         * <-- END HTTP
         * }</pre>
         */
        BODY
    }


    public HttpLogInterceptor() {
        this(ILogger.DEFAULT);
    }

    public HttpLogInterceptor(ILogger logger) {
        this.logger = logger;
    }

    private final ILogger logger;
    private volatile Level level = Level.NONE;

    /** Change the level at which this interceptor logs. */
    public HttpLogInterceptor setLevel(HttpLogInterceptor.Level level) {
        if (level == null) throw new NullPointerException("level == null. Use Level.NONE instead.");
        this.level = level;
        return this;
    }

    public HttpLogInterceptor.Level getLevel() {
        return level;
    }

    @Override public Response intercept(Chain chain) throws IOException {
        HttpLogInterceptor.Level level = this.level;

        Request request = chain.request();
        if (level == HttpLogInterceptor.Level.NONE) {
            return chain.proceed(request);
        }

        boolean logBody = level == HttpLogInterceptor.Level.BODY;
        boolean logHeaders = logBody || level == HttpLogInterceptor.Level.HEADERS;

        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null && requestBody.contentLength()>0;

        Connection connection = chain.connection();
        Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;

        StringBuilder logBuilder = new StringBuilder(1024);

        logBuilder.append(String.format("--> %s %s %s \n", request.method(), request.url(), protocol));

        if (!logHeaders && hasRequestBody) {
            logBuilder.append(String.format(" ( %d -byte body )\n", requestBody.contentLength()));
        }

        if (logHeaders) {
            if (hasRequestBody) {
                // Request body headers are only present when installed as a network interceptor. Force
                // them to be included (when available) so there values are known.
                if (requestBody.contentType() != null) {
                    logBuilder.append(String.format("Content-Type: %s\n", requestBody.contentType()));
                }
                if (requestBody.contentLength() != -1) {
                    logBuilder.append(String.format("Content-Length: %d\n", requestBody.contentLength()));
                }
            }

            Headers headers = request.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                String name = headers.name(i);
                // Skip headers from the request body as they are explicitly logged above.
                if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                    logBuilder.append(String.format("%s : %s \n", name, headers.value(i)));
                }
            }

            if (!logBody || !hasRequestBody) {
                logBuilder.append(String.format("--> END %s \n", request.method()));
            } else if (bodyEncoded(request.headers())) {
                logBuilder.append(String.format("--> END %s (encoded body omitted) \n", request.method()));
            } else {
                logBuilder.append("Request Body:\n");
                if(requestBody instanceof FormBody){
                    FormBody formBody = (FormBody) requestBody;
                    for(int i = 0; i<formBody.size(); i++){
                        logBuilder.append(
                                String.format("%s(%s) : %s(%s) \n",
                                        formBody.encodedName(i),
                                        formBody.name(i),
                                        formBody.encodedValue(i),
                                        formBody.value(i)));
                    }
                } else {
                    Buffer buffer = new Buffer();
                    requestBody.writeTo(buffer);

                    Charset charset = UTF8;
                    MediaType contentType = requestBody.contentType();
                    if (contentType != null) {
                        charset = contentType.charset(UTF8);
                    }
                    logBuilder.append(String.format("%s \n", buffer.readString(charset)));
                }
                logBuilder.append(String.format("--> END %s ( %d -byte body) \n", request.method(), requestBody.contentLength()));
            }
        }

        logBuilder.append("\n");

        long startNs = System.nanoTime();
        Response response = chain.proceed(request);
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";

        logBuilder.append(
                String.format("<-- %d %s %s ( %dms %s)\n",
                        response.code(),
                        response.message(),
                        response.request().url(),
                        tookMs,
                        (!logHeaders ? ", " + bodySize + " body" : "")));

        String responseContentType = "";
        if (logHeaders) {
            Headers headers = response.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                logBuilder.append(String.format("%s : %s \n", headers.name(i), headers.value(i)));
                if(headers.name(i).equalsIgnoreCase("Content-Type")){
                    responseContentType = headers.value(i);
                }
            }

            if (!logBody || !HttpEngine.hasBody(response)) {
                logBuilder.append("<-- END HTTP\n");
            } else if (bodyEncoded(response.headers())) {
                logBuilder.append("<-- END HTTP (encoded body omitted)\n");
            } else {
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                Buffer buffer = source.buffer();

                Charset charset = UTF8;
                MediaType contentType = responseBody.contentType();
                if (contentType != null) {
                    try {
                        charset = contentType.charset(UTF8);
                    } catch (UnsupportedCharsetException e) {
                        logBuilder.append("\nCouldn't decode the response body; charset is likely malformed.\n");
                        logBuilder.append("<-- END HTTP\n");
                        return response;
                    }
                }

                if (contentLength != 0) {
                    logBuilder.append("Response Content:\n");
                    String content = buffer.clone().readString(charset).trim();

                    if(responseContentType.equalsIgnoreCase("application/json")) {
                        try {
                            if (content.startsWith("{")) {
                                JSONObject jsonObject = new JSONObject(content);
                                content = jsonObject.toString(4);
                            }
                            if (content.startsWith("[")) {
                                JSONArray jsonArray = new JSONArray(content);
                                content = jsonArray.toString(4);
                            }
                        } catch (JSONException e) {
                        }
                    }
                    logBuilder.append(String.format("%s\n", content));
                }
                logBuilder.append(String.format("<-- END HTTP ( %d -byte body)\n", buffer.size()));
            }
        }
        this.logger.log(logBuilder.toString());
        return response;
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }
}
