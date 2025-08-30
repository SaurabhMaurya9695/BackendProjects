package com.backend.design.pattern.structural.proxyPattern;

import com.backend.design.pattern.structural.proxyPattern.Proxy.ImageProxy;

/**
 * The Proxy Design Pattern is a structural design pattern that provides a surrogate
 * or placeholder for another object to control access to it.
 * <p>
 * In this pattern:
 * <ul>
 *   <li>The <b>Subject</b> defines the common interface (e.g., {@code IImage}).</li>
 *   <li>The <b>RealSubject</b> is the actual implementation that does the real work
 *       (e.g., {@code RealImage}).</li>
 *   <li>The <b>Proxy</b> (e.g., {@code ImageProxy}) acts as a substitute, controlling access
 *       to the {@code RealImage} and deferring its creation until it is really needed.</li>
 * </ul>
 *
 * <p><b>Benefits:</b></p>
 * <ul>
 *   <li>Controls access to heavy or sensitive objects.</li>
 *   <li>Supports lazy initialization (e.g., load image only when {@code display()} is called).</li>
 *   <li>Provides an additional layer (security, logging, caching, etc.).</li>
 * </ul>
 *
 * <p><b>Example in this code:</b></p>
 * <ul>
 *   <li>{@code IImage} – Subject interface</li>
 *   <li>{@code RealImage} – Real object that loads and displays the image</li>
 *   <li>{@code ImageProxy} – Proxy that defers creation of {@code RealImage} until needed</li>
 *   <li>{@code Client} – Uses {@code IImage} without worrying whether it’s a proxy or real object</li>
 * </ul>
 *
 * <p><b>Usage:</b></p>
 * <pre>
 *     IImage iImage = new ImageProxy("Saurabh.jpeg");
 *     iImage.display(); // RealImage is loaded lazily and then displayed
 * </pre>
 */
public class Client {

    public static void main(String[] args) {
        IImage iImage = new ImageProxy("Saurabh.jpeg");
        iImage.display();
    }
}
