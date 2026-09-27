package com.martecyber.ares.imports;

import java.util.ArrayList;
import java.util.List;

/** Parsed output produced by an ImportParser before persistence. */
public class ParseResult {

    private final List<ParsedAsset> assets = new ArrayList<>();
    private final List<ParsedAssetLink> links = new ArrayList<>();
    private final List<ParsedDetection> detections = new ArrayList<>();
    private final List<String> errors = new ArrayList<>();
    private final List<String> warnings = new ArrayList<>();

    public List<ParsedAsset> getAssets() { return assets; }
    public List<ParsedAssetLink> getLinks() { return links; }
    public List<ParsedDetection> getDetections() { return detections; }
    public List<String> getErrors() { return errors; }
    public List<String> getWarnings() { return warnings; }

    public void addAsset(ParsedAsset a) { assets.add(a); }
    public void addLink(String fromId, String toId, String linkType) {
        links.add(new ParsedAssetLink(fromId, toId, linkType));
    }
    public void addDetection(ParsedDetection d) { detections.add(d); }
    public void addError(String e) { errors.add(e); }
    public void addWarning(String w) { warnings.add(w); }
}
