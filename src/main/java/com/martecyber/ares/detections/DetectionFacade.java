package com.martecyber.ares.detections;

/** Lets a plugin ingest a detection straight from a live API response — one at a time, unlike the
 *  batch {@code ImportParser}/{@code IngestFacade} flow — without needing {@code DetectionService}
 *  (ares-core-internal) on its classpath. Implemented in ares-core by a thin adapter. */
public interface DetectionFacade {

    /** Idempotently ingests a detection from an external source (e.g. a Caido finding). Dedup key
     *  is {@code (projectId, sourceType, externalId)} — a repeat call just bumps the existing
     *  detection's occurrence count/last-seen instead of creating a duplicate. */
    ExternalIngestResult ingestExternal(Long projectId, String sourceType, String externalId,
                                        Long assetId, String severity, String title,
                                        String description, String rawData);

    /** Attaches a captured HTTP request/response pair to an existing detection. */
    void attachHttpSample(Long detectionId, String label, String requestContent, String responseContent, String notes);
}
