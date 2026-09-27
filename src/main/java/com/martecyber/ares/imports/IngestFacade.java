package com.martecyber.ares.imports;

/** Persists a completed {@link ParseResult} — the same effect a file-import plugin gets for free
 *  via {@link ImportParser}, but for a live {@code IntegrationActionHandler}/sync job that builds
 *  its {@code ParseResult} from an API response rather than an uploaded file, and needs to
 *  trigger persistence itself. Implemented in ares-core by a thin adapter over the real {@code
 *  ImportService}. */
public interface IngestFacade {

    IngestResult ingest(Long projectId, Long organizationId, String sourceType, ParseResult parsed);
}
