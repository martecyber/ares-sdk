package com.martecyber.ares.plugins;

/**
 * Marker for an internal bean a plugin wires together via ordinary Spring constructor injection
 * (a {@code @Service}/{@code @Repository}-style helper) but that isn't itself one of the other
 * known extension points ({@link PluginRestController}, an {@code IntegrationActionHandler}, …).
 * Without this, such a class is never instantiated at all — {@link PluginLoader} only ever calls
 * {@code createBean} for classes it discovers via a known {@code META-INF/services} convention, so
 * a plain helper class referenced only from another plugin bean's constructor would fail
 * autowiring with a {@code NoSuchBeanDefinitionException} (the shared {@code BeanFactory} has no
 * definition for it — {@code createBean} does not recursively instantiate unregistered types).
 *
 * <p>Listed in {@code META-INF/services/com.martecyber.ares.plugins.PluginComponent}, one FQCN per
 * line, <b>in dependency order</b> — {@link PluginLoader} instantiates and registers them in the
 * order listed, so a bean must appear before anything that constructor-injects it. This is a
 * lighter-weight substitute for a full component scan (which the plugin's own isolated classloader
 * and this app's single shared {@code BeanFactory} make impractical to run per plugin); a plugin
 * with only one or two internal helpers can usually just list them directly, no different from
 * writing the file by hand for {@code IntegrationActionHandler}.
 */
public interface PluginComponent {
}
