package com.martecyber.ares.workflows;

import java.util.Map;
import java.util.Optional;

/** Lets a plugin keep a managed, locked Workflow in sync with its own scheduling state (e.g. "run
 *  my sync every N hours") — without needing {@code ManagedWorkflowService}/{@code Workflow}
 *  (ares-core-internal JPA types) on its classpath at all. Implemented in ares-core by a thin
 *  adapter over the real {@code ManagedWorkflowService}. */
public interface WorkflowFacade {

    /** Creates the managed workflow keyed on {@code (managedBy, scopeKind, scopeId)} if absent,
     *  or replaces its graph in place if it already exists. Returns the workflow's id. */
    Long createOrReplace(String managedBy, String scopeKind, Long scopeId, String name,
                         String description, Map<String, Object> graphDefinition);

    void setEnabled(Long workflowId, boolean enabled);

    Optional<Long> find(String managedBy, String scopeKind, Long scopeId);

    void delete(Long workflowId);
}
