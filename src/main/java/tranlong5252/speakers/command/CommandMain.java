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
import tranlong5252.speakers.utils.DelayObj;
import tranlong5252.speakers.utils.DelayUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class CommandMain implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        String finalPrefix = "§6§l❖ " + Config.getColor() + "§l[" + Config.getServerName() + Config.getColor() + "§l]";
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cBạn không thể sử dụng lệnh này");
            return false;
        }
        if (cmd.getName().equalsIgnoreCase("loa")) {
            if (args.length <= 0) {
                sender.sendMessage("§7/loa <tin nhắn>");
                return false;
            }
            Player player = (Player) sender;
            for (ItemStack is : player.getInventory().getContents()) {
                if (is == null) continue;
                if (Config.isGlobalSpeaker(is)) {
                    if (!player.hasPermission("speaker.cooldown")) {
                        DelayObj delay = new DelayObj(player.getUniqueId(), "delay");
                        if (DelayUtils.isDelay(delay,Config.getCooldownTime()*1000,true,player)) return false;
                    }
                    is.setAmount(is.getAmount() - 1);
                    player.updateInventory();
                    StringBuilder message = new StringBuilder();
                    for (String arg : args) {
                        message.append(arg).append(" ");
                    }
                    message = new StringBuilder(ChatColor.stripColor(message.toString()).replace(';',' '));
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    DataOutputStream out = new DataOutputStream(stream);
                    String prefix = "§f ;" + finalPrefix + "§f§l " + player.getName() + " §7§l> §e";
                    String suffix = "; §f";
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
            if (!sender.hasPermission("speaker.getspeaker")) {
                sender.sendMessage("§cBạn không thể sử dụng lệnh này");
                return false;
            }
            if (args.length != 1) {
                sender.sendMessage("§cSử dụng sai cú pháp: /getloa <số lượng>");
                return false;
            }
            String amoStr= args[0];
            int amount = Integer.parseInt(amoStr);
            Player player = (Player) sender;
            ItemStack speaker = Config.getGlobalSpeaker();
            speaker.setAmount(amount);
            player.getInventory().addItem(speaker);
            sender.sendMessage("§aĐã thêm loa vào túi của bạn");
        }
        if (cmd.getName().equalsIgnoreCase("loareload")) {
            if (sender.hasPermission("speaker.reload")) {
                Speaker speaker = Speaker.get();
                Config.reload(speaker);
                sender.sendMessage("done");
            }
        }
        return false;
    }
}
