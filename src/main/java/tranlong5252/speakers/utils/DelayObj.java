package tranlong5252.speakers.utils;

import java.util.UUID;

public class DelayObj {
	
	public UUID uuid;
	public String key;
	
	public DelayObj(UUID uuid, String key) {
		this.uuid = uuid;
		this.key = key;
	}
	
	public String toString() {
		return this.uuid + "_" + this.key;
	}
	
}
