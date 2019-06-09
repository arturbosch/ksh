package io.gitlab.arturbosch.ksh.aliases

import io.gitlab.arturbosch.ksh.api.provider.AdditionalHelpProvider
import io.gitlab.arturbosch.kutils.Injektor

class AliasesHelpProvider : AdditionalHelpProvider {

    override fun provide(container: Injektor): String {
        val aliases = aliases(container)
        return if (aliases.isEmpty()) {
            ""
        } else {
            "Available aliases:\n\n${aliases.entries.joinToString("\n", transform = this::renderAliases)}\n"
        }
    }

    private fun renderAliases(entry: Map.Entry<String, String>): String =
        "    * ${entry.key} -> ${entry.value}"
}
