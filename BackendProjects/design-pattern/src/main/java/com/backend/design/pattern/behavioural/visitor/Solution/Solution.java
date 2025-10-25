package com.backend.design.pattern.behavioural.visitor.Solution;

/**
 * ✅ Solution: Applying the Visitor Design Pattern
 * ------------------------------------------------------------
 * The Visitor Pattern helps us achieve **Open/Closed Principle (OCP)**
 * by separating operations (like calculating size, compression, encryption, etc.)
 * from the object structure (like Text, Pdf, Image, etc.).
 * <p>
 * Each new operation can be added **without modifying existing file classes**.
 * <p>
 * ✅ Fixes Achieved:
 * ------------------------------------------------------------
 * 1️⃣ **Open/Closed Principle (OCP)**
 * - New operations (visitors) can be added without changing file classes.
 * - Example: Add a new `EncryptVisitor` without touching `TextFile` or `PdfFile`.
 * <p>
 * 2️⃣ **Interface Segregation Principle (ISP)**
 * - Each visitor interface is specialized for operations, not bloated with irrelevant methods.
 * <p>
 * 3️⃣ **Single Responsibility Principle (SRP)**
 * - File classes only describe what they are (structure/data).
 * - Visitor classes describe what to do with those files (behavior).
 * <p>
 * ------------------------------------------------------------
 * Example Workflow:
 * FileElement text = new TextFile("document.txt");
 * FileOperation sizeVisitor = new SizeVisitor();
 * text.accept(sizeVisitor);
 * <p>
 * Output:
 * [TextFile] Calculating size for: document.txt
 * <p>
 * ------------------------------------------------------------
 * Extensibility:
 * ✅ Add new file type → Just create a new class implementing FileElement.
 * ✅ Add new operation → Just create a new class implementing FileOperation.
 * ❌ No need to modify any existing code.
 */

// ------------------------------------------------------------
// STEP 1: Element Interface (Base for all file types)
// ------------------------------------------------------------
interface FileElement {

    void accept(FileOperation operation);
}

// ------------------------------------------------------------
// STEP 2: Concrete Elements (Specific file types)
// ------------------------------------------------------------

/**
 * Represents a Text File in the system.
 * It only defines structure (its name) and how it accepts a visitor.
 * It does NOT define operations like compress or calculateSize directly.
 */
class TextFile implements FileElement {

    private final String fileName;

    public TextFile(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public void accept(FileOperation operation) {
        // Double dispatch: delegates operation to the visitor
        operation.visit(this);
    }
}

/**
 * Represents a PDF File in the system.
 * New operations can be added through visitors without modifying this class.
 */
class PdfFile implements FileElement {

    private final String fileName;

    public PdfFile(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public void accept(FileOperation operation) {
        operation.visit(this);
    }
}

// ------------------------------------------------------------
// STEP 3: Visitor Interface (Defines operations)
// ------------------------------------------------------------

/**
 * Defines operations (behaviors) that can be applied to file elements.
 * Each visitor can perform a different type of operation (e.g., size, compress, encrypt).
 * <p>
 * Adding new operations → create a new class implementing FileOperation.
 * No changes required to TextFile or PdfFile classes.
 */
interface FileOperation {

    void visit(TextFile textFile);

    void visit(PdfFile pdfFile);
}

// ------------------------------------------------------------
// STEP 4: Concrete Visitors (Specific behaviors)
// ------------------------------------------------------------

/**
 * Concrete Visitor that calculates size for different file types.
 * Demonstrates polymorphic behavior for TextFile and PdfFile.
 */
class SizeVisitor implements FileOperation {

    @Override
    public void visit(TextFile textFile) {
        System.out.println("[TextFile] Calculating size for: " + textFile.getFileName());
    }

    @Override
    public void visit(PdfFile pdfFile) {
        System.out.println("[PdfFile] Calculating size for: " + pdfFile.getFileName());
    }
}

/**
 * Concrete Visitor that performs compression on files.
 * Notice: We added this without changing TextFile or PdfFile.
 * ✅ Demonstrates OCP compliance.
 */
class CompressionVisitor implements FileOperation {

    @Override
    public void visit(TextFile textFile) {
        System.out.println("[TextFile] Compressing: " + textFile.getFileName());
    }

    @Override
    public void visit(PdfFile pdfFile) {
        System.out.println("[PdfFile] Compressing: " + pdfFile.getFileName());
    }
}

// ------------------------------------------------------------
// STEP 5: Client Code (Demonstrates Visitor Pattern)
// ------------------------------------------------------------

/**
 * Demonstrates the use of the Visitor Pattern to perform operations
 * on different file types without modifying their class definitions.
 */
public class Solution {

    public static void main(String[] args) {
        // Create elements
        FileElement text = new TextFile("document.txt");
        FileElement pdf = new PdfFile("report.pdf");

        // Create visitors (operations)
        FileOperation sizeVisitor = new SizeVisitor();
        FileOperation compressionVisitor = new CompressionVisitor();

        // Apply operations
        text.accept(sizeVisitor);
        pdf.accept(sizeVisitor);

        text.accept(compressionVisitor);
        pdf.accept(compressionVisitor);
    }
}

