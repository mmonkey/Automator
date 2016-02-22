package com.github.mmonkey.Automator.Migrations;

import com.github.mmonkey.Automator.Automator;

public abstract class MigrationRunner {

    protected Automator plugin;
    protected int version;
    protected int latest = 0;

    public void run() {

        while (this.version != this.latest) {
            MigrationInterface migration = this.getMigration(this.version);

            if (migration != null) {
                migration.up();
                this.version++;
            }
        }

    }

    abstract MigrationInterface getMigration(int version);

    public MigrationRunner(Automator plugin, int version) {
        this.plugin = plugin;
        this.version = version;
    }

}
