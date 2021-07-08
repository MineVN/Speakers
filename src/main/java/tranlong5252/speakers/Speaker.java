package tranlong5252.speakers;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import tranlong5252.speakers.command.CommandMain;
import tranlong5252.speakers.config.Config;

import java.util.Objects;

public final class Speaker extends JavaPlugin {

    static Speaker plugin;

    public static Speaker get() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        this.reloadConfig();
        registerCommands();
        registerChannels();
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void reloadConfig() {
        this.saveDefaultConfig();
        Config.reload(this);
    }


    private void registerChannels() {
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "fs:minestrike");
    }

    public void registerCommands() {
        CommandMain cmd = new CommandMain();
        Objects.requireNonNull(this.getCommand("loa")).setExecutor(cmd);
        Objects.requireNonNull(this.getCommand("getloa")).setExecutor(cmd);
        Objects.requireNonNull(this.getCommand("loareload")).setExecutor(cmd); //TODO doesn't work, fix later
    }
}
