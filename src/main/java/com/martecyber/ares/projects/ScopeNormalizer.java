package com.martecyber.ares.projects;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Normalises raw scope-entry values from bug-bounty programs into one or more
 * canonical (kind, value) pairs that Ares can store and classify correctly.
 *
 * Two main concerns:
 *  1. Multi-domain expansion — "example{.com,.net}" / "example.{com,net}" /
 *     "example.com,net" → one entry per domain.
 *  2. URL-declared-as-domain — kind=domain but value="https://example.com" →
 *     (domain, "example.com") + (url, "https://example.com").
 */
public final class ScopeNormalizer {

    private ScopeNormalizer() {}

    public record NormalizedEntry(String kind, String value) {}

    /** Bracket patterns: {…}, […], (…) — content must not itself contain brackets. */
    private static final Pattern BRACE_RE =
        Pattern.compile("[\\{\\[\\(]([^\\{\\}\\[\\]\\(\\)]+)[\\}\\]\\)]");

    /**
     * Main entry point. Returns the list of (kind, value) pairs that should be
     * stored instead of the raw (kind, rawValue) pair.
     *
     * A raw value may span multiple lines — each non-blank line is treated as its
     * own value (still subject to the brace/comma expansion below), so pasting a
     * newline-separated list creates one entry per line, all sharing the same kind.
     */
    public static List<NormalizedEntry> normalize(String kind, String rawValue) {
        if (rawValue == null || rawValue.isBlank()) return List.of();
        List<NormalizedEntry> result = new ArrayList<>();
        for (String line : rawValue.split("\\r?\\n")) {
            String trimmed = line.trim();
            if (trimmed.isEmpty()) continue;
            List<String> expanded = expandValue(trimmed);
            for (String val : expanded) result.addAll(normalizeOne(kind, val));
        }
        return result;
    }

    /**
     * Expand brace/bracket/comma notation into individual values.
     *
     * <pre>
     *   "example{.com,.net}"        → ["example.com", "example.net"]
     *   "example.{com,net}"         → ["example.com", "example.net"]
     *   "*.{example.com,example.net}" → ["*.example.com", "*.example.net"]
     *   "example.com,net"           → ["example.com", "example.net"]
     *   "sub1.example.com,sub2.example.com" → ["sub1.example.com", "sub2.example.com"]
     * </pre>
     */
    public static List<String> expandValue(String value) {
        if (value == null || value.isBlank()) return List.of();

        // ── brace/bracket expansion ────────────────────────────────────────
        Matcher m = BRACE_RE.matcher(value);
        if (m.find()) {
            String prefix = value.substring(0, m.start());
            String suffix = value.substring(m.end());
            String[] alts = m.group(1).split(",", -1);
            List<String> result = new ArrayList<>();
            for (String alt : alts) {
                String candidate = prefix + alt.trim() + suffix;
                result.addAll(expandValue(candidate)); // recurse for nested braces
            }
            return result;
        }

        // ── trailing comma expansion (no brackets) ─────────────────────────
        // Only applicable when value has a comma and no URL scheme.
        if (value.contains(",") && !value.contains("://")) {
            String[] parts = value.split(",", -1);
            if (parts.length > 1) {
                List<String> result = new ArrayList<>();
                String anchor = parts[0].trim();
                result.add(anchor);
                boolean expanded = false;
                for (int i = 1; i < parts.length; i++) {
                    String part = parts[i].trim();
                    if (part.isEmpty()) continue;
                    if (part.contains(".")) {
                        // Looks like a full domain — add as-is
                        result.add(part);
                    } else {
                        // Bare TLD / extension — prefix with the base of the anchor
                        // "example.com" + "net" → base "example." + "net" = "example.net"
                        String base = domainBase(anchor);
                        String sep  = part.startsWith(".") ? "" : ".";
                        result.add(base.endsWith(".") && sep.equals(".")
                                ? base + part
                                : base + sep + part);
                    }
                    expanded = true;
                }
                if (expanded) return result;
            }
        }

        return List.of(value);
    }

    // ── private helpers ───────────────────────────────────────────────────────

    /**
     * Returns the domain up to and including the dot before the last label.
     * "example.com" → "example.", "sub.example.com" → "sub.example."
     */
    private static String domainBase(String domain) {
        int lastDot = domain.lastIndexOf('.');
        return (lastDot < 0) ? domain + "." : domain.substring(0, lastDot + 1);
    }

    /**
     * Ensures url/url_wildcard values always carry a scheme. When the value looks
     * like a bare domain or path (no "://"), "https://" is prepended.
     * Safe to call on any kind — returns value unchanged for non-URL kinds.
     */
    public static String ensureUrlScheme(String kind, String value) {
        if (value == null) return null;
        if (("url".equals(kind) || "url_wildcard".equals(kind)) && !value.contains("://")) {
            return "https://" + value;
        }
        return value;
    }

    /**
     * Normalise a single (kind, value) pair. Handles the case where a URL is
     * declared under a "domain" or "domain_wildcard" kind — produces both a
     * {@code domain} entry (hostname only) and a {@code url} entry.
     */
    private static List<NormalizedEntry> normalizeOne(String kind, String value) {
        if (("domain".equals(kind) || "domain_wildcard".equals(kind)) && value.contains("://")) {
            try {
                String host = new URI(value).getHost();
                if (host != null && !host.isBlank()) {
                    return List.of(
                        new NormalizedEntry("domain", host.toLowerCase()),
                        new NormalizedEntry("url", value)
                    );
                }
            } catch (Exception ignored) { /* fall through to default */ }
        }
        return List.of(new NormalizedEntry(kind, ensureUrlScheme(kind, value)));
    }
}
