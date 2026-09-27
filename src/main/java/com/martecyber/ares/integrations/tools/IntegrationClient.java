package com.martecyber.ares.integrations.tools;

import java.util.List;
import java.util.Map;

/** Strategy interface implemented by each tool connector (Tenable, Qualys…). */
public interface IntegrationClient {

    /** The type discriminator this client handles (e.g. "tenable"). */
    String supports();

    default boolean supports(String type) { return supports().equalsIgnoreCase(type); }

    /**
     * Validates connectivity and credentials. Throws on failure so the caller
     * can update connectionStatus accordingly.
     */
    void testConnection(String settingsJson, Map<String, String> credentials) throws Exception;

    /** Optional tool-specific "pick one of these" items for grant creation (e.g. Greenbone GVM
     *  tasks, Tenable MSSP child accounts) — each map has at least {@code id}/{@code name}, plus
     *  whatever further tool-specific keys the concrete client wants (e.g. {@code status}).
     *  Default: unsupported, since most integration types have no such picker. Lets {@code
     *  IntegrationService} stay generic across every {@link IntegrationClient} implementation,
     *  including a plugin-provided one, instead of casting to a concrete class it can't see at
     *  compile time once that client lives outside ares-core. */
    default List<Map<String, String>> listPickerItems(String settingsJson, Map<String, String> credentials) throws Exception {
        throw new UnsupportedOperationException(supports() + " has no picker items");
    }

    /** Optional: after a successful {@link #testConnection}, probe which finer-grained
     *  capabilities these specific credentials can actually reach (e.g. Tenable checks whether
     *  SYNC_ASSETS/SYNC_VULNS are individually available) — the result gets merged into the
     *  integration's own settings. Default: nothing extra to detect, same as every client that
     *  never had this concept. Lets {@code IntegrationService} stay generic across every {@link
     *  IntegrationClient} implementation, including a plugin-provided one, instead of an {@code
     *  instanceof} check against a concrete class it can't see at compile time. */
    default List<String> detectCapabilities(String settingsJson, Map<String, String> credentials) {
        return List.of();
    }
}
