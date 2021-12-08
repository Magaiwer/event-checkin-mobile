package dev.magaiver.eventcheckin.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;


public class ApiTemplate {

    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    public static final int INTERNET = 1;

    public static Call post(String pathUrl, Object object, String token) throws Exception {
        String objectJson = new ObjectMapper().writeValueAsString(object);

        OkHttpClient okHttpClient = getUnsafeOkHttpClient();

        RequestBody body = RequestBody.create(objectJson, MEDIA_TYPE_JSON);
        Builder builder = new Builder().url(Routes.URL_BASE + pathUrl);
        addTokenHeader(Routes.TOKEN, builder);

        builder.post(body);
        return okHttpClient.newCall(builder.build());
    }

    public static Call get(String pathUrl, String token, String eTag) throws Exception {
        OkHttpClient okHttpClient = getUnsafeOkHttpClient();
        Builder builder = new Builder().url(Routes.URL_BASE + pathUrl);
        addTokenHeader(Routes.TOKEN, builder);

        if (!eTag.equals(""))
            builder.header("If-None-Match", eTag);

        return okHttpClient.newCall(builder.build());
    }

    private static void addTokenHeader(String token, Builder builder) {
        if (!token.equals(""))
            builder.header("Authorization", "Bearer " + token);
    }


    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }
}
