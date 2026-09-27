package com.martecyber.ares.kb.cve;

import java.util.Collection;
import java.util.List;

/** Bulk CVE lookups against Ares's own CVE knowledge base — without needing {@code CveRepository}/
 *  {@code CveEntry} (ares-core-internal JPA types) on your classpath. Implemented in ares-core by
 *  a thin adapter. Useful when a tool reports a bare CVE id with thin or absent severity/CVSS of
 *  its own, and you want to enrich it against Ares's own synced feed instead. */
public interface CveFacade {

    /** Resolves every {@code cveId} that exists in the knowledge base — missing ids are simply
     *  absent from the result, not an error. */
    List<CveInfo> findByCveIds(Collection<String> cveIds);
}
