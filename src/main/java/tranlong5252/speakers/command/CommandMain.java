package tranlong5252.speakers.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.messaging.PluginMessageRecipient;
import org.jetbrains.annotations.NotNull;
import tranlong5252.speakers.Speaker;
import tranlong5252.speakers.config.Config;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class CommandMain implements CommandExecutor {

    String finalPrefix = "§6§l❖ " + Config.getColor() + "§l[" + Config.getServerName() + Config.getColor() + "§l]";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (cmd.getName().equalsIgnoreCase("loa")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cBạn không thể sử dụng lệnh này");
                return false;
            }
            if (args.length <= 0) {
                sender.sendMessage("§7/loa <tin nhắn>");
                return false;
            }
            Player player = (Player) sender;
            for (ItemStack is : player.getInventory().getContents()) {
                if (is == null) continue;
                if (Config.isGlobalSpeaker(is)) {
                    is.setAmount(is.getAmount() - 1);
                    player.updateInventory();
                    StringBuilder message = new StringBuilder();
                    for (String arg : args) {
                        message.append(arg).append(" ");
                    }
                    message = new StringBuilder(ChatColor.stripColor(message.toString()));
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    DataOutputStream out = new DataOutputStream(stream);
                    String prefix = "§f ;" + finalPrefix + "§f§l " + player.getName() + " §7§l> §e";
                    String suffix = ";§f";
                    String action = "fsbc";
                    String data2 = "";
                    try {
                        out.writeUTF(action);
                        out.writeUTF(prefix + message + suffix);
                        out.writeUTF(data2);
                        ((PluginMessageRecipient) sender).sendPluginMessage(Speaker.get(), "fs:minestrike", stream.toByteArray());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            }
            sender.sendMessage("§cBạn phải có loa trong túi đồ");
        }
        if (cmd.getName().equalsIgnoreCase("getloa")) {
            if (!sender.hasPermission("loa.admin") || !(sender instanceof Player)) {
                sender.sendMessage("§cBạn không thể sử dụng lệnh này");
                return false;
            }
            Player player = (Player) sender;
            player.getInventory().addItem(Config.getGlobalSpeaker());
            sender.sendMessage("§aĐã thêm loa vào túi của bạn");
        }
        return false;
    }
}
