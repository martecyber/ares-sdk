package com.martecyber.ares.integrations;

/** Read model for a project's grant against an integration — returned by {@link
 *  IntegrationFacade#resolveGrant}/{@link IntegrationFacade#resolveGrants}, never the underlying
 *  {@code IntegrationGrant} JPA entity. {@code accountId}/{@code accountName} are Tenable-MSSP-
 *  only fields (the managed child account); {@code taskId}/{@code taskName} are Greenbone/OpenVAS-
 *  only (the GVM task) — null when not applicable to the integration type. */
public record GrantView(Long id, String accountId, String accountName, String taskId, String taskName) {
}
