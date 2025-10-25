package com.backend.design.pattern.behavioural.visitor.Problem;

/**
 * ❌ Problem Explanation:
 * ------------------------------------------------------------
 * This code violates several SOLID principles:
 * <p>
 * 1️⃣ **Open/Closed Principle (OCP) Violation**
 * - The `FileVisitor` interface is not closed for modification.
 * - If we need to add a new operation like `encrypt()`, `preview()`, or `extractMetadata()`,
 * we must modify this interface and then also modify *all* implementing classes (`Text`, `Pdf`, etc.).
 * - Hence, existing classes must be changed whenever a new operation is added.
 * <p>
 * 2️⃣ **Interface Segregation Principle (ISP) Violation**
 * - Both `Text` and `Pdf` classes are forced to implement *all* methods of the interface
 * even if some of them are irrelevant for that class (e.g., `compress()` for `Text` files may not make sense).
 * - This creates a "fat interface" — one that forces clients to depend on methods they do not use.
 * <p>
 * 3️⃣ **Single Responsibility Principle (SRP) Violation**
 * - The `FileVisitor` interface handles multiple responsibilities — size calculation, compression, etc.
 * - Any change to one responsibility affects all classes implementing it.
 * - As a result, there is tight coupling and low cohesion.
 * <p>
 * 💡 In short:
 * - Every time a new behavior is added, you modify existing code.
 * - This breaks modularity and flexibility.
 * <p>
 * ✅ Correct Approach (using Visitor Pattern):
 * - Define separate "visitor" objects that can be applied to different file types.
 * - Allows new operations (visitors) to be added without changing file classes.
 */

interface FileVisitor {

    // ❌ These methods represent different types of operations that should ideally
    // be handled by separate visitors — not all together in one interface.
    void calculateSize(String file);

    void compress(String image);
}

/**
 * ❌ 'Text' implements all methods of FileVisitor,
 * even if 'compress' doesn't make much sense for text files.
 * This shows violation of Interface Segregation Principle (ISP).
 */
class Text implements FileVisitor {

    @Override
    public void calculateSize(String file) {
        System.out.println("[Text] calculating size of file " + file);
    }

    @Override
    public void compress(String image) {
        System.out.println("[Text] compressing size of image " + image);
    }
}

/**
 * ❌ 'Pdf' also implements both methods, and will need to change
 * whenever a new operation is added to FileVisitor.
 * This demonstrates Open/Closed Principle (OCP) violation.
 */
class Pdf implements FileVisitor {

    @Override
    public void calculateSize(String file) {
        System.out.println("[Pdf] calculating size of file " + file);
    }

    @Override
    public void compress(String image) {
        System.out.println("[Pdf] compressing size of image " + image);
    }
}

/**
 * This class represents the problem setup.
 * In a correct Visitor Pattern solution, each file type (Text, Pdf, Image, etc.)
 * would have an `accept()` method, and operations like "calculateSize" or "compress"
 * would be implemented as separate visitors.
 */
public class Problem {

}
