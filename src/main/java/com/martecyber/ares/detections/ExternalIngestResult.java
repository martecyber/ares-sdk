package com.martecyber.ares.detections;

/** Outcome of {@link DetectionFacade#ingestExternal} — the detection's id plus whether it was
 *  newly created (vs. an existing one just bumped). */
public record ExternalIngestResult(Long detectionId, boolean created) {
}
