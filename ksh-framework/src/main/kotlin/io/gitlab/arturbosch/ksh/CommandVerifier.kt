package io.gitlab.arturbosch.ksh

import io.gitlab.arturbosch.ksh.api.ShellClass

/**
 * @author Artur Bosch
 */
class CommandVerifier(val commands: List<ShellClass>) {

    init {
        Debugging.log { commands }

        val distinctIds = commands.mapTo(HashSet()) { it.commandId }
        if (commands.size != distinctIds.size) {
            val doubleIds = mutableSetOf<String>()
            for (id in commands.map { it.commandId }) {
                if (id in distinctIds) {
                    distinctIds.remove(id)
                } else {
                    doubleIds.add(id)
                }
            }
            throw IllegalStateException("Must not contain duplicated command ids: '$doubleIds'")
        }
    }
}
