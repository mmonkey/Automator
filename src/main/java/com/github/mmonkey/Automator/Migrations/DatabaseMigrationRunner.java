package com.github.mmonkey.Automator.Migrations;

import com.github.mmonkey.Automator.Automator;

public class DatabaseMigrationRunner extends MigrationRunner {

    protected MigrationInterface getMigration(int version) {

        switch (version) {
            case 0:
//                return new M01_AddInitialDatabaseTables(this.plugin);

            default:
                return null;
        }

    }

    public DatabaseMigrationRunner(Automator plugin, int version) {
        super(plugin, version);
        this.latest = Automator.DATABASE_VERSION;
    }

}
