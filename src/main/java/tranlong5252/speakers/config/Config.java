package tranlong5252.speakers.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import tranlong5252.speakers.Speaker;
import tranlong5252.speakers.utils.ItemStackUtils;

import java.io.File;
import java.io.IOException;

public class Config {

    private static ItemStack GLOBAL_SPEAKER;
    private static String SERVER_NAME;
    private static String COLOR;
    private static long cooldownTime;

    public static void reload(Speaker plugin) {
        File file = plugin.getFile("config");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection item = config.getConfigurationSection("global-speaker");
        if (item!=null) GLOBAL_SPEAKER = ItemStackUtils.buildItem(item);
        SERVER_NAME = config.getString("server-name");
        COLOR = config.getString("color");
        cooldownTime = config.getLong("cooldown-time",5);
        try {
            plugin.saveDefaultConfig();
            config.save(file);
            plugin.getLogger().info("registered name:" + COLOR + "§l[" + SERVER_NAME + COLOR + "§l]");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
