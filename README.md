# ares-sdk

The stable, publishable contract a plugin compiles against: the extension-point interfaces
(`ImportParser`, `IntegrationActionHandler`, `IntegrationClient`, `DetectionUrlReferenceParser`,
`PluginComponent`, `PluginLifecycle`, `PluginRestController`, `PluginExtensionRegistry`) plus
their own supporting DTOs/enums — deliberately not the whole of `ares-core`. Zero dependency on
`ares-core` itself.

Published to GitHub Packages on every push to `main` (intermediate `<version>+<githash>`) and on
every run of the `Release` workflow (real `release`/`rc`/`beta`/`alpha` version).

Extracted from the `ares-asm` monorepo — see that repo for full project history.
