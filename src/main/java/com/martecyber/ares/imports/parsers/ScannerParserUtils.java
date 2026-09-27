package com.martecyber.ares.imports.parsers;

import com.martecyber.ares.assets.AssetLinkType;
import com.martecyber.ares.assets.AssetType;
import com.martecyber.ares.imports.AssetMetadataKeys;
import com.martecyber.ares.imports.ParseResult;
import com.martecyber.ares.imports.ParsedAsset;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/** Shared helpers for network scanner parsers — part of {@code ares-sdk}, so any plugin's own
 *  parser (e.g. ares-plugin-qualys/-greenbone's file-import parsers) can depend on it directly. */
public final class ScannerParserUtils {

    public static final Pattern IP_RE = Pattern.compile("^\\d{1,3}(\\.\\d{1,3}){3}$");

    public static boolean isIp(String s) {
        return s != null && IP_RE.matcher(s.trim()).matches();
    }

    public static boolean isIpv6(String s) {
        if (s == null || !s.contains(":")) return false;
        try {
            return java.net.InetAddress.getByName(s) instanceof java.net.Inet6Address;
        } catch (Exception e) {
            return false;
        }
    }

    public static String firstNonEmptyLine(byte[] content) {
        return new String(content, StandardCharsets.UTF_8)
            .lines().filter(l -> !l.isBlank()).findFirst().orElse("");
    }

    /**
     * Emits: IP, INTERFACE (iface-{ip}), HOST (host-{ip}) and the links between them.
     * Returns the interface identifier.
     */
    public static String emitHostChain(String ip, ParseResult result, Set<String> seen) {
        return emitHostChain(ip, null, result, seen);
    }

    /**
     * Same as the 3-arg overload but carries {@code hostname} (if non-blank) as a hint for
     * the HOST's {@code hostnames} list. The HOST's identifier stays "host-{ip}" — an
     * internal join key only, resolved to the real (possibly pre-existing) host identity by
     * AssetImportHelper, never derived from hostname text directly. This is what makes host
     * identity stable across a hostname change for the same IP (the reported duplicate-host bug).
     */
    public static String emitHostChain(String ip, String hostname, ParseResult result, Set<String> seen) {
        return emitHostChain(ip, hostname, null, null, result, seen);
    }

    /**
     * Same as the 4-arg overload but also carries the source tool's own stable host id
     * (e.g. Greenbone/GVM's {@code asset_id}), when known, so {@code AssetImportHelper}
     * resolves by it first instead of relying only on the IP/interface chain — which
     * previously created a duplicate HOST whenever the active IP drifted between syncs.
     * {@code tool}/{@code externalId} are null for every caller except GreenboneXMLParser today.
     */
    public static String emitHostChain(String ip, String hostname, String tool, String externalId,
                                 ParseResult result, Set<String> seen) {
        String ifaceId = "iface-" + ip;
        String hostId  = "host-" + ip;
        Map<String, Object> hostMeta = new LinkedHashMap<>();
        if (hostname != null && !hostname.isBlank()) {
            hostMeta.put(AssetMetadataKeys.HOSTNAME_HINTS_KEY, List.of(hostname));
        }
        if (tool != null && !tool.isBlank() && externalId != null && !externalId.isBlank()) {
            hostMeta.put(AssetMetadataKeys.EXTERNAL_ID_TOOL_KEY, tool);
            hostMeta.put(AssetMetadataKeys.EXTERNAL_ID_VALUE_KEY, externalId);
        }
        if (seen.add(ip))      result.addAsset(new ParsedAsset(ip,      AssetType.IP,        Map.of()));
        if (seen.add(ifaceId)) result.addAsset(new ParsedAsset(ifaceId, AssetType.INTERFACE,  Map.of()));
        if (seen.add(hostId))  result.addAsset(new ParsedAsset(hostId,  AssetType.HOST,       hostMeta));
        result.addLink(hostId,  ifaceId, AssetLinkType.HOST_INTERFACE);
        result.addLink(ifaceId, ip,      AssetLinkType.INTERFACE_IP);
        return ifaceId;
    }

    /** Reserved metadata key carrying the port-state for visibility tracking. ImportService strips it before persisting. */
    public static final String VISIBILITY_STATE_KEY = AssetMetadataKeys.VISIBILITY_STATE_KEY;

