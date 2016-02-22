package com.github.mmonkey.Automator.Migrations;

import com.github.mmonkey.Automator.Automator;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public abstract class Migration implements MigrationInterface {

    protected Automator plugin;

    protected void bumpVersion(String node, int version) {
        CommentedConfigurationNode config = plugin.getDefaultConfig().get();
        config.getNode(node).setValue(version);
        plugin.getDefaultConfig().save();
    }

    public Migration(Automator plugin) {
        this.plugin = plugin;
    }

}
