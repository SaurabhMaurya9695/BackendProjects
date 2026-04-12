package com.backend.ai.springai.config;

import org.springframework.context.annotation.Configuration;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.security.cert.X509Certificate;

/**
 * Disables SSL verification for outgoing HTTP calls.
 * Spring AI 1.0.0 uses RestClient which respects the JVM default SSL context,
 * so setting it here applies to all outgoing calls including Anthropic API.
 */
@Configuration
public class AnthropicSslConfig {

    static {
        try {
            X509TrustManager trustAll = new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            };

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[] { trustAll }, new java.security.SecureRandom());

            SSLContext.setDefault(sslContext);
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        } catch (Exception e) {
            throw new RuntimeException("Failed to disable SSL verification", e);
        }
    }
}
