package com.github.mmonkey.Automator.Migrations;

import com.github.mmonkey.Automator.Automator;
import com.github.mmonkey.Automator.Configs.DefaultConfig;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public abstract class Migration implements MigrationInterface {

    protected Automator plugin;

    protected void bumpDatabaseVersion(int version) {
        CommentedConfigurationNode config = plugin.getDefaultConfig().get();
        config.getNode(DefaultConfig.DATABASE, DefaultConfig.VERSION).setValue(version);
        plugin.getDefaultConfig().save();
    }

    public Migration(Automator plugin) {
        this.plugin = plugin;
    }

}
