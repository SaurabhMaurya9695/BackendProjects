package com.backend.design.pattern.creational.builderPatternRevised.SimpleBuilder;

import java.util.*;

/**
 * Represents an HTTP request that can be built using the {@link HttpRequestBuilder}.
 * <p>
 * This class follows the <b>Builder Design Pattern</b> to allow easy and flexible
 * creation of HTTP request objects with configurable fields such as URL, headers,
 * query parameters, method, body, and timeout.
 * </p>
 *
 * <p>
 * Example usage:
 * <pre>{@code
 * HttpRequest request = new HttpRequest.HttpRequestBuilder()
 *     .withUrl("https://api.example.com/users")
 *     .withMethod("GET")
 *     .withHeader("Authorization", "Bearer token123")
 *     .withQueryParams("limit", "10")
 *     .withTimeout(30)
 *     .build();
 *
 * request.execute();
 * }</pre>
 * </p>
 *
 * @author Saurabh
 * @version 1.0
 */
public class HttpRequest {

    /**
     * The request URL.
     */
    private String url;

    /**
     * The HTTP method (e.g., GET, POST, PUT, DELETE).
     */
    private String method;

    /**
     * Map of request headers.
     */
    private Map<String, String> _headers;

    /**
     * Map of query parameters to be appended to the URL.
     */
    private Map<String, String> queryParams;

    /**
     * Request body (used mainly for POST/PUT methods).
     */
    private String body;

    /**
     * Request timeout in seconds.
     */
    private int timeout;

    /**
     * Private constructor to enforce object creation only through the builder.
     */
    HttpRequest() {
        _headers = new HashMap<>();
        queryParams = new HashMap<>();
        body = "";
    }

    /**
     * Executes the HTTP request by printing its configuration details to the console.
     * <p>
     * Note: This method does not actually perform a real network operation.
     * It simply simulates the execution of the request.
     * </p>
     */
    public void execute() {
        System.out.println("Executing " + method + " request to " + url);

        if (!queryParams.isEmpty()) {
            System.out.println("Query Parameters:");
            for (Map.Entry<String, String> param : queryParams.entrySet()) {
                System.out.println("  " + param.getKey() + "=" + param.getValue());
            }
        }

        System.out.println("Headers:");
        for (Map.Entry<String, String> header : _headers.entrySet()) {
            System.out.println("  " + header.getKey() + ": " + header.getValue());
        }

        if (body != null && !body.isEmpty()) {
            System.out.println("Body: " + body);
        }

        System.out.println("Timeout: " + timeout + " seconds");
        System.out.println("Request executed successfully!");
    }

    /**
     * Builder class for constructing instances of {@link HttpRequest}.
     * <p>
     * Follows the fluent API design, allowing method chaining for building a request.
     * </p>
     */
    public static class HttpRequestBuilder {

        /**
         * Internal {@link HttpRequest} instance being constructed.
         */
        private HttpRequest req;

        /**
         * Constructs a new {@code HttpRequestBuilder}.
         */
        public HttpRequestBuilder() {
            req = new HttpRequest();
        }

        /**
         * Sets the request URL.
         *
         * @param u the URL to set
         * @return the current {@link HttpRequestBuilder} instance
         */
        public HttpRequestBuilder withUrl(String u) {
            req.url = u;
            return this;
        }

        /**
         * Sets the HTTP method (e.g., GET, POST, PUT).
         *
         * @param method the HTTP method
         * @return the current {@link HttpRequestBuilder} instance
         */
        public HttpRequestBuilder withMethod(String method) {
            req.method = method;
            return this;
        }

        /**
         * Adds a header to the HTTP request.
         *
         * @param key   the header name
         * @param value the header value
         * @return the current {@link HttpRequestBuilder} instance
         */
        public HttpRequestBuilder withHeader(String key, String value) {
            req._headers.put(key, value);
            return this;
        }

        /**
         * Adds a query parameter to the HTTP request.
         *
         * @param key   the query parameter name
         * @param value the query parameter value
         * @return the current {@link HttpRequestBuilder} instance
         */
        public HttpRequestBuilder withQueryParams(String key, String value) {
            req.queryParams.put(key, value);
            return this;
        }

        /**
         * Sets the body of the HTTP request.
         *
         * @param body the body content as a String
         * @return the current {@link HttpRequestBuilder} instance
         */
        public HttpRequestBuilder withBody(String body) {
            req.body = body;
            return this;
        }

        /**
         * Sets the timeout for the HTTP request.
         *
         * @param timeout timeout value in seconds
         * @return the current {@link HttpRequestBuilder} instance
         */
        public HttpRequestBuilder withTimeout(int timeout) {
            req.timeout = timeout;
            return this;
        }

        /**
         * Builds and returns the configured {@link HttpRequest} instance.
         * <p>
         * Performs basic validation before returning the object.
         * </p>
         *
         * @return the fully constructed {@link HttpRequest} instance
         * @throws RuntimeException if the URL is missing or invalid
         */
        public HttpRequest build() {
            if (req.url == null || req.url.isEmpty()) {
                throw new RuntimeException("URL cannot be empty");
            }
            return req;
        }
    }
}
