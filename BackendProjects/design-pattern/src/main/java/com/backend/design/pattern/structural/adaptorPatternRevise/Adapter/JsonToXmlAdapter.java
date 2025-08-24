package com.backend.design.pattern.structural.adaptorPatternRevise.Adapter;

import com.backend.design.pattern.structural.adaptorPatternRevise.Target.XmlDataSource;
import com.backend.design.pattern.structural.adaptorPatternRevise.Target.convertTo.LegacyJsonService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Adapter: Converts JSON (This we have)  → XML (Customer expects)
// JsonToXmlAdapter maintain 'has-a' relationship with Adaptee and 'Is-a' relationship with Target
public class JsonToXmlAdapter implements XmlDataSource {

    private final LegacyJsonService _jsonService;

    public JsonToXmlAdapter(LegacyJsonService jsonService) {
        this._jsonService = jsonService;
    }

    @Override
    public String getXml() {
        String json = _jsonService.getJson();
        // Convert JSON → XML. In production, use a robust library.
        // Below is a minimal converter for demonstration (limited support)
        return jsonToXmlSimple(json, "root");
    }

    // Very small demo JSON→XML converter (handles flat values & arrays of flat objects)
    private String jsonToXmlSimple(String json, String rootName) {
        // Remove surrounding whitespace
        json = json.trim();

        // For an actual implementation, prefer org.json:
        // JSONObject obj = new JSONObject(json);
        // String xmlBody = XML.toString(obj);
        // return "<" + rootName + ">" + xmlBody + "</" + rootName + ">";

        // --- Start of naive demo conversion (not for production) ---
        StringBuilder xml = new StringBuilder();
        xml.append("<").append(rootName).append(">");

        // Parse top-level simple fields and one array named "items"
        Map<String, String> simple = new LinkedHashMap<>();
        List<Map<String, String>> items = new ArrayList<>();

        String[] lines = json.replace("{", "").replace("}", "").replace("[", "").replace("]", "").split("\n");

        Map<String, String> currentItem = null;
        boolean inItems = false;

        for (String raw : lines) {
            String line = raw.trim();
            if (line.isEmpty()) {
                continue;
            }

            // Remove trailing commas
            if (line.endsWith(",")) {
                line = line.substring(0, line.length() - 1);
            }

            if (line.startsWith("\"items\"")) {
                inItems = true;
                continue;
            }

            if (inItems) {
                if (line.startsWith("\"sku\"")) {
                    // Start of a new item
                    if (currentItem != null) {
                        items.add(currentItem);
                    }
                    currentItem = new LinkedHashMap<>();
                }
                String[] kv = splitKeyValue(line);
                if (kv != null && currentItem != null) {
                    currentItem.put(kv[0], kv[1]);
                }
                continue;
            }

            String[] kv = splitKeyValue(line);
            if (kv != null) {
                simple.put(kv[0], kv[1]);
            }
        }

        if (currentItem != null && !currentItem.isEmpty()) {
            items.add(currentItem);
        }

        // Build XML
        simple.forEach((k, v) -> {
            xml.append("<").append(k).append(">").append(escapeXml(v)).append("</").append(k).append(">");
        });

        if (!items.isEmpty()) {
            xml.append("<items>");
            for (Map<String, String> it : items) {
                xml.append("<item>");
                for (Map.Entry<String, String> e : it.entrySet()) {
                    xml.append("<").append(e.getKey()).append(">").append(escapeXml(e.getValue())).append("</").append(
                            e.getKey()).append(">");
                }
                xml.append("</item>");
            }
            xml.append("</items>");
        }

        xml.append("</").append(rootName).append(">\n");
        return xml.toString();
    }

    private String[] splitKeyValue(String line) {
        int colon = line.indexOf(':');
        if (colon < 0) {
            return null;
        }
        String key = line.substring(0, colon).trim();
        String val = line.substring(colon + 1).trim();
        key = stripQuotes(key);
        val = stripQuotes(val);
        return new String[] { key, val };
    }

    private String stripQuotes(String s) {
        if (s.startsWith("\"") && s.endsWith("\"")) {
            return s.substring(1, s.length() - 1);
        }
        return s;
    }

    private String escapeXml(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'",
                "&apos;");
    }
}
