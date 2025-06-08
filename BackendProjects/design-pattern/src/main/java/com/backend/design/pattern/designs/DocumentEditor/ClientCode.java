package com.backend.design.pattern.designs.DocumentEditor;

import com.backend.design.pattern.designs.DocumentEditor.goodDesign.Document;
import com.backend.design.pattern.designs.DocumentEditor.goodDesign.DocumentEditor;
import com.backend.design.pattern.designs.DocumentEditor.goodDesign.saveLLD.Persistence;
import com.backend.design.pattern.designs.DocumentEditor.goodDesign.saveLLD.SaveToDb;

public class ClientCode {

    public static void main(String[] args) {
        Document document = new Document();
        Persistence persistence = new SaveToDb();

        DocumentEditor documentEditor = new DocumentEditor(document, persistence);

        documentEditor.addText("Hello From Client Code");
        documentEditor.addVideo("Hello From Client Code.jpeg");
        System.out.println("Rendering doc in client code: " + documentEditor.renderDocuments());
        documentEditor.saveDocuments();
    }
}
