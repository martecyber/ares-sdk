package com.martecyber.ares.workflows.integrations;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Extension point for the {@code ACTION_INTEGRATION_CALL} workflow node type: implement one of
 * these per integration type that exposes something a workflow can trigger, register it as a
 * Spring {@code @Component}, and it's automatically picked up everywhere —
 * {@link IntegrationActionRegistry} collects every bean implementing this interface, so adding a
 * new action (or a whole new integration type) needs zero changes to
 * {@code WorkflowGraphValidator}, {@code WorkflowRunService}, {@code WorkflowStepPoller}, the
 * integration-actions listing endpoint, or the frontend's node editor — they all read straight
 * from whatever's currently registered instead of a hardcoded switch/set.
 */
public interface IntegrationActionHandler {

    /** The type discriminator this handler owns, e.g. {@code "caido-api"} — must be globally
     *  unique across all handlers ({@link IntegrationActionRegistry} rejects a duplicate at
     *  startup). Not necessarily the same string as the generic {@code integrations} table's
     *  {@code type} column — some integration families (Caido) live in their own tables
     *  entirely, outside that generic one. */
    String integrationType();

    /** Human label for the type, e.g. "Caido (API)" — the first pick in the node's integration
     *  selector, before any instance or action is chosen. */
    String integrationTypeLabel();

    /** Which workflow scope kind(s) ({@code "platform"}/{@code "organization"}/{@code "project"})
     *  this handler's work can be triggered from — e.g. Caido tasks are always project-owned
     *  ({@code Set.of("project")}), KB sync is platform-global, Shodan tasks are org-scoped.
     *  {@code WorkflowGraphValidator}/{@code WorkflowRunService} reject a node whose workflow
     *  scope isn't in this set, and the integration-actions listing endpoint only offers this
     *  type at scopes it declares here. */
    Set<String> supportedScopes();

    /** A finer-grained gate beyond {@link #supportedScopes()} — e.g. Bug Hunting sync is
     *  project-scoped like any other project-owned action, but only meaningful for projects whose
     *  own type is actually a bug-hunting one; every other handler's default (always available to
     *  any scope it supports) is correct as-is. Checked both at save time ({@code
     *  WorkflowGraphValidator}) and at run time ({@code WorkflowRunService}), and used to filter
     *  the catalog endpoint the node editor reads from — never rely on the UI hiding this alone. */
    default boolean isAvailableForScope(String scopeKind, Long scopeId) { return true; }

    /** Whether this type should be offered in the Data Sources "new integration" picker as
     *  something a user can create/configure. {@code false} for handlers that only exist to let
     *  a Workflow trigger an existing native feature (KB sync, Bug Hunting sync, the orphaned
     *  {@code shodan-task} connector) — those still show up in the Workflow editor's action
     *  catalog (unfiltered), just not as a creatable Data Source. */
    default boolean isDataSourceIntegration() { return true; }

    /** Every action this handler currently supports. Static — doesn't need scope/instance
     *  context, since which actions exist is a property of the integration TYPE, not any one
     *  configured instance of it. */
    List<IntegrationActionDescriptor> describeActions();

    /** Configured instances of this type usable from the given workflow scope. Handlers whose
     *  integration type isn't scoped at all (e.g. Caido's API connectors are configured
     *  platform-wide, usable from any project) can ignore scopeKind/scopeId entirely. */
    List<IntegrationInstanceDescriptor> listInstances(String scopeKind, Long scopeId);

    /** Starts the action; returns an opaque id {@link #checkStatus} can later resolve — typically
     *  the id of whatever row/job/task the handler creates to track the work. Expected to run
     *  synchronously just long enough to kick the work off, not to wait for it to finish; actual
     *  completion is polled later via checkStatus, mirroring how {@code ACTION_AGENT_TASK}/
     *  {@code ACTION_SYNC} wait on an AgentTask/Job today. {@code scopeKind}/{@code scopeId} are
     *  the calling workflow's own scope (guaranteed to be one of {@link #supportedScopes()} by
     *  the time this is called) — a project-scoped handler reads {@code scopeId} as the project
     *  id, an org-scoped one as the organization id, and so on. */
    Long start(String actionCode, Long integrationInstanceId, String scopeKind, Long scopeId, Map<String, Object> params);

    /** Non-terminal ({@link IntegrationActionResult#RUNNING}) or terminal
     *  ({@link IntegrationActionResult#COMPLETED}/{@link IntegrationActionResult#FAILED}) status
     *  for a ref previously returned by {@link #start}. Polled every ~15s by
     *  {@code WorkflowStepPoller} until terminal. */
    IntegrationActionResult checkStatus(Long refId);
}
