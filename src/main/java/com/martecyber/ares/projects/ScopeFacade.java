package com.martecyber.ares.projects;

import com.martecyber.ares.projects.dto.ScopeEntryDto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

/** Read/write access to a project's scope entries, for a plugin that syncs scope from an external
 *  platform (e.g. a bug-bounty program) — without needing {@code ProjectScopeEntryRepository}/
 *  {@code ProjectScopeEntry} (ares-core-internal JPA types) on its classpath at all. Implemented
 *  in ares-core by a thin, genuinely {@code @Transactional} adapter. */
public interface ScopeFacade {

    /** Upserts one scope entry keyed on {@code (projectId, source, externalId)} — creates it if
     *  absent, otherwise updates kind/value/notes/inScope/metadata in place. {@code value} is
     *  normalized (e.g. a bare domain under kind {@code url} gets a scheme prepended) before the
     *  comparison that decides whether Ares's own {@code updatedAt} should move — a platform
     *  re-sync that changes nothing content-wise never bumps it. */
    ScopeEntryDto upsert(Long projectId, String source, String externalId, String kind, String value,
                        String notes, boolean inScope, OffsetDateTime platformCreatedAt,
                        OffsetDateTime platformUpdatedAt, String metadata);

    /** Every scope entry for the project, regardless of source. */
    List<ScopeEntryDto> listAll(Long projectId);

    /** Deletes every entry of {@code source} whose {@code externalId} is no longer in {@code
     *  activeExternalIds} — call after a full re-sync to prune entries the platform removed. */
    void deleteObsolete(Long projectId, String source, Set<String> activeExternalIds);

    /** Deletes every entry NOT of {@code keepSource} — e.g. wiping every platform-imported entry
     *  while leaving manually-added ones untouched, on disconnect. */
    void deleteAllExceptSource(Long projectId, String keepSource);
}