    /**
     * Emits a SERVICE asset (ip:port/proto) and links it to the interface.
     * Returns the service identifier. Defaults visibility state to "OPEN".
     */
    public static String emitService(String ip, int port, String proto, String banner,
                               ParseResult result, Set<String> seen) {
        return emitService(ip, port, proto, banner, "OPEN", result, seen);
    }

    /**
     * Same as the 6-arg overload but lets the parser carry an explicit port state
     * (OPEN / FILTERED / CLOSED) used to populate service_visibility records when
     * the import declared a sourceIp.
     */
    public static String emitService(String ip, int port, String proto, String banner,
                               String state, ParseResult result, Set<String> seen) {
        String p     = proto != null ? proto.toLowerCase() : "tcp";
        String svcId = ip + ":" + port + "/" + p;
        if (seen.add(svcId)) {
            Map<String, Object> meta = new LinkedHashMap<>();
            meta.put("port", port);
            meta.put("protocol", p);
            if (banner != null && !banner.isBlank()) meta.put("service", banner);
            if (state != null && !state.isBlank()) meta.put(VISIBILITY_STATE_KEY, state);
            result.addAsset(new ParsedAsset(svcId, AssetType.SERVICE, meta));
            result.addLink("iface-" + ip, svcId, AssetLinkType.INTERFACE_SERVICE);
        }
        return svcId;
    }

    /** Iterates non-empty JSON object lines from JSONL content. */
    public static void forEachJsonLine(byte[] content, LineConsumer consumer) {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(content), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty() && line.startsWith("{")) {
                    consumer.accept(line);
                }
            }
        } catch (Exception ignored) {}
    }

    /**
     * Buckets a CVSS base score into Ares's severity scale (critical/high/medium/low),
     * with an explicit fallback for score <= 0 — callers whose source data has a large
     * share of purely-informational findings at score 0 (e.g. Greenbone's "Log" threat
     * level) should pass "info" rather than a fallback that would misleadingly show
     * zero-risk findings as medium severity. Delegates to the shared
     * {@link com.martecyber.ares.common.SeverityThresholds}, which uses "info" for
     * score <= 0 — only pass a different {@code zeroFallback} if you actually need one.
     */
    public static String cvssToSeverity(double score, String zeroFallback) {
        if (score <= 0.0) return zeroFallback;
        return com.martecyber.ares.common.SeverityThresholds.fromScore(score);
    }

    /**
     * RFC4180-style CSV tokenizer that treats the whole content as one character stream,
     * so quoted fields containing literal embedded newlines don't break record boundaries
     * (unlike a line-by-line reader). Returns one String[] per record; embedded "" inside
     * a quoted field decodes to a literal quote.
     */
    public static List<String[]> parseCsv(byte[] content) {
        List<String[]> records = new ArrayList<>();
        String text = new String(content, StandardCharsets.UTF_8);
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        boolean recordStarted = false;
        int i = 0;
        int n = text.length();
        while (i < n) {
            char c = text.charAt(i);
            if (inQuotes) {
                if (c == '"') {
                    if (i + 1 < n && text.charAt(i + 1) == '"') { current.append('"'); i += 2; continue; }
                    inQuotes = false;
                } else { current.append(c); }
                i++;
            } else if (c == '"') {
                inQuotes = true; recordStarted = true; i++;
            } else if (c == ',') {
                fields.add(current.toString()); current.setLength(0); recordStarted = true; i++;
            } else if (c == '\r') {
                i++;
            } else if (c == '\n') {
                if (recordStarted) {
                    fields.add(current.toString());
                    records.add(fields.toArray(new String[0]));
                }
                fields.clear(); current.setLength(0); recordStarted = false; i++;
            } else {
                current.append(c); recordStarted = true; i++;
            }
        }
        if (recordStarted) {
            fields.add(current.toString());
            records.add(fields.toArray(new String[0]));
        }
        return records;
    }

    /** Returns scheme://host or scheme://host:port (strips path). */
    public static String baseUrl(String url) {
        if (url == null || url.isBlank()) return null;
        try {
            URI uri = URI.create(url.trim());
            int port = uri.getPort();
            String host = uri.getHost();
            if (host == null) return url.trim();
            return port == -1
                ? uri.getScheme() + "://" + host
                : uri.getScheme() + "://" + host + ":" + port;
        } catch (Exception e) {
            return url.trim();
        }
    }

    @FunctionalInterface
    public interface LineConsumer {
        void accept(String line) throws Exception;
    }

    private ScannerParserUtils() {}
}
