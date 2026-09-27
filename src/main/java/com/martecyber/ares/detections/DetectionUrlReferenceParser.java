package com.martecyber.ares.detections;

import java.util.Set;

/**
 * Extracts documentation/reference URLs a scan tool embeds in a distinct, structured field of
 * its own raw output (e.g. nuclei's {@code info.reference[]}, WPScan's {@code
 * references.url[]}) — see {@link DetectionUrlReferenceExtractor}'s own doc for why this is
 * scoped to known field paths rather than regex-scanning the whole raw payload. Optional: a
 * plugin whose tool doesn't embed such a field simply doesn't implement this at all — {@link
 * DetectionUrlReferenceExtractor#extractAndLink} no-ops for any {@code sourceType} with no
 * registered parser, the same way an unsupported {@code ImportParser} format no-ops elsewhere.
 *
 * <p>Discovered the same way as {@code ImportParser} — a plugin lists its implementation(s) in
 * {@code META-INF/services/com.martecyber.ares.detections.DetectionUrlReferenceParser}, and
 * {@code PluginLoader} instantiates and registers them as real Spring beans on install/enable.
 */
public interface DetectionUrlReferenceParser {

    /** Must match the {@code Detection#sourceType} this parser applies to — the same toolId the
     *  plugin's {@code ImportParser}/{@code AgentToolSpec} (if any) already use. */
    String getToolId();

    /** Reads reference URLs out of one detection's raw JSON payload. Never throws — return an
     *  empty set for malformed/unexpected input; {@link DetectionUrlReferenceExtractor} treats
     *  an exception here the same as "nothing found", never fails the import over it. */
    Set<String> extractUrls(String rawJson);
}
