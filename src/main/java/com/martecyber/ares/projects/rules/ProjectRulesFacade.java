package com.martecyber.ares.projects.rules;

import java.util.Map;

/** Lets a plugin push platform-sourced testing requirements (e.g. a bug-bounty program's required
 *  user-agent or header) into a project's rules — without needing {@code ProjectRuleService}
 *  (ares-core-internal) on its classpath. Implemented in ares-core by a thin adapter. */
public interface ProjectRulesFacade {

    /** Idempotent full-replace keyed on {@code syncedFrom = platform}: recognized keys today are
     *  {@code "userAgent"} and {@code "requestHeader"} (the latter as {@code "name: value"});
     *  a blank/absent value deletes the previously-synced rule for that key. */
    void syncPlatformTestingRequirements(Long projectId, String platform, Map<String, String> requirements);
}
