package com.martecyber.ares.imports;

import java.io.InputStream;

public interface ImportParser {

    /** Short id used in API / DB — a plugin's own choice, e.g. its binary's name. */
    String getToolId();

    /** Format discriminator for multi-format tools (e.g. "json", "html", "csv"). Default: "default". */
    default String getFormatId() { return "default"; }

    /** Human-readable label shown in the UI. */
    String getDisplayName();

    /** File extensions this parser accepts (e.g. [".xml"]). */
    String[] getSupportedExtensions();

    /** Quick content-based validation before full parse. */
    boolean validate(byte[] content);

    /** Parse the file bytes and return structured result. Does NOT persist anything. */
    ParseResult parse(byte[] content) throws Exception;

    /** Parse with optional scope context; by default delegates to {@link #parse(byte[])}. */
    default ParseResult parse(byte[] content, ParseContext context) throws Exception {
        return parse(content);
    }
}
