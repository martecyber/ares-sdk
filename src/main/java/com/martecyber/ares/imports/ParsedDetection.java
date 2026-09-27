package com.martecyber.ares.imports;

/** Lightweight representation of a detection extracted by a parser. */
public class ParsedDetection {

    private final String title;
    private final String severity;   // critical, high, medium, low, info
    private final String description;
    private String assetIdentifier; // matches ParsedAsset.identifier; null = project-level
    private final String sourceTemplateId;
    private String rawData;    // JSON string for raw_data column
    /** Source tool state: "open", "reopened", "fixed" — null means active/unknown. */
    private final String state;
    /** Identifier of the asset this detection "affects" (DetectionAffectedAsset), when it
     *  differs from assetIdentifier — e.g. a Burp issue is detected_at a specific WEB_ENDPOINT
     *  but affects the parent WEB_APPLICATION. Null (the default for every other parser) means
     *  "same as assetIdentifier", preserving today's behavior. */
    private String affectsIdentifier;
    /** Raw HTTP request/response text captured by the source tool for this finding (e.g. Burp's
     *  per-issue requestresponse block), stored as a DetectionHttpSample once the Detection row
     *  exists. Null when the tool doesn't capture this. */
    private String requestContent;
    private String responseContent;
    /** CVSS score/vector/version ("CVSS 3.1"/"CVSS 2.0", must match finding_score_type.title)
     *  captured from the source tool's own plugin/advisory data, when available. Null (the
     *  default for every parser except Tenable today) means "no score to persist". */
    private java.math.BigDecimal cvssScore;
    private String cvssVector;
    private String cvssVersion;

    public ParsedDetection(String title, String severity, String description,
                           String assetIdentifier, String sourceTemplateId, String rawData) {
        this(title, severity, description, assetIdentifier, sourceTemplateId, rawData, null);
    }

    public ParsedDetection(String title, String severity, String description,
                           String assetIdentifier, String sourceTemplateId, String rawData,
                           String state) {
        this.title = title;
        this.severity = normalizeSeverity(severity);
        this.description = description;
        this.assetIdentifier = assetIdentifier;
        this.sourceTemplateId = sourceTemplateId;
        this.rawData = rawData;
        this.state = state;
    }

    public String getTitle() { return title; }
    public String getSeverity() { return severity; }
    public String getDescription() { return description; }
    public String getAssetIdentifier() { return assetIdentifier; }
    public void setAssetIdentifier(String assetIdentifier) { this.assetIdentifier = assetIdentifier; }
    public String getSourceTemplateId() { return sourceTemplateId; }
    public String getRawData() { return rawData; }
    public void setRawData(String rawData) { this.rawData = rawData; }
    public String getState() { return state; }
    public boolean isFixed() { return "fixed".equalsIgnoreCase(state); }
    public boolean isActive() { return !isFixed(); }
    public String getAffectsIdentifier() { return affectsIdentifier; }
    public void setAffectsIdentifier(String affectsIdentifier) { this.affectsIdentifier = affectsIdentifier; }
    public String getRequestContent() { return requestContent; }
    public void setRequestContent(String requestContent) { this.requestContent = requestContent; }
    public String getResponseContent() { return responseContent; }
    public void setResponseContent(String responseContent) { this.responseContent = responseContent; }
    public java.math.BigDecimal getCvssScore() { return cvssScore; }
    public void setCvssScore(java.math.BigDecimal cvssScore) { this.cvssScore = cvssScore; }
    public String getCvssVector() { return cvssVector; }
    public void setCvssVector(String cvssVector) { this.cvssVector = cvssVector; }
    public String getCvssVersion() { return cvssVersion; }
    public void setCvssVersion(String cvssVersion) { this.cvssVersion = cvssVersion; }

    private static String normalizeSeverity(String s) {
        if (s == null) return "info";
        return switch (s.toLowerCase()) {
            case "critical" -> "critical";
            case "high" -> "high";
            case "medium", "moderate" -> "medium";
            case "low" -> "low";
            default -> "info";
        };
    }
}
