package com.backend.design.pattern.structural.adaptorPatternRevise;

import com.backend.design.pattern.structural.adaptorPatternRevise.Adapter.JsonToXmlAdapter;
import com.backend.design.pattern.structural.adaptorPatternRevise.Target.XmlDataSource;
import com.backend.design.pattern.structural.adaptorPatternRevise.Target.convertTo.LegacyJsonService;

public class Main {

    public static void main(String[] args) {
        LegacyJsonService legacy = new LegacyJsonService(); // 3rd party service
        XmlDataSource adapter = new JsonToXmlAdapter(legacy); // implement a adaptor

        Client client = new Client();
        client.process(adapter);
    }

    //    Right now, your client does:
    //    client.process(adapter , json);
    //    But ideally, the client shouldn’t even see JSON. That’s the whole point of the adapter.
    //    The adapter should fetch JSON from the legacy service internally, then return XML.
}
