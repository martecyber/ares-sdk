package com.martecyber.ares.imports;

/** Represents a typed link between two assets to be created during import. */
public class ParsedAssetLink {
    private final String fromIdentifier;
    private final String toIdentifier;
    private final String linkType;

    public ParsedAssetLink(String fromIdentifier, String toIdentifier, String linkType) {
        this.fromIdentifier = fromIdentifier;
        this.toIdentifier = toIdentifier;
        this.linkType = linkType;
    }

    public String getFromIdentifier() { return fromIdentifier; }
    public String getToIdentifier()   { return toIdentifier; }
    public String getLinkType()       { return linkType; }
}
