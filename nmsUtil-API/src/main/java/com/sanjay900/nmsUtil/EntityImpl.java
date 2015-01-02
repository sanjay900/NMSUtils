package com.sanjay900.nmsUtil;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Entity;

public interface EntityImpl {
	public Entity getBukkitEntity();
	public UUID getUniqueID();
	public HashMap<String,Object> getStoredData();
}
