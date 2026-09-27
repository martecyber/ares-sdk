package com.martecyber.ares.workflows;

/** {@code Workflow.scopeKind} values, plus the platform sentinel scopeId (0), mirroring
 *  {@code PlatformNotificationService.PLATFORM_SCOPE_ID}. */
public final class WorkflowScope {
    public static final String PLATFORM = "platform";
    public static final String ORGANIZATION = "organization";
    public static final String PROJECT = "project";
    public static final long PLATFORM_SCOPE_ID = 0L;

    private WorkflowScope() {}
}
