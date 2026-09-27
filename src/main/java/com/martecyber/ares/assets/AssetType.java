package com.martecyber.ares.assets;

import java.util.Map;
import java.util.Set;

public final class AssetType {

    public static final String HOST            = "host";
    public static final String IP              = "ip";
    public static final String SERVICE         = "service";
    public static final String INTERFACE       = "interface";
    public static final String DOMAIN          = "domain";
    public static final String WEB_APPLICATION = "web_application";
    public static final String WEB_ENDPOINT    = "web_endpoint";
    public static final String TECHNOLOGY      = "technology";
    public static final String SYSTEM          = "system";
    public static final String DIRECTORY       = "directory";
    public static final String NETWORK         = "network";
    public static final String ANDROID_APP     = "android_app";
    public static final String IOS_APP         = "ios_app";
    public static final String WINDOWS_APP     = "windows_app";
    public static final String HARDWARE        = "hardware";
    // A software artifact/project as a unit (identified by package coordinates, e.g. Maven
    // groupId:artifactId or an npm/PyPI package name) — deliberately language-agnostic so it
    // covers Java/JVM today and JavaScript/Python/etc. builds later, not just Maven.
    public static final String SOFTWARE        = "software";
    // DNS / text discovery
    public static final String TEXT_DATA       = "text_data";
    // Human / identity
    public static final String EMAIL           = "email";
    public static final String CREDENTIAL      = "credential";
    public static final String PERSON          = "person";
    public static final String LOCATION        = "location";

    /** All canonical asset types. */
    public static final Set<String> ALL = Set.of(
        HOST, IP, SERVICE, INTERFACE, DOMAIN,
        WEB_APPLICATION, WEB_ENDPOINT, TECHNOLOGY,
        SYSTEM, DIRECTORY, NETWORK,
        ANDROID_APP, IOS_APP, WINDOWS_APP, HARDWARE, SOFTWARE,
        TEXT_DATA, EMAIL, CREDENTIAL, PERSON, LOCATION
    );

    /** Short codes used in the asset code format: {org_slug}-{TYPE_CODE}-N */
    public static final Map<String, String> TYPE_CODES = Map.ofEntries(
        Map.entry(HOST,            "HOST"),
        Map.entry(IP,              "IP"),
        Map.entry(SERVICE,         "SVC"),
        Map.entry(INTERFACE,       "IFACE"),
        Map.entry(DOMAIN,          "DOM"),
        Map.entry(WEB_APPLICATION, "WEBAPP"),
        Map.entry(WEB_ENDPOINT,    "ENDPOINT"),
        Map.entry(TECHNOLOGY,      "TECH"),
        Map.entry(SYSTEM,          "SYS"),
        Map.entry(DIRECTORY,       "DIR"),
        Map.entry(NETWORK,         "NET"),
        Map.entry(ANDROID_APP,     "AAPP"),
        Map.entry(IOS_APP,         "IAPP"),
        Map.entry(WINDOWS_APP,     "WAPP"),
        Map.entry(HARDWARE,        "HW"),
        Map.entry(SOFTWARE,        "SW"),
        Map.entry(TEXT_DATA,       "TXT"),
        Map.entry(EMAIL,           "EMAIL"),
        Map.entry(CREDENTIAL,      "CRED"),
        Map.entry(PERSON,          "PERSON"),
        Map.entry(LOCATION,        "LOC")
    );

    public static String typeCode(String type) {
        return TYPE_CODES.getOrDefault(type, type.toUpperCase().replace("_", "-"));
    }

    private AssetType() {}
}
