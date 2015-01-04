package com.sanjay900.nmsUtil.fallingblocks;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.sanjay900.nmsUtil.NMSUtil;
import com.sanjay900.nmsUtil.util.Utils;
import com.sanjay900.nmsUtil.util.V10Location;

public class FrozenSand {

	private HashMap<String,Object> storedData = new HashMap<>();
	public int entityId = 0;
	double x = 0;
	double y = 0;
	double z = 0;
	public int blockId;
	public int blockData;
	public int storageId = 0;
	String worldName = "";
	ProtocolManager pm;
	private Vector motion;
	public BukkitTask velocitytask = null;
	private final UUID uuid = UUID.randomUUID();
	public V10Location spawnloc = null;
	private NMSUtil nmsutil;
	private Player attachPlayer;
	private String name;
	protected FrozenSand(final JavaPlugin plugin,Player attachPlayer,NMSUtil nmsutil, Integer storageId, String worldName, double x, double y, double z, int blockid, int blockdata, String name) {
		this.nmsutil = nmsutil;
		this.attachPlayer = attachPlayer;
		entityId = nmsutil.frozenSandManager.nextId();
		this.x = x;
		this.y = y;
		this.z = z;
		this.blockId = blockid;
		this.blockData = blockdata;
		this.worldName = worldName;
		this.pm = ProtocolLibrary.getProtocolManager();
		this.storageId = storageId;
		this.name = name;
		velocitytask = Bukkit.getScheduler().runTaskTimer(plugin, new Runnable(){

			@Override
			public void run() {
				if (motion != null) {
					if (!Utils.isSolid(getLocation().clone().add(motion).getBlock().getType())
							&&!Utils.isSolid(getLocation().clone().add(motion).add(-0.5,0,0).getBlock().getType())
							&&!Utils.isSolid(getLocation().clone().add(motion).add(0,0,0.5).getBlock().getType())) {
						move(getLocation().add(motion));
					}
				}
			}
		}
		,2l,1l);


	}
	public void setData(String key, Object data) {
		storedData.put(key,data);
	}
	@SuppressWarnings("unchecked")
	public <dataType> dataType getData(String key) {
		if (storedData.containsKey(key)) {
			try{
				return (dataType)storedData.get(key);
			}
			catch(ClassCastException e){
				return null;
			}
		}
		return null;
	}
	protected void generate(Player observer) {
		x = Math.floor(x)+0.5;
		z = Math.floor(z)+0.5;
		try {
			PacketContainer armourStand2Item = pm.createPacket(PacketType.Play.Server.ATTACH_ENTITY);
			PacketContainer armourStand = pm.createPacket(PacketType.Play.Server.SPAWN_ENTITY_LIVING);
			StructureModifier<Integer> modifier = armourStand.getIntegers();
			byte i = Byte.valueOf((byte)0);
			i &= -5;
			i = (byte)(i | 8);
			i = (byte)(i | 1);
			modifier.write(0, this.getArmourStandIndex());
			armourStand.getIntegers().write(1, 30);
			armourStand.getIntegers().write(2, floor(x * 32.0D));
			armourStand.getIntegers().write(3, floor((y-1.5) * 32.0D));
			armourStand.getIntegers().write(4, floor(z * 32.0D));
			WrappedDataWatcher watcher = new WrappedDataWatcher();
			watcher.setObject(0, (byte) 32);
			watcher.setObject(10, i);
			if (name != null) {
				watcher.setObject(2, name);
				watcher.setObject(3, (byte)1);
			}
			armourStand.getDataWatcherModifier().write(0,watcher);
			PacketContainer item = pm.createPacket(PacketType.Play.Server.SPAWN_ENTITY);

			modifier = item.getIntegers();
			modifier.write(0, this.getFallingBlockIndex());
			modifier.write(1, (int) Math.floor(x * 32.0D));
			modifier.write(2, (int) Math.floor( (y-1.5)* 32.0D));
			modifier.write(3, (int) Math.floor(z * 32.0D));
			modifier.write(9, 70);
			modifier.write(10, blockId + (blockData << 12));
			modifier = armourStand2Item.getIntegers();
			modifier.write(0, 0);
			modifier.write(1, item.getIntegers().read(0));
			armourStand2Item.getIntegers().write(2,this.getArmourStandIndex());

			pm.sendServerPacket(observer, armourStand);
			if (attachPlayer != null) {
				PacketContainer attach3 = pm.createPacket(PacketType.Play.Server.ATTACH_ENTITY);
				modifier = attach3.getIntegers();
				modifier.write(0, 0);
				modifier.write(1, attachPlayer.getEntityId());

				modifier.write(2, armourStand.getIntegers().read(0));

				pm.sendServerPacket(observer, attach3);

			} else if (name == null){

				pm.sendServerPacket(observer, item);
				pm.sendServerPacket(observer, armourStand2Item);
			}

		} catch (InvocationTargetException e) {
		}

	}
	public static int floor(double paramDouble)
	{
		int i = (int)paramDouble;
		return paramDouble < i ? i - 1 : i;
	}

