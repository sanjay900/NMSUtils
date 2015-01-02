package com.sanjay900.nmsUtil;

import java.util.List;

import org.bukkit.Material;

public interface EntityCubeImpl extends EntityImpl {
	public Material getMaterial();
	public byte getMaterialData();
	public <type> type getStored(String string);
	public List<org.bukkit.entity.Entity> getCollidedEntities(double size);
	public List<org.bukkit.entity.Entity> getCollidedEntities();
}
