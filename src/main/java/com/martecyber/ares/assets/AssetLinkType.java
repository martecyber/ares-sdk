package com.martecyber.ares.assets;

import java.util.Map;
import java.util.Set;

/**
 * Valid typed links between assets.
 *
 * Matrix (fromType → linkType → allowed toTypes):
 *
 *   host            host_interface      → interface
 *   host            host_technology     → technology
 *   interface       interface_ip            → ip
 *   interface       interface_service       → service
 *   interface       interface_access_point  → interface  (station/client → AP's BSSID interface)
 *   domain          domain_cname        → domain
 *   domain          domain_a            → ip
 *   domain          domain_aaaa         → ip
 *   domain          domain_mx           → domain
 *   domain          domain_ns           → domain
 *   domain          domain_txt          → text_data
 *   domain          domain_ptr          → domain
 *   domain          domain_soa          → domain
 *   domain          domain_caa          → domain
 *   domain          domain_srv          → service
 *   domain          domain_host         → host  (local AD / split-horizon)
 *   domain          domain_subdomain    → domain  (nearest-ancestor hierarchy, auto-computed)
 *   web_application webapp_service      → service
 *   web_application webapp_domain       → domain  (served via this domain)
 *   web_application webapp_endpoint     → web_endpoint  (root endpoint)
 *   web_application webapp_technology   → technology
 *   web_endpoint    endpoint_child      → web_endpoint  (recursive path tree)
 *   web_endpoint    endpoint_technology → technology
 *   system          system_host         → host
 *   directory       directory_domain    → domain
 *   directory       directory_host      → host  (domain controllers)
 *   network         network_subnet      → network
 *   network         network_ip          → ip
 *   android_app     app_technology      → technology
 *   ios_app         app_technology      → technology
 *   windows_app     app_technology      → technology
 *   hardware        hardware_host       → host
 *   software        software_technology → technology  (dependency)
 */
public final class AssetLinkType {

    public static final String HOST_INTERFACE      = "host_interface";
    public static final String HOST_TECHNOLOGY     = "host_technology";
    public static final String INTERFACE_IP        = "interface_ip";
    public static final String INTERFACE_SERVICE   = "interface_service";
    public static final String DOMAIN_CNAME        = "domain_cname";
    public static final String DOMAIN_A            = "domain_a";
    public static final String DOMAIN_AAAA         = "domain_aaaa";
    public static final String DOMAIN_MX           = "domain_mx";
    public static final String DOMAIN_NS           = "domain_ns";
    public static final String DOMAIN_TXT          = "domain_txt";
    public static final String DOMAIN_PTR          = "domain_ptr";
    public static final String DOMAIN_SOA          = "domain_soa";
    public static final String DOMAIN_CAA          = "domain_caa";
    public static final String DOMAIN_SRV          = "domain_srv";
    public static final String DOMAIN_HOST         = "domain_host";
    public static final String DOMAIN_SUBDOMAIN    = "domain_subdomain";
    public static final String WEBAPP_SERVICE      = "webapp_service";
    public static final String WEBAPP_DOMAIN       = "webapp_domain";
    public static final String WEBAPP_ENDPOINT     = "webapp_endpoint";
    public static final String WEBAPP_TECHNOLOGY   = "webapp_technology";
    public static final String ENDPOINT_CHILD      = "endpoint_child";
    public static final String ENDPOINT_TECHNOLOGY = "endpoint_technology";
    public static final String SYSTEM_HOST         = "system_host";
    public static final String DIRECTORY_DOMAIN    = "directory_domain";
    public static final String DIRECTORY_HOST      = "directory_host";
    public static final String NETWORK_SUBNET      = "network_subnet";
    public static final String NETWORK_IP          = "network_ip";
    public static final String APP_TECHNOLOGY      = "app_technology";
    public static final String HARDWARE_HOST       = "hardware_host";
    public static final String INTERFACE_ACCESS_POINT = "interface_access_point";
    public static final String SOFTWARE_TECHNOLOGY = "software_technology";

