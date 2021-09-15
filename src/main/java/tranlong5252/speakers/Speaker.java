package tranlong5252.speakers;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import tranlong5252.speakers.command.CommandMain;
import tranlong5252.speakers.config.Config;

import java.io.File;
import java.util.Objects;

public final class Speaker extends JavaPlugin {

    static Speaker plugin;

    public static Speaker get() {
        return plugin;
    }

    public File getFile(String name) {
        return new File(this.getDataFolder(), name + ".yml");
    }

    @Override
    public void onEnable() {
        plugin = this;
        Config.reload(plugin);
        registerCommands();
        registerChannels();
    }

    @Override
    public void onDisable() {
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
