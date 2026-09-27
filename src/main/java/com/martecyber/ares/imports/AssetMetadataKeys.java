package com.martecyber.ares.imports;

/** Single source of truth for the well-known {@code ParsedAsset}/{@code Asset} metadata keys a
 *  parser can set to influence core's own host-resolution/dedup logic — previously duplicated as
 *  raw string literals across {@code AssetImportHelper}, {@code ScannerParserUtils}, and {@code
 *  ImportService} in ares-core. Those three keep their own {@code public static final} fields for
 *  source compatibility, each assigned from here. */
public final class AssetMetadataKeys {

    private AssetMetadataKeys() {}

    /** On a HOST {@code ParsedAsset}'s metadata: a {@code List<String>} of hostnames to merge into
     *  the resolved host's {@code hostnames} list without driving identity on its own. */
    public static final String HOSTNAME_HINTS_KEY = "_hostnameHints";

    /** On a HOST {@code ParsedAsset}'s metadata, paired with {@link #EXTERNAL_ID_VALUE_KEY}: the
     *  tool name an external id came from, so host identity survives active-IP drift across syncs. */
    public static final String EXTERNAL_ID_TOOL_KEY = "_externalIdTool";

    /** Paired with {@link #EXTERNAL_ID_TOOL_KEY} — the external id's own value. */
    public static final String EXTERNAL_ID_VALUE_KEY = "_externalIdValue";

    /** On a SERVICE {@code ParsedAsset}'s metadata: the port's OPEN/FILTERED/... state, used by
     *  {@code ImportService} for later visibility-tracking. */
    public static final String VISIBILITY_STATE_KEY = "__visibilityState";
}
