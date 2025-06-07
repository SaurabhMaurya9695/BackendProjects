package com.backend.design.pattern.designs.documentEditor.goodDesign;

import com.backend.design.pattern.designs.documentEditor.goodDesign.renderLLD.TextElement;
import com.backend.design.pattern.designs.documentEditor.goodDesign.renderLLD.VideoElement;
import com.backend.design.pattern.designs.documentEditor.goodDesign.saveLLD.Persistence;

public class DocumentEditor {

    private final Document _document;
    private final Persistence _persistence;
    private String _renderedDocuments;

    public DocumentEditor(Document document, Persistence persistence) {
        this._document = document;
        this._persistence = persistence;
    }

    public void addText(String text) {
        _document.addElement(new TextElement("Added TextElement " + text));
    }

    public void addVideo(String thumbnail) {
        _document.addElement(new VideoElement("Added VideoThumbnail" + thumbnail));
    }

    public String renderDocuments() {
        if (_renderedDocuments == null) {
            this._renderedDocuments = _document.renderDocuments();
        }
        return _renderedDocuments;
    }

    public void saveDocuments() {
        _persistence.save(renderDocuments());
    }
}
