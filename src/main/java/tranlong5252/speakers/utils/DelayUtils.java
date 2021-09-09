package tranlong5252.speakers.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentHashMap;

public class DelayUtils {
	
	public static ConcurrentHashMap<String, Long> delayMap = new ConcurrentHashMap<>();
	
	public static boolean isDelay(DelayObj delayObj, long delayTime, boolean message, CommandSender sender) {
		long time = getDelay(delayObj, delayTime);
		if(time != 0) {
			if(message) sender.sendMessage(getDelayMessage(time));
			return true;
		} else {
			return false;
		}
	}
	
	public static long getDelay(DelayObj delayObj, long delay) {
		if(!delayMap.containsKey(delayObj.toString())) {
			delayMap.put(delayObj.toString(), System.currentTimeMillis());
			return 0;
		} else {
			long time = delayMap.get(delayObj.toString());
			if(System.currentTimeMillis() - time < delay) {
				return delay - (System.currentTimeMillis() - time);
			} else {
				delayMap.put(delayObj.toString(), System.currentTimeMillis());
				return 0;
			}
		}
	}
	
	public static String getDelayMessage(long time) {
		int seconds = (int) (time / 1000);
		return "§cBạn phải chờ §a" + seconds + "s §cnữa để sử dụng";
	}
	
	public static boolean isDelay(Player p) {
		DelayObj delay = new DelayObj(p.getUniqueId(), "delay");
		if(getDelay(delay, 3000) != 0) {
			p.sendMessage("§cBạn thao tác quá nhanh");
			return true;
		}
		return false;
	}
	
	
}
