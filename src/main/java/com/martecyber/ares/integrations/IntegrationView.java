package com.martecyber.ares.integrations;

/** Read model for a configured integration instance — returned by {@link IntegrationFacade},
 *  never the underlying JPA entity. Credentials are never included; see {@link
 *  IntegrationFacade#loadCredentials}. */
public record IntegrationView(Long id, String type, String name, String settings) {
}
