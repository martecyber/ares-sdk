package com.martecyber.ares.common;

/** Platform-wide settings a plugin might need — without needing {@code PlatformSettingsService}
 *  (ares-core-internal) on its classpath. */
public interface PlatformFacade {

    /** The IANA timezone id the platform is configured to interpret wall-clock schedules in
     *  (e.g. for computing a cron expression's next run). */
    String getTimezone();
}
