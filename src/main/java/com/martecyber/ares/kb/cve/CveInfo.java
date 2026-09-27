package com.martecyber.ares.kb.cve;

/** Read model for a CVE knowledge-base entry — returned by {@link CveFacade}, never the
 *  underlying {@code CveEntry} JPA entity. */
public record CveInfo(String cveId, String severity, Double cvssScore, String cvssVector, String cvssVersion) {
}
