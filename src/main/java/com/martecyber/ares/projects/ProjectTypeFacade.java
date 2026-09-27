package com.martecyber.ares.projects;

/** Lets a plugin register its own project type(s) — without needing {@code
 *  ProjectTypeRepository}/{@code ProjectType} (ares-core-internal JPA types) on its classpath at
 *  all. Implemented in ares-core by a thin adapter over the real {@code ProjectTypeRepository}.
 *  Call {@link #ensure} from {@code PluginLifecycle#onInstall} (idempotent — safe on every
 *  install/re-enable) and {@link #disable} from {@code PluginLifecycle#onForget}. */
public interface ProjectTypeFacade {

    /** Creates the type if absent, or re-asserts every field (including re-enabling it) if it
     *  already exists. Throws if {@code spec.parentCode()} is non-null and no such type exists
     *  yet — a dependent plugin (e.g. a bughunting platform satellite) must declare the parent
     *  plugin as a {@code dependsOn} so it's guaranteed to already be installed. */
    void ensure(ProjectTypeSpec spec);

    /** Soft-disables the type (never deletes the row, or any project already using it) — a no-op
     *  if the type doesn't exist. */
    void disable(String code);
}
