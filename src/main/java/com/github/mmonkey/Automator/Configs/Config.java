package com.github.mmonkey.Automator.Configs;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Config {

    public static final String LIST = "list";

    private File configDir;
    private File configFile;
    private ConfigurationLoader<CommentedConfigurationNode> configLoader;
    private CommentedConfigurationNode config;

    public Config(File configDir) {
        this.configDir = configDir;
    }

    public File getConfigDir() {
        return this.configDir;
    }

    public File getConfigFile() {
        return this.configFile;
    }

    public void setConfigFile(File configFile) {
        this.configFile = configFile;
    }

    public ConfigurationLoader<CommentedConfigurationNode> getConfigLoader() {
        return this.configLoader;
    }

    public void setConfigLoader(ConfigurationLoader<CommentedConfigurationNode> configLoader) {
        this.configLoader = configLoader;
    }

    public CommentedConfigurationNode get() {
        return this.config;
    }

    public void setConfig(CommentedConfigurationNode config) {
        this.config = config;
    }

    public void load() {

        setConfigLoader(HoconConfigurationLoader.builder().setFile(getConfigFile()).build());

        try {

            if (!getConfigFile().isFile()) {
                getConfigFile().createNewFile();
            }

            setConfig(getConfigLoader().load());

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    public void save() {

        try {

            this.getConfigLoader().save(this.get());

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    public List<String> getList(CommentedConfigurationNode config) {

        @SuppressWarnings("unchecked")
        List<String> list = (List<String>) config.getNode(LIST).getValue();

        if (list == null) {
            return new ArrayList<String>();
        }

        return list;

    }

}
