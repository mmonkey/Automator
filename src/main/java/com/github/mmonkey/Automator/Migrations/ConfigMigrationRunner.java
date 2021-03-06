package com.github.mmonkey.Automator.Migrations;

import com.github.mmonkey.Automator.Automator;

public class ConfigMigrationRunner extends MigrationRunner {

    protected MigrationInterface getMigration(int version) {

        switch (version) {
            default:
                return null;
        }
    }

    public ConfigMigrationRunner(Automator plugin, int version) {
        super(plugin, version);
        this.latest = Automator.CONFIG_VERSION;
    }

}
