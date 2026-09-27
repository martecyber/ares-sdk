package com.martecyber.ares.projects.dto;

import java.time.OffsetDateTime;

public record ScopeEntryDto(
    Long id,
    Long projectId,
    String kind,
    String value,
    String notes,
    String metadata,
    boolean inScope,
    /** 'manual' or platform name for imported entries (e.g. 'bugcrowd'). */
    String source,
    /** External ID on the originating platform; null for manual entries. */
    String externalId,
    /** Ares-side: when this record was first created in Ares. */
    OffsetDateTime createdAt,
    /** Ares-side: when this record was last modified in Ares. */
    OffsetDateTime updatedAt,
    /** Platform-side: when the scope target was created on the originating platform. Null for manual entries. */
    OffsetDateTime platformCreatedAt,
    /** Platform-side: when the scope target was last updated on the originating platform. Null for manual entries. */
    OffsetDateTime platformUpdatedAt
) {}
