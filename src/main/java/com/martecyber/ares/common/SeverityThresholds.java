package com.martecyber.ares.common;

import java.math.BigDecimal;

/**
 * The single CVSS-style 0-10 -&gt; severity mapping used across the app (findings,
 * finding templates, scanner-import parsers). Mirrors {@code scoreToSeverity()} in
 * {@code ares-ui/src/utils/cvss.ts} — keep both in sync if either changes.
 *
 * <p>Since V144 (AQL implementation plan), {@link PriorityThresholds} holds the actual
 * breakpoints and this class just derives the severity string from it, kept only so existing
 * callers don't all need to move to a priority int — severity remains the display value,
 * priority is now the stored source of truth.
 */
public final class SeverityThresholds {

    private SeverityThresholds() {}

    public static String fromScore(BigDecimal score) {
        return fromScore(score == null ? 0.0 : score.doubleValue());
    }

    public static String fromScore(double score) {
        return PriorityThresholds.severityForPriority(PriorityThresholds.fromScore(score));
    }
}
