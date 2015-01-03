package com.sanjay900.nmsUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.PortalStick.fallingblocks.FrozenSandManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.sanjay900.nmsUtil.events.EntityMoveEvent;
public class NMSUtil {
	public final HashMap<UUID,Object> wl = new HashMap<>();
	public FrozenSandManager frozenSandManager = new FrozenSandManager();
	private static String version;
	private static boolean cubeEnabled = true;
	private static Class<?> nmsWorld;
	private static Class<?> nmsWorldServer;
	private static Class<?> craftEntity;
	private static Class<?> craftWorld;
	private static Class<?> nmsEntity;
	private static Class<?> craftPlayer;
	private static Class<?> nmsPlayer;
	private static Class<?> entityTracker;
	private static Class<?> entityCube;
	private static Class<?> entityFireball;
	private static Class<?> customEntityType;
	private static Class<?> commonWorldListener;
	static {
		version = Bukkit.getServer().getClass().getPackage().getName()
				.split("\\.")[3];
		try {
			String obc = "org.bukkit.craftbukkit.";
			String nms = "net.minecraft.server.";
			craftEntity = Class.forName(obc+version+".entity.CraftEntity");
			nmsEntity = Class.forName(nms+version+".Entity");
			nmsWorld = Class.forName(nms+version+".World");
			nmsWorldServer = Class.forName(nms+version+".WorldServer");
			craftWorld = Class.forName(obc+version+".CraftWorld");
			nmsPlayer = Class.forName(nms+version+".EntityPlayer");
			entityTracker = Class.forName(nms+version+".EntityTracker");
			craftPlayer = Class.forName(obc+version+".entity.CraftPlayer");

		} catch (ClassNotFoundException e) {
			printError(e);
		}
		try {
			entityFireball = Class.forName(NMSUtil.class.getPackage().getName()+"."+version+".EntityCannonFireball");
			entityCube = Class.forName(NMSUtil.class.getPackage().getName()+"."+version+".EntityCube");
			customEntityType = Class.forName(NMSUtil.class.getPackage().getName()+"."+version+".CustomEntityType");
			commonWorldListener = Class.forName(NMSUtil.class.getPackage().getName()+"."+version+".CommonWorldListener");
		} catch (ClassNotFoundException e) {
			cubeEnabled = false;
		}
	}
	public NMSUtil()
	{
		version = Bukkit.getServer().getClass().getPackage().getName()
				.split("\\.")[3];
	}
	public static void printError(Exception e) {
		Bukkit.getLogger().severe("NMSUtils has encountered an error while starting. Please send the next few lines to the developer and they will try to fix the problem.");
		e.printStackTrace();
	}
	public Object getNMSEntity(Entity ent) {
		try {

			return craftEntity.getDeclaredMethod("getHandle").invoke(ent);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			printError(e);
		}
		return null;
	}
	public static Object getNMSWorld(World world) {
		try {
			return craftWorld.getDeclaredMethod("getHandle").invoke(world);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			printError(e);
		}
		return null;
	}
	public EntityCubeImpl getCube(Entity ent) {
		if (getNMSEntity(ent) instanceof EntityCubeImpl) {
			return (EntityCubeImpl) getNMSEntity(ent);
		}
		return null;
	}
	public EntityImpl getEntity(Entity ent) {
		if (getNMSEntity(ent) instanceof EntityImpl) {
			return (EntityImpl) getNMSEntity(ent);
		}
		return null;
	}
	public EntityCubeImpl createCube(Location location, int id, int data, HashMap<String,Object> storedData) {
		if (!checkVersion()) return null;
		Object c = null;

		try {
			c = entityCube.getConstructor(Object.class, double.class, double.class, double.class, int.class, byte.class,  HashMap.class)
					.newInstance(getNMSWorld(location.getWorld()), location.getX(), location.getY(), location.getZ(), id, (byte)data,storedData);
			nmsWorld.getDeclaredMethod("addEntity", nmsEntity).invoke(getNMSWorld(location.getWorld()), c);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException
				| SecurityException | InstantiationException e) {
			e.printStackTrace();
		}
		return (EntityCubeImpl)c;
	}
	public EntityFireballImpl createFireball(Location location, Vector vec) {
		if (!checkVersion()) return null;
		Object c = null;

		try {
			c = entityFireball.getConstructor(Object.class, double.class, double.class, double.class, double.class, double.class, double.class)
					.newInstance(getNMSWorld(location.getWorld()), location.getX(), location.getY(), location.getZ(), vec.getX(), vec.getY(), vec.getZ());
			nmsWorld.getDeclaredMethod("addEntity", nmsEntity).invoke(getNMSWorld(location.getWorld()), c);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException
				| SecurityException | InstantiationException e) {
			e.printStackTrace();
		}
		return (EntityFireballImpl)c;
	}
	public static boolean checkVersion() {
		return cubeEnabled;
	}
	public void checkMvt(Entity entity) {
		double locX = 0;
		double locY = 0;
		double locZ = 0;
		double lastX = 0;
		double lastY = 0;
		double lastZ = 0;
		float yaw = 0;
		float lastYaw = 0;
		float pitch = 0;
		float lastPitch = 0;
		try {
			locX = (double) nmsEntity.getDeclaredField("locX").get(getNMSEntity(entity));
			locY = (double) nmsEntity.getDeclaredField("locY").get(getNMSEntity(entity));
			locZ = (double) nmsEntity.getDeclaredField("locZ").get(getNMSEntity(entity));
			lastX = (double) nmsEntity.getDeclaredField("lastX").get(getNMSEntity(entity));
			lastY = (double) nmsEntity.getDeclaredField("lastY").get(getNMSEntity(entity));
			lastZ = (double) nmsEntity.getDeclaredField("lastZ").get(getNMSEntity(entity));
			yaw = (float) nmsEntity.getDeclaredField("yaw").get(getNMSEntity(entity));
			lastYaw = (float) nmsEntity.getDeclaredField("lastYaw").get(getNMSEntity(entity));
			pitch = (float) nmsEntity.getDeclaredField("pitch").get(getNMSEntity(entity));
			lastPitch = (float) nmsEntity.getDeclaredField("lastPitch").get(getNMSEntity(entity));
		} catch (IllegalArgumentException | IllegalAccessException
				| NoSuchFieldException | SecurityException e) {
			printError(e);
		}
		if (locX != lastX || locY != lastY || locZ != lastZ 
				|| yaw != lastYaw || pitch != lastPitch) {
			Bukkit.getPluginManager().callEvent(new EntityMoveEvent(entity, lastX, lastY, lastZ, locX, locY, locZ, pitch, lastPitch, yaw, lastYaw));
		}
	}
	public void flushEntityRemoveQueue(Player pl) {
		final List<Integer> ids = getEntityRemoveQueue(pl);
		if (ids.isEmpty()) {
			return;
		}
		while (ids.size() >= 128) {
			final int[] rawIds = new int[127];
			for (int i = 0; i < rawIds.length; i++) {
				rawIds[i] = ids.remove(0).intValue();
			}
			PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_DESTROY);
			packet.getIntegerArrays().write(0, rawIds);
			try {
				ProtocolLibrary.getProtocolManager().sendServerPacket(pl, packet);
			} catch (InvocationTargetException e) {
				printError(e);
			}
		}
		final int[] rawIds = new int[ids.size()];
		for (int i = 0; i < rawIds.length; i++) {
			rawIds[i] = ids.remove(0).intValue();
		}
		PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_DESTROY);
		packet.getIntegerArrays().write(0, rawIds);
		try {
			ProtocolLibrary.getProtocolManager().sendServerPacket(pl, packet);
		} catch (InvocationTargetException e) {
			printError(e);
		}
		ids.clear();
	}
	@SuppressWarnings("unchecked")
	public List<Integer> getEntityRemoveQueue(Player entity) {
		try {
			Object o = craftPlayer.getDeclaredMethod("getHandle").invoke(entity);
			return (List<Integer>) nmsPlayer.getDeclaredField("removeQueue").get(o);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException
				| SecurityException | NoSuchFieldException e) {
			printError(e);
		}
		return null;
	}
	public boolean teleport(Entity entity, Location location) {
		final Object tracker = getTracker(entity.getWorld());
		try {
			entityTracker.getDeclaredMethod("untrackEntity", nmsEntity).invoke(tracker, getNMSEntity(entity));
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			printError(e);
		}
		for (Player bukkitPlayer : entity.getWorld().getPlayers()) {
			flushEntityRemoveQueue(bukkitPlayer);
		}
		boolean succ = entity.teleport(location);
		try {
			entityTracker.getDeclaredMethod("track", nmsEntity).invoke(tracker, getNMSEntity(entity));
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			printError(e);
		}
		return succ;

	}
	public static Object getTracker(World world) {
		try {
			return nmsWorldServer.getField("tracker").get(getNMSWorld(world));
		} catch (IllegalArgumentException | IllegalAccessException
				| NoSuchFieldException | SecurityException e) {
			printError(e);
		}
		return null;
	}
	public void registerWorld(World w) {
		try {
			wl.put(w.getUID(),commonWorldListener.getConstructor(World.class).newInstance(w));
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			printError(e);
		}	
	}
	public void deregisterWorld(UUID world) {

		if (wl.containsKey(world)) {
		try {
			commonWorldListener.getMethod("disable").invoke(wl.get(world));
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			printError(e);
		}
		wl.remove(world);
		}
	}
	public void deregisterAll() {
		for (UUID wlr: wl.keySet()) {
			deregisterWorld(wlr);
		}
	}
	public void registerEntities() {
		try {
			Method m = customEntityType.getMethod("registerCustomEntity", Class.class,String.class,int.class);
			Object c = customEntityType.newInstance();
			m.invoke(c, entityCube,"FallingSand",21);
			m.invoke(c, entityFireball,"Fireball",12);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException
				| SecurityException | InstantiationException e) {
			printError(e);
		}

	}
	

}
