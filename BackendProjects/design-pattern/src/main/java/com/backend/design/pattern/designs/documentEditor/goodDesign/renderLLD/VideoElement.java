package com.backend.design.pattern.designs.documentEditor.goodDesign.renderLLD;

public class VideoElement extends DocumentElement {

    private final String _text;

    public VideoElement(String text) {
        this._text = text;
    }

    @Override
    public String render() {
        System.out.println("Rendering VideoElement");
        if (!_text.isEmpty()) {
            System.out.print("Rendering VideoElement : " + _text);
        }
        return _text;
    }
}
