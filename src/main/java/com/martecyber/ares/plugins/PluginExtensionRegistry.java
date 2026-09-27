package com.martecyber.ares.plugins;

/**
 * Lets a plugin define its own extension point for OTHER plugins to implement — not just the two
 * built into core ({@code IntegrationActionHandler}/{@code IntegrationClient}). A "base" plugin
 * (e.g. {@code ares-plugin-bughunting}) declares an interface (e.g. {@code BugHuntingClient}) in
 * its own {@code providesExtensionPoints} manifest field, and ships exactly one bean implementing
 * {@code PluginExtensionRegistry<ThatInterface>} — discovered the same way as every other plugin
 * bean, via {@code META-INF/services/com.martecyber.ares.plugins.PluginExtensionRegistry}.
 *
 * <p>A dependent plugin (one whose manifest lists the base plugin under {@code dependsOn}) then
 * simply implements the base plugin's interface and lists its own implementation under {@code
 * META-INF/services/<base-plugin's-interface-fqcn>} — {@link PluginLoader} resolves the interface
 * name against the already-loaded dependency's declared {@code providesExtensionPoints}, finds
 * this registry, and calls {@link #register}/{@link #unregister} automatically. The base plugin
 * never needs to know which (if any) dependents exist; a dependent never needs to know how the
 * base plugin stores/uses the registered instances.
 *
 * <p>One registry bean maps to exactly one declared extension-point interface — {@link
 * PluginLoader} pairs a plugin's {@code META-INF/services/com.martecyber.ares.plugins.
 * PluginExtensionRegistry} entries with its manifest's {@code providesExtensionPoints} list
 * positionally (same order). A plugin declaring more than one extension point needs one registry
 * class (and one manifest entry) per point, listed in matching order.
 */
public interface PluginExtensionRegistry<T> {

    /** Called once per contributed instance, right after {@link
     *  org.springframework.beans.factory.config.AutowireCapableBeanFactory#createBean} constructs
     *  it — same "fully DI'd, ready to use" guarantee every other plugin-discovered bean gets. */
    void register(T instance);

    /** Called when the contributing (dependent) plugin is disabled or uninstalled — symmetric to
     *  {@link #register}, so this registry never ends up holding a reference to a bean whose
     *  owning plugin's classloader should otherwise be eligible for GC. */
    void unregister(T instance);
}