	public void clearTags(Player observer) {
		if (this.getAllEntityIds().length > 0) {
			PacketContainer packet = pm.createPacket(PacketType.Play.Server.ENTITY_DESTROY);
			packet.getIntegerArrays().write(0, this.getAllEntityIds());
			try {
				pm.sendServerPacket(observer, packet);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(
						"Cannot send packet " + packet, e);
			}

		}
	}
	public int[] getAllEntityIds() {
		int[] entityIdList = new int[3];
		for (int i = 0; i < 3; i++) {
			entityIdList[i] = this.getArmourStandIndex() + i;
		}

		return entityIdList;
	}
	public void updateName(String name) {
		if (this.name == null || name == null) return;
		if (this.name == name) return;
		this.name=name;
		PacketContainer updateName = pm.createPacket(PacketType.Play.Server.ENTITY_METADATA);
		StructureModifier<Integer> modifier = updateName.getIntegers();
		modifier.write(0, getArmourStandIndex());
		WrappedDataWatcher watcher = new WrappedDataWatcher();
		watcher.setObject(2, name);
		watcher.setObject(3, (byte)1);
		updateName.getWatchableCollectionModifier().write(0,watcher.getWatchableObjects());
		for (Player observer: Bukkit.getOnlinePlayers()) {
		try {
			pm.sendServerPacket(observer, updateName);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} 
		}

	}

	protected void moveTag(Player observer) {
		PacketContainer teleportSkull = pm.createPacket(PacketType.Play.Server.ENTITY_TELEPORT);
		StructureModifier<Integer> modifier = teleportSkull.getIntegers();
		modifier.write(0, getArmourStandIndex());

		modifier.write(1, floor( x* 32.0D));
		modifier.write(2, floor((y-1.5) * 32.0D));
		modifier.write(3, floor( z * 32.0D));

		try {
			pm.sendServerPacket(observer, teleportSkull);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} 

	}

	public int getArmourStandIndex() {
		return entityId;
	}
	public int getSkullIndex() {
		return entityId+1;
	}
	public int getFallingBlockIndex() {
		return entityId+2;
	}
	public void setVelocity(final Vector velocity) {			
		motion = velocity;
	}

	private void move(Location add) {
		this.x = add.getX();
		this.y = add.getY();
		this.z = add.getZ();
		for (Player p : add.getWorld().getPlayers()) {
			this.moveTag(p);
		}
	}
	public void show(Player observer) {
		this.generate(observer);
	}
	public Location getLocation() {
		return new Location(Bukkit.getWorld(worldName),x,y,z);
	}
	public void remove() {
		nmsutil.frozenSandManager.remove(this);
	}

	public UUID getUniqueID() {
		return uuid;
	}

	public int getMaterial() {
		return blockId;
	}
	public int getData() {
		return blockData;
	}



}