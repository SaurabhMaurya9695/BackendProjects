package com.backend.design.pattern.creational.builderPatternRevised.StepBuilder;

import java.util.*;

/**
 * Represents an HTTP request built using the <b>Step Builder Pattern</b>.
 * <p>
 * Enforces a specific order of building steps — URL → Method → Header → Optional fields.
 * </p>
 *
 * <pre>{@code
 * HttpRequest request = HttpRequest.HttpRequestStepBuilder.getBuilder()
 *     .withUrl("https://api.example.com")
 *     .withMethod("POST")
 *     .withHeader("Content-Type", "application/json")
 *     .withBody("{\"name\":\"Saurabh\"}")
 *     .withTimeout(30)
 *     .build();
 *
 * request.execute();
 * }</pre>
 */
public class HttpRequest {

    private String url;
    private String method;
    private Map<String, String> headers;
    private Map<String, String> queryParams;
    private String body;
    private int timeout; // in seconds

    /**
     * Private constructor; instances are created via the step builder.
     */
    private HttpRequest() {
        headers = new HashMap<>();
        queryParams = new HashMap<>();
        body = "";
    }

    /**
     * Simulates execution of the HTTP request by printing details to the console.
     */
    public void execute() {
        System.out.println("Executing " + method + " request to " + url);

        if (!queryParams.isEmpty()) {
            System.out.println("Query Parameters:");
            queryParams.forEach((k, v) -> System.out.println("  " + k + "=" + v));
        }

        System.out.println("Headers:");
        headers.forEach((k, v) -> System.out.println("  " + k + ": " + v));

        if (!body.isEmpty()) {
            System.out.println("Body: " + body);
        }

        System.out.println("Timeout: " + timeout + " seconds");
        System.out.println("Request executed successfully!");
    }

    /**
     * Step 1: Set URL.
     */
    interface UrlStep {

        MethodStep withUrl(String url);
    }

    /**
     * Step 2: Set HTTP method.
     */
    interface MethodStep {

        HeaderStep withMethod(String method);
    }

    /**
     * Step 3: Add headers.
     */
    interface HeaderStep {

        OptionalStep withHeader(String key, String value);
    }

    /**
     * Step 4: Optional fields and build.
     */
    interface OptionalStep {

        OptionalStep withBody(String body);

        OptionalStep withTimeout(int timeout);

        HttpRequest build();
    }

    /**
     * Concrete step builder implementing all steps in sequence.
     */
    static class HttpRequestStepBuilder implements UrlStep, MethodStep, HeaderStep, OptionalStep {

        private final HttpRequest req;

        private HttpRequestStepBuilder() {
            req = new HttpRequest();
        }

        /**
         * {@inheritDoc}
         */
        public MethodStep withUrl(String url) {
            req.url = url;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        public HeaderStep withMethod(String method) {
            req.method = method;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        public OptionalStep withHeader(String key, String value) {
            req.headers.put(key, value);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        public OptionalStep withBody(String body) {
            req.body = body;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        public OptionalStep withTimeout(int timeout) {
            req.timeout = timeout;
            return this;
        }

        /**
         * Final build step that returns the constructed {@link HttpRequest}.
         *
         * @throws RuntimeException if URL is missing
         */
        public HttpRequest build() {
            if (req.url == null || req.url.isEmpty()) {
                throw new RuntimeException("URL cannot be empty");
            }
            return req;
        }

        /**
         * Initiates the builder chain.
         *
         * @return the first step {@link UrlStep}
         */
        public static UrlStep getBuilder() {
            return new HttpRequestStepBuilder();
        }
    }
}
