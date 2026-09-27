package com.martecyber.ares.projects;

/** A project type a plugin wants to register — passed to {@link ProjectTypeFacade#ensure}.
 *
 * @param code                stable, unique code (e.g. "BH", "BH_H1")
 * @param displayName         human label
 * @param parentCode          another already-installed type's code this one nests under, or
 *                            {@code null} for a top-level type
 * @param requiredPluginId    the owning plugin's own id — only that plugin's install/enable
 *                            should ever re-enable this type (see {@code ProjectTypeController}'s
 *                            own guard)
 * @param continuousNumbering when true, generated project codes for this type skip the year
 *                            component and use a continuous per-org sequence instead
 * @param description         optional
 */
public record ProjectTypeSpec(
    String code,
    String displayName,
    String parentCode,
    String requiredPluginId,
    boolean continuousNumbering,
    String description
) {
}
