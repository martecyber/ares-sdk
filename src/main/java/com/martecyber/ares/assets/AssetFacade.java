package com.martecyber.ares.assets;

import com.martecyber.ares.imports.ParsedAsset;

/** Lets a plugin resolve/create assets and their project association one at a time — for a live
 *  integration handler processing results as they arrive (e.g. one Caido finding at a time), as
 *  opposed to a batch {@code ImportParser}/{@code IngestFacade} flow. Without needing {@code
 *  AssetImportHelper}/{@code ProjectAssetAccessRepository}/{@code AssetToolSightingService}
 *  (ares-core-internal) on its classpath. Implemented in ares-core by thin adapters over those. */
public interface AssetFacade {

    /** Resolves {@code asset} to an existing asset's id, or creates it — same identity/merge
     *  rules an {@code ImportParser}'s {@link ParsedAsset} gets via the batch import pipeline. */
    Long resolveOrCreate(Long organizationId, ParsedAsset asset);

    /** Links two already-resolved assets (e.g. a web app to one of its endpoints) if no such link
     *  exists yet — idempotent. */
    void linkIfAbsent(Long fromAssetId, Long toAssetId, String linkType);

    /** Ensures the WEB_APPLICATION at {@code webAppAssetId} has its usual IP/interface/port chain
     *  resolved from {@code webAppUrl}, same as the batch import pipeline does for a discovered
     *  web app. */
    void ensureWebApplicationTree(Long organizationId, Long projectId, Long webAppAssetId, String webAppUrl);

    /** Grants the project visibility into the asset (idempotent) — the same effect adding an
     *  asset to a project's scope during import has. */
    void grantProjectAccess(Long projectId, Long assetId);

    /** Records that {@code tool} observed this asset for this project just now — drives "last
     *  seen by tool" reporting. */
    void recordToolSighting(Long assetId, Long projectId, String tool);
}
