package com.backend.design.pattern.designs.DocumentEditor.goodDesign;

import com.backend.design.pattern.designs.DocumentEditor.goodDesign.renderLLD.DocumentElement;

import java.util.ArrayList;
import java.util.List;

public class Document {

    public List<DocumentElement> _elements = new ArrayList<>();
    // this element can be of any type , such as textElement , VideoElement

    public void addElement(DocumentElement _documentElement) {
        _elements.add(_documentElement);
    }

    // This Method loop through every DocumentElement and render the things
    public String renderDocuments() {
        if (!_elements.isEmpty()) {
            for (DocumentElement element : _elements) {
                 return element.render();
            }
        }
        return null;
    }
}
