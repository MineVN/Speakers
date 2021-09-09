package tranlong5252.speakers.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import tranlong5252.speakers.utils.ItemStackUtils;

import java.io.File;

public class Config {

    private static ItemStack GLOBAL_SPEAKER;
    private static String SERVER_NAME;
    private static String COLOR;
    private static long cooldownTime;

    public static void reload(JavaPlugin plugin) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "config.yml"));
        ConfigurationSection item = config.getConfigurationSection("global-speaker");
        if (item!=null) GLOBAL_SPEAKER = ItemStackUtils.buildItem(item);
        SERVER_NAME = config.getString("server-name");
        COLOR = config.getString("color");
        cooldownTime = config.getLong("cooldown-time",5);
    }

    public static ItemStack getGlobalSpeaker() {
        return GLOBAL_SPEAKER.clone();
    }

    public static boolean isGlobalSpeaker(ItemStack is) {
        return is.isSimilar(getGlobalSpeaker());
    }

    public static String getServerName() {
        return SERVER_NAME;
    }

    public static String getColor() {
        return COLOR.toLowerCase();
    }

    public static long getCooldownTime() {
        return cooldownTime;
    }
}
