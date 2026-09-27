package com.martecyber.ares.workflows.integrations;

/** One configured instance of an {@link IntegrationActionHandler}'s type that a given workflow
 *  scope can act through — e.g. one configured Caido API connector. */
public record IntegrationInstanceDescriptor(Long id, String label) {}
