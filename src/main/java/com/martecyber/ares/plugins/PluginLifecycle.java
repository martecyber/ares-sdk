package com.martecyber.ares.plugins;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Optional install/uninstall hook for a plugin that owns its own database schema and/or
 * reference-data rows — plugins still can't bring their own Flyway migrations (Flyway's own
 * migration history is core-owned and versioned), so a plugin that needs a table manages it
 * itself via plain {@link JdbcTemplate} SQL, never through JPA/Hibernate (Hibernate builds its
 * entire entity map once at boot, before any plugin ever loads — a plugin-defined {@code @Entity}
 * would simply be invisible to it).
 *
 * <p>Discovered like any other plugin bean, via {@code
 * META-INF/services/com.martecyber.ares.plugins.PluginLifecycle} — at most one such bean per
 * plugin. {@link #onInstall} runs once, right after {@link PluginLoader#install} finishes wiring
 * everything else, and only on a genuine install (not on every {@link PluginLoader#load} — i.e.
 * not every time the plugin is merely re-enabled after being disabled, so it must be idempotent
 * regardless: {@code CREATE TABLE IF NOT EXISTS ...}, upsert-style seed rows). {@link #onForget}
 * runs right before {@link PluginLoader#forget} drops the plugin's cached classes, symmetric to
 * {@code onInstall} — only on uninstall, never on a mere disable ({@link PluginLoader#unload}),
 * so disabling and re-enabling a plugin never touches its data.
 */
public interface PluginLifecycle {

    default void onInstall(JdbcTemplate jdbc) {}

    default void onForget(JdbcTemplate jdbc) {}
}
