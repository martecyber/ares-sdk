package com.martecyber.ares.integrations;

import java.util.List;
import java.util.Map;

/** Read/write access to Ares's configured integration instances, for a plugin's own {@code
 *  IntegrationActionHandler}/sync job to resolve config and credentials against — without
 *  needing {@code IntegrationRepository}/{@code Integration}/{@code IntegrationGrant} (all
 *  ares-core-internal JPA types) on its classpath at all. Implemented in ares-core by a thin
 *  adapter over the real {@code IntegrationService}. */
public interface IntegrationFacade {

    /** Org-wide integration instances of {@code type} (org-scoped, "PLATFORM" instances) — {@code
     *  status}/paging may be left at sensible defaults ({@code null}, {@code 0}, a generous page
     *  size) by a caller that just wants "all of them". */
    List<IntegrationView> list(Long organizationId, String type, String status, int page, int size);

    /** Every instance granted to {@code projectId}, regardless of org-vs-project scope. */
    List<IntegrationView> listForProject(Long projectId, Long organizationId);

    IntegrationView get(Long integrationId);

    /** Decrypts and returns this integration's stored credentials — never logged, never
     *  round-tripped back through {@link #list}/{@link #get}. */
    Map<String, String> loadCredentials(Long integrationId);

    /** Records the outcome of a sync attempt: sets {@code lastSyncAt} to now and {@code
     *  connectionStatus} to the given value. Replaces the direct read-mutate-save on the raw
     *  {@code Integration} entity a plugin would otherwise need {@code IntegrationRepository} for. */
    void recordSyncResult(Long integrationId, String connectionStatus);

    /** The (single) grant of {@code integrationId} against {@code projectId} — throws if the
     *  integration isn't granted to this project. Use {@link #resolveGrants} instead for an
     *  integration type that can have several grants per project (Greenbone: one per task). */
    GrantView resolveGrant(Long integrationId, Long projectId, Long organizationId);

    /** Every active grant of {@code integrationId} against {@code projectId} — throws if none. */
    List<GrantView> resolveGrants(Long integrationId, Long projectId, Long organizationId);
}
