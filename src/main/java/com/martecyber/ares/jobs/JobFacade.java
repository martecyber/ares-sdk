package com.martecyber.ares.jobs;

/** Read/write access to Ares's tracked async job system, for a plugin's own long-running sync or
 *  scan handler to report progress against — without needing {@code JobRepository}/{@code Job}
 *  (both ares-core-internal JPA types) on its classpath at all. Implemented in ares-core by a
 *  thin adapter over the real {@code JobService}. */
public interface JobFacade {

    /** True if a job of {@code type} is already active (not yet completed/failed) for {@code
     *  projectId} — used to reject an overlapping sync/scan request before creating a new job. */
    boolean existsActive(Long projectId, String type);

    /** Creates a new tracked job and returns its initial state. */
    JobView create(String type, Long organizationId, Long projectId, String payload);

    /** Updates a job's status/progress/result/error — any parameter may be {@code null} to leave
     *  that field unchanged. */
    JobView update(Long jobId, String status, Integer progress, String result, String error);

    /** Current state of a previously created job. */
    JobView get(Long jobId);

    /** True if the job has been cancelled — a long-running handler should poll this and stop
     *  early rather than running to completion. */
    boolean isCancelled(Long jobId);
}