    /** from-type → link-type → allowed to-types */
    private static final Map<String, Map<String, Set<String>>> MATRIX = Map.ofEntries(
        Map.entry(AssetType.HOST, Map.of(
            HOST_INTERFACE,  Set.of(AssetType.INTERFACE),
            HOST_TECHNOLOGY, Set.of(AssetType.TECHNOLOGY)
        )),
        Map.entry(AssetType.INTERFACE, Map.of(
            INTERFACE_IP,           Set.of(AssetType.IP),
            INTERFACE_SERVICE,      Set.of(AssetType.SERVICE),
            INTERFACE_ACCESS_POINT, Set.of(AssetType.INTERFACE)
        )),
        Map.entry(AssetType.DOMAIN, Map.ofEntries(
            Map.entry(DOMAIN_CNAME, Set.of(AssetType.DOMAIN)),
            Map.entry(DOMAIN_A,     Set.of(AssetType.IP)),
            Map.entry(DOMAIN_AAAA,  Set.of(AssetType.IP)),
            Map.entry(DOMAIN_MX,    Set.of(AssetType.DOMAIN)),
            Map.entry(DOMAIN_NS,    Set.of(AssetType.DOMAIN)),
            Map.entry(DOMAIN_TXT,   Set.of(AssetType.TEXT_DATA)),
            Map.entry(DOMAIN_PTR,   Set.of(AssetType.DOMAIN)),
            Map.entry(DOMAIN_SOA,   Set.of(AssetType.DOMAIN)),
            Map.entry(DOMAIN_CAA,   Set.of(AssetType.DOMAIN)),
            Map.entry(DOMAIN_SRV,   Set.of(AssetType.SERVICE)),
            Map.entry(DOMAIN_HOST,  Set.of(AssetType.HOST)),
            Map.entry(DOMAIN_SUBDOMAIN, Set.of(AssetType.DOMAIN))
        )),
        Map.entry(AssetType.WEB_APPLICATION, Map.of(
            WEBAPP_SERVICE,    Set.of(AssetType.SERVICE),
            WEBAPP_DOMAIN,     Set.of(AssetType.DOMAIN),
            WEBAPP_ENDPOINT,   Set.of(AssetType.WEB_ENDPOINT),
            WEBAPP_TECHNOLOGY, Set.of(AssetType.TECHNOLOGY)
        )),
        Map.entry(AssetType.WEB_ENDPOINT, Map.of(
            ENDPOINT_CHILD,      Set.of(AssetType.WEB_ENDPOINT),
            ENDPOINT_TECHNOLOGY, Set.of(AssetType.TECHNOLOGY)
        )),
        Map.entry(AssetType.SYSTEM, Map.of(
            SYSTEM_HOST, Set.of(AssetType.HOST)
        )),
        Map.entry(AssetType.DIRECTORY, Map.of(
            DIRECTORY_DOMAIN, Set.of(AssetType.DOMAIN),
            DIRECTORY_HOST,   Set.of(AssetType.HOST)
        )),
        Map.entry(AssetType.NETWORK, Map.of(
            NETWORK_SUBNET, Set.of(AssetType.NETWORK),
            NETWORK_IP,     Set.of(AssetType.IP)
        )),
        Map.entry(AssetType.ANDROID_APP, Map.of(
            APP_TECHNOLOGY, Set.of(AssetType.TECHNOLOGY)
        )),
        Map.entry(AssetType.IOS_APP, Map.of(
            APP_TECHNOLOGY, Set.of(AssetType.TECHNOLOGY)
        )),
        Map.entry(AssetType.WINDOWS_APP, Map.of(
            APP_TECHNOLOGY, Set.of(AssetType.TECHNOLOGY)
        )),
        Map.entry(AssetType.HARDWARE, Map.of(
            HARDWARE_HOST, Set.of(AssetType.HOST)
        )),
        Map.entry(AssetType.SOFTWARE, Map.of(
            SOFTWARE_TECHNOLOGY, Set.of(AssetType.TECHNOLOGY)
        ))
    );

    /**
     * Best-effort resolution of the (fromType, toType) pair a linkType implies — used by
     * import pipelines to disambiguate two candidate assets sharing an identifier string
     * (e.g. a HOST and a DOMAIN both named "foo.example.com") when resolving a link's
     * endpoints. Returns null if the linkType isn't recognized.
     *
     * Every linkType in the matrix belongs to exactly one fromType today except
     * {@link #APP_TECHNOLOGY} (shared by android_app/ios_app/windows_app, all → technology);
     * that one ambiguity is harmless here since no parser can produce an identifier
     * collision across app-store platforms.
     */
    public static String[] expectedTypes(String linkType) {
        for (Map.Entry<String, Map<String, Set<String>>> e : MATRIX.entrySet()) {
            Set<String> toTypes = e.getValue().get(linkType);
            if (toTypes != null) return new String[]{ e.getKey(), toTypes.iterator().next() };
        }
        return null;
    }

    /**
     * Validates that the link is allowed. Throws {@link IllegalArgumentException} if not.
     */
    public static void validate(String fromType, String linkType, String toType) {
        Map<String, Set<String>> byLinkType = MATRIX.get(fromType);
        if (byLinkType == null) {
            throw new IllegalArgumentException(
                "Asset type '" + fromType + "' does not support any outgoing links");
        }
        Set<String> allowed = byLinkType.get(linkType);
        if (allowed == null) {
            throw new IllegalArgumentException(
                "Link type '" + linkType + "' is not valid from asset type '" + fromType + "'");
        }
        if (!allowed.contains(toType)) {
            throw new IllegalArgumentException(
                "Link type '" + linkType + "' from '" + fromType +
                "' cannot point to asset type '" + toType + "'");
        }
    }

    private AssetLinkType() {}
}
