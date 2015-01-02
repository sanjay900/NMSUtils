package com.sanjay900.nmsUtil.v1_8_R1;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;

import net.minecraft.server.v1_8_R1.BlockPosition;
import net.minecraft.server.v1_8_R1.Entity;
import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.EntityPlayer;
import net.minecraft.server.v1_8_R1.World;
import net.minecraft.server.v1_8_R1.WorldManager;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R1.CraftServer;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;

import com.sanjay900.nmsUtil.NMSUtil;
import com.sanjay900.nmsUtil.events.EntityDespawnEvent;
import com.sanjay900.nmsUtil.events.EntitySpawnEvent;

public class CommonWorldListener extends WorldManager {
	private HashSet<EntityPlayer> players = new HashSet<EntityPlayer>();
	public CommonWorldListener(org.bukkit.World world) {
		super(((CraftServer)Bukkit.getServer()).getServer(), ((CraftWorld)world).getHandle());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void enable() {
			Field f;
			try {
				f = World.class.getDeclaredField("u");
				f.setAccessible(true);
				List l = (List)f.get(world);
				l.add(this);
				f.set(world, l);
				this.players.addAll(this.world.players);
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				NMSUtil.printError(e);
			}
			
	}

	@SuppressWarnings("rawtypes")
	public void disable() {
		try {
			Field f = World.class.getDeclaredField("u");
			f.setAccessible(true);
			List l = (List)f.get(world);
			l.remove(this);
			f.set(world, l);
			this.players.clear();
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			NMSUtil.printError(e);
		}
		
	}
	@Override
	public final void a(Entity added) {
		if (added != null) {
			if (added instanceof EntityPlayer && !this.players.add((EntityPlayer) added)) {
				return;
			}
			Bukkit.getPluginManager().callEvent(new EntitySpawnEvent(added.getBukkitEntity()));
		}
	}

	@Override
	public final void b(Entity removed) {
		if (removed != null) {
			if (removed instanceof EntityPlayer && !this.players.remove(removed)) {
				return;
			}
			Bukkit.getPluginManager().callEvent(new EntityDespawnEvent(removed.getBukkitEntity()));
		}
	}
	@Override
	public void a(int i, boolean flag, double d0, double d1, double d2, double d3, double d4, double d5, int... aint) {}
	@Override
    public void a(String s, double d0, double d1, double d2, float f, float f1) {
    }
	@Override
    public void a(EntityHuman entityhuman, String s, double d0, double d1, double d2, float f, float f1) {
    }
	@Override
    public void a(int i, int j, int k, int l, int i1, int j1) {}
	@Override
    public void a(BlockPosition blockposition) {
    }
	@Override
    public void b(BlockPosition blockposition) {}
	@Override
    public void a(String s, BlockPosition blockposition) {}
	@Override
    public void a(EntityHuman entityhuman, int i, BlockPosition blockposition, int j) {
    }
	@Override
    public void a(int i, BlockPosition blockposition, int j) {
    }
	@Override
    public void b(int i, BlockPosition blockposition, int j) {
    }
}
