package com.martecyber.ares.workflows.integrations;

/** One action an {@link IntegrationActionHandler}'s type supports — {@code code} is what's
 *  stored in a node's config and what {@link IntegrationActionRegistry} validates against;
 *  {@code label} is what the node editor's action dropdown shows. */
public record IntegrationActionDescriptor(String code, String label) {}
