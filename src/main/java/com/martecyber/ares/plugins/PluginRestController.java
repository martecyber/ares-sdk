package com.martecyber.ares.plugins;

/**
 * Marker a plugin's own {@code @RestController} class implements so {@link PluginLoader} knows to
 * discover it (via {@code META-INF/services/com.martecyber.ares.plugins.PluginRestController})
 * and dynamically register its {@code @RequestMapping}-family-annotated methods with the app's
 * {@link org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping} —
 * Spring MVC otherwise only ever builds its routing table once, at startup, from classpath-scanned
 * {@code @Controller} beans, which a plugin loaded after boot is never part of.
 *
 * <p>Purely a discovery marker — carries no methods of its own. The class still needs the usual
 * {@code @RestController}/{@code @RequestMapping} annotations for {@link PluginLoader} to read;
 * this interface only tells the loader THAT it should look.
 */
public interface PluginRestController {
}
