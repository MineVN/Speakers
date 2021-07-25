package tranlong5252.speakers.utils;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemStackUtils {

    public static ItemStack create(Material m, short dur) {
        ItemStack is = new ItemStack(m);
        ItemMeta meta = is.getItemMeta();
        is.setDurability(dur);
        is.setItemMeta(meta);
        return is;
    }

    public static boolean isNull(ItemStack item) {
        return item == null || item.getType() == Material.AIR;
    }

    public static Inventory fromBase64(String data, String title) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt(), title);

            // Read the serialized inventory
            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }
            dataInput.close();
            inputStream.close();
            return inventory;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

    public static Map<Integer, String> toBase64ItemStack(Map<Integer, ItemStack> items) {
        try {
            Map<Integer, String> map = new HashMap<>();

            for (int i : items.keySet()) {
                map.put(i, toBase64ItemStack(items.get(i)));
            }
            return map;
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public static ItemStack toItemStack(String base64) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(Base64Coder.decodeLines(base64));
            BukkitObjectInputStream ois = new BukkitObjectInputStream(bais);

            ItemStack item = (ItemStack) ois.readObject();
            ois.close();
            bais.close();
            return item;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toBase64(Inventory inventory) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(inventory.getSize());

            // Save every element in the list
            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }

            // Serialize that array
            dataOutput.close();
            outputStream.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public static String toBase64ItemStack(ItemStack item) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeObject(item);

            // Serialize that array
            dataOutput.close();
            outputStream.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public static boolean hasLore(ItemStack item) {
        if (item == null) return false;
        if (!item.hasItemMeta()) return false;
        return item.getItemMeta().hasLore();
    }

    public static List<String> getLore(ItemStack item) {
        if (!hasLore(item)) return new ArrayList<>();
        return item.getItemMeta().getLore();
    }

    public static void setLore(ItemStack item, List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public static void addLoreLine(ItemStack item, String line) {
        List<String> lore = getLore(item);
        lore.add(line);
        setLore(item, lore);
    }

    public static void addLore(ItemStack item, List<String> lore) {
        for (String s : lore) {
            addLoreLine(item, s);
        }
    }

    public static void setDisplayName(ItemStack item, String name) {
        if (item == null || item.getType() == Material.AIR) return;
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
    }

    public static String getName(ItemStack item) {
        if (item.getItemMeta().hasDisplayName()) {
            return item.getItemMeta().getDisplayName();
        }
        return "§f" + item.getType().name().toLowerCase().replace("_", " ");
    }

    public static void addFlag(ItemStack item, ItemFlag flag) {
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(flag);
        item.setItemMeta(meta);
    }

    public static void setUnbreakable(ItemStack item, boolean bool) {
        ItemMeta meta = item.getItemMeta();
        meta.setUnbreakable(bool);
        item.setItemMeta(meta);
    }

    public static void addEnchant(ItemStack item, Enchantment enchant, int lv) {
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(enchant, lv, false);
        item.setItemMeta(meta);
    }

    public static void addEnchantEffect(ItemStack item) {
        ItemStackUtils.addEnchant(item, Enchantment.DURABILITY, 1);
        ItemStackUtils.addFlag(item, ItemFlag.HIDE_ENCHANTS);
    }

    public static ItemStack subtractItem(ItemStack item, int amount) {
        if (item == null) return null;
        if (item.getAmount() <= amount) return null;
        item.setAmount(item.getAmount() - amount);
        return item;
    }

    public static ItemStack buildItem(ConfigurationSection config) {
        Material material = Material.valueOf(config.getString(".type"));
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        String name = config.getString(".name").replace("&", "§");
        if (name.equals("")) name = "§r";
        meta.setDisplayName(name);
        List<String> lore = Lists.newArrayList();
        config.getStringList(".lore").forEach(s -> lore.add(s.replace("&", "§")));
        if (config.contains(".enchant")) {
            Map<Enchantment, Integer> e = new HashMap<>();
            for (int i = 0; i < config.getStringList(".enchant").size(); i++) {
                String s = config.getStringList(".enchant").get(i);
                String eString = s.substring(0, s.indexOf(":"));
                Enchantment enchant = Enchantment.getByName(eString);
                int level = Integer.parseInt(s.substring(s.indexOf(":") + 1));
                e.put(enchant, level);
            }
            item.addEnchantments(e);
            for (Enchantment en : e.keySet()) meta.addEnchant(en, e.get(en), true);
        }
        if (config.contains("flag")) {
            config.getStringList("flag").forEach(s -> meta.addItemFlags(ItemFlag.valueOf(s)));
        }

        meta.setLore(lore);

        int data = config.getInt(".durability");
        item.setDurability((short) data);
        item.setItemMeta(meta);

        return item;
    }

}
