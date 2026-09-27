package com.martecyber.ares.common;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Ares's canonical P0-P4 priority scale (AQL implementation plan, V144) — 0=P0 (most urgent)
 * .. 4=P4 (least urgent). This is now the source of truth thresholds live at: {@link
 * SeverityThresholds#fromScore} delegates here rather than the other way around, and every write
 * path that used to set only a severity string now derives priority first and severity from it.
 * Breakpoints are unchanged from the pre-existing SeverityThresholds/PriorityLabels — only which
 * class is canonical changed, not the values.
 */
public final class PriorityThresholds {

    private PriorityThresholds() {}

    private static final Map<String, Short> PRIORITY_BY_SEVERITY_NAME = Map.of(
        "critical", (short) 0,
        "high",     (short) 1,
        "medium",   (short) 2,
        "low",      (short) 3,
        "info",     (short) 4
    );

    public static short fromScore(BigDecimal score) {
        return fromScore(score == null ? 0.0 : score.doubleValue());
    }

    public static short fromScore(double score) {
        if (score >= 9.0) return 0;
        if (score >= 7.0) return 1;
        if (score >= 4.0) return 2;
        if (score > 0.0)  return 3;
        return 4;
    }

    /** Unrecognized or null severity name -> 4 (P4, the safest/least-urgent default for a
     *  NOT NULL priority column) — mirrors severityWeight's existing ELSE-&gt;0(lowest) fallback. */
    public static short fromSeverityName(String severity) {
        if (severity == null) return 4;
        return PRIORITY_BY_SEVERITY_NAME.getOrDefault(severity.toLowerCase(), (short) 4);
    }

    public static String severityForPriority(int priority) {
        return switch (priority) {
            case 0 -> "critical";
            case 1 -> "high";
            case 2 -> "medium";
            case 3 -> "low";
            default -> "info";
        };
    }
}
