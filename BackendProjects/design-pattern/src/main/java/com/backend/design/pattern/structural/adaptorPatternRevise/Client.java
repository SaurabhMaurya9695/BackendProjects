package com.backend.design.pattern.structural.adaptorPatternRevise;

import com.backend.design.pattern.structural.adaptorPatternRevise.Target.XmlDataSource;

// --- Client (uses only XmlDataSource) ------------------------------------
public class Client {

    public void process(XmlDataSource source) {
        String xml = source.getXml();
        System.out.println("Client received XML: \n" + xml);
        // The client remains XML-only and doesn’t care about JSON.
    }
}
