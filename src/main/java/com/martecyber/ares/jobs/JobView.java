package com.martecyber.ares.jobs;

/** Read model for a tracked async job — returned by {@link JobFacade}, never the underlying JPA
 *  entity. */
public record JobView(Long id, String status, String result, String error, Integer progress) {
}
