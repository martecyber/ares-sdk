package com.martecyber.ares.projects;

import java.util.Optional;

/** The narrow slice of project lookups a plugin's own integration handler needs — without
 *  needing {@code ProjectRepository}/{@code Project} (ares-core-internal JPA types) on its
 *  classpath at all. Implemented in ares-core by a thin adapter over the real {@code
 *  ProjectRepository}. */
public interface ProjectFacade {

    /** The organization a project belongs to. */
    Long getOrganizationId(Long projectId);

    boolean exists(Long projectId);

    /** The project's own type's code (e.g. "BH_H1"), or empty if the project has no type or
     *  doesn't exist. */
    Optional<String> getProjectTypeCode(Long projectId);
}
