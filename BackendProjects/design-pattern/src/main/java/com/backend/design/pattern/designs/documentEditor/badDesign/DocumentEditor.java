package com.backend.design.pattern.designs.documentEditor.badDesign;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code DocumentEditor} class is responsible for managing document content,
 * rendering it, and saving it to a file.
 * <p>
 * This class currently supports adding text, videos, and images into a single list
 * and rendering or saving them. However, it violates the SOLID design principles:
 * <ul>
 *   <li><strong>SRP (Single Responsibility Principle)</strong>: Handles content addition, rendering, and saving.</li>
 *   <li><strong>OCP (Open/Closed Principle)</strong>: Cannot easily extend to support new media types.</li>
 *   <li><strong>DIP (Dependency Inversion Principle)</strong>: Depends on concrete logic, not abstractions.</li>
 * </ul>
 * <p>
 * Refactoring this class is recommended to follow better object-oriented design principles.
 * <b>IN SHOT THIS IS A BAD DESIGN </b>
 *
 * @author Saurabh Maurya
 */
public class DocumentEditor {

    /**
     * List to store all added components (text, images, videos).
     */
    private final List<String> lst = new ArrayList<>();

    /**
     * Holds the last filename associated with video or image.
     */
    String fileName = null;

    /**
     * Adds textual content to the document.
     *
     * @param text the text content to add
     */
    public void addText(String text) {
        lst.add(text);
    }

    /**
     * Adds a video to the document.
     * Also updates the filename to the video's name.
     *
     * @param videos the video content to add
     */
    public void addVideos(String videos) {
        lst.add(videos);
        fileName = videos;
    }

    /**
     * Adds an image to the document.
     * Also updates the filename to the image's name.
     *
     * @param images the image content to add
     */
    public void addImages(String images) {
        lst.add(images);
        fileName = images;
    }

    /**
     * Renders the document by iterating over all its contents.
     * Currently, it only loops through the list without actual rendering logic.
     */
    public void renderDoc() {
        if (!lst.isEmpty()) {
            for (String item : lst) {
                System.out.println("Rendering: " + item);
            }
        } else {
            System.out.println("Document is empty.");
        }
    }

    /**
     * Saves the document to a file using the last video/image filename as the reference.
     * <p>
     * This method currently lacks actual file I/O logic and uses fileName as a placeholder.
     */
    public void saveToFile() {
        if (fileName != null && !fileName.isEmpty()) {
            System.out.println("Saving document to file: " + fileName);
            // File saving logic would go here
        } else {
            System.out.println("No filename specified for saving.");
        }
    }
}
