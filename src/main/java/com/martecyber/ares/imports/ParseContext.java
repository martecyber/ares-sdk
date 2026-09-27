package com.martecyber.ares.imports;

import java.net.URI;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Parser hints derived from project scope entries.
 * Currently used by KatanaParser to decide whether to keep or drop crawled endpoints.
 */
public record ParseContext(Set<String> allowedDomains, boolean filterDisabled) {

    /** No context — parser applies its default filtering behaviour. */
    public static final ParseContext NONE = new ParseContext(Set.of(), false);

    /** One project scope entry's own shape, minus everything ares-core's own {@code
     *  ProjectScopeEntry} JPA entity carries beyond what {@link #forScope} actually reads — kept
     *  here (not that entity) so this module has zero dependency on ares-core's persistence
     *  layer. Its one caller (ares-core's own {@code ImportService}) maps from the real entity. */
    public record ScopeEntryView(String kind, String value, boolean inScope) {}

    /**
     * Builds a context from a project's in-scope entries, extracting hostnames from
     * {@code domain}, {@code domain_wildcard}, {@code url}, and {@code url_wildcard} kinds.
     * When {@code filterDisabled} is true the entry list is ignored.
     */
    public static ParseContext forScope(Collection<ScopeEntryView> entries, boolean filterDisabled) {
        if (filterDisabled) return new ParseContext(Set.of(), true);
        Set<String> domains = new HashSet<>();
        for (ScopeEntryView e : entries) {
            if (!e.inScope()) continue;
            switch (e.kind()) {
                case "domain" -> domains.add(e.value().toLowerCase());
                case "domain_wildcard" -> {
                    String v = e.value();
                    domains.add(v.startsWith("*.") ? v.substring(2).toLowerCase() : v.toLowerCase());
                }
                case "url", "url_wildcard" -> {
                    try {
                        String host = URI.create(e.value()).getHost();
                        if (host != null && !host.isBlank()) domains.add(host.toLowerCase());
                    } catch (Exception ignored) {}
                }
            }
        }
        return new ParseContext(Set.copyOf(domains), false);
    }
}
