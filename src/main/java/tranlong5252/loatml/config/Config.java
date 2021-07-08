package tranlong5252.loatml.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import tranlong5252.loatml.utils.ItemStackUtils;

import java.io.File;
import java.util.Objects;

public class Config {

    private static ItemStack GLOBAL_SPEAKER;
    private static String SERVER_NAME;
    private static String COLOR;

    public static void reload(JavaPlugin plugin) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "config.yml"));
        GLOBAL_SPEAKER = ItemStackUtils.buildItem(Objects.requireNonNull(config.getConfigurationSection("global-speaker")));
        SERVER_NAME = config.getString("server-name");
        COLOR = config.getString("color");
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
}
