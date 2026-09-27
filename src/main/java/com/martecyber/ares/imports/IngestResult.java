package com.martecyber.ares.imports;

import java.util.List;

/** Stats from a completed {@link IngestFacade#ingest} call — mirrors ares-core's own {@code
 *  ImportResult}, trimmed to the fields a plugin actually reports back on its job. */
public record IngestResult(int assetsCreated, int detectionsCreated, int detectionsUpdated, List<String> warnings) {
}
