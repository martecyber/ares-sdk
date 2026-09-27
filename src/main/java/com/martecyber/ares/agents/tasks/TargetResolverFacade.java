package com.martecyber.ares.agents.tasks;

/** Resolves a workflow target selector (e.g. {@code {"type": "project_assets", ...}} or {@code
 *  {"type": "scope_entries", ...}}) into concrete IP/domain/hostname strings — without needing
 *  {@code TargetResolver} (ares-core-internal, AQL/JPA-backed) on your classpath at all.
 *  Implemented in ares-core by a thin adapter over the real {@code TargetResolver}. */
public interface TargetResolverFacade {

    /** {@code selector} is the same untyped shape (a {@code Map<String, Object>}, typically with
     *  a {@code "type"} key of {@code "project_assets"} or {@code "scope_entries"} plus filter
     *  keys like {@code "assetTypes"}/{@code "kinds"}) a workflow node's own config already uses
     *  — see any existing {@code IntegrationActionHandler} that resolves targets for the exact
     *  shape to pass. No limit is applied. */
    java.util.List<String> resolveTargets(Long projectId, Object selector);
}
