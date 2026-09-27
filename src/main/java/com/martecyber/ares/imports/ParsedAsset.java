package com.martecyber.ares.imports;

import java.util.HashMap;
import java.util.Map;

/** Lightweight representation of an asset extracted by a parser. */
public class ParsedAsset {

    private final String identifier;
    private final String type;         // ip, domain, service, url
    private final Map<String, Object> metadata;

    public ParsedAsset(String identifier, String type) {
        this.identifier = identifier;
        this.type = type;
        this.metadata = new HashMap<>();
    }

    public ParsedAsset(String identifier, String type, Map<String, Object> metadata) {
        this.identifier = identifier;
        this.type = type;
        this.metadata = metadata != null ? metadata : new HashMap<>();
    }

    public String getIdentifier() { return identifier; }
    public String getType() { return type; }
    public Map<String, Object> getMetadata() { return metadata; }
}
