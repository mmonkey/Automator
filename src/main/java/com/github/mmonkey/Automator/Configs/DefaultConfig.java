package com.github.mmonkey.Automator.Configs;

import com.github.mmonkey.Automator.Automator;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

import java.io.File;
import java.io.IOException;

public class DefaultConfig extends Config {

    public static final String COMMANDS = "Commands";
    public static final String VERSION = "version";
    public static final String DATABASE = "Database";
    public static final String WEBSERVER = "webserver";

    public DefaultConfig(File configDir) {
        super(configDir);

        setConfigFile(new File(configDir, Automator.NAME + ".conf"));
    }

    @Override
    public void load() {

        setConfigLoader(HoconConfigurationLoader.builder().setFile(getConfigFile()).build());

        try {

            if (!getConfigFile().isFile()) {
                getConfigFile().createNewFile();
                saveDefaults();
            }

            setConfig(getConfigLoader().load());

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    private void saveDefaults() {

        try {

            setConfig(getConfigLoader().load());

        } catch (IOException e) {

            e.printStackTrace();

        }

        get().getNode(VERSION).setValue(0);
        get().getNode(DATABASE, VERSION).setValue(0);
        get().getNode(DATABASE, WEBSERVER).setValue(false);
        get().getNode(COMMANDS, "refill").setValue(true);
        get().getNode(COMMANDS, "tool").setValue(true);
        get().getNode(COMMANDS, "torch").setValue(true);
        save();

    }

}
