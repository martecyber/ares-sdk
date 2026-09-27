package com.martecyber.ares.workflows.integrations;

/** {@link IntegrationActionHandler#checkStatus} result — {@code state} is one of the three
 *  constants below; {@code outputJson} is set only when COMPLETED, {@code error} only when
 *  FAILED. Mirrors the RUNNING/COMPLETED/FAILED vocabulary {@code WorkflowStepPoller} already
 *  uses for AgentTask/Job terminal-state checks, so the poller can treat this exactly like those
 *  two existing ref types. */
public record IntegrationActionResult(String state, String outputJson, String error) {
    public static final String RUNNING = "running";
    public static final String COMPLETED = "completed";
    public static final String FAILED = "failed";
}
