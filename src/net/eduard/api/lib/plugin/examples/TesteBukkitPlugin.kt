package net.eduard.api.lib.plugin.examples

import net.eduard.api.lib.plugin.BukkitPlugin


class TesteBukkitPlugin : BukkitPlugin() {
    override val hybridPlugin = PluginExemplo(this)
}