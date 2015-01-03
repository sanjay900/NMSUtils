package org.PortalStick.fallingblocks;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.sanjay900.nmsUtil.NMSUtil;

public class FrozenSandFactory {
        private final JavaPlugin plugin;
	    private String worldName;
	    private double locX;
	    private double locY;
	    private double locZ;
	    private String saveId;
		private int blockid = 0;
		private int blockdata = 0;
		private NMSUtil nmsutil;
		
		public FrozenSandFactory(JavaPlugin plugin, NMSUtil nmsutil) {
		    this.plugin = plugin;
		    this.nmsutil = nmsutil;
		}
		
	    private FrozenSandFactory withCoords(double x, double y, double z) {
	        this.locX = x;
	        this.locY = y;
	        this.locZ = z;
	        return this;
	    }

	    private FrozenSandFactory withWorld(String worldName) {
	        this.worldName = worldName;
	        return this;
	    }
	    public FrozenSandFactory withLocation(Location location) {
	        this.withCoords(location.getX(), location.getY(), location.getZ());
	        this.withWorld(location.getWorld().getName());
	        return this;
	    }
	    public FrozenSandFactory withLocation(Vector vectorLocation, String worldName) {
	        this.withCoords(vectorLocation.getX(), vectorLocation.getY(), vectorLocation.getZ());
	        this.withWorld(worldName);
	        return this;
	    }
	    public FrozenSandFactory withId(int id) {
	        this.blockid = id;
	        return this;
	    }
	    public FrozenSandFactory withData(int id) {
	        this.blockid = id;
	        return this;
	    }
	    public FrozenSand build() {
	        World world = Bukkit.getWorld(this.worldName);
	        if (world == null) {
	                plugin.getLogger().warning("Could not find valid world (" + this.worldName + ") for Hologram of ID " + this.saveId + ". Maybe the world isn't loaded yet?");
	            return null;
	        }
	        if (blockid == 0) {
	                plugin.getLogger().warning("The Hologram: " + this.saveId + ". is invalid as it has no id set!");
	            return null;
	        }
	        int id = nmsutil.frozenSandManager.getNextId();
	        FrozenSand hologram = new FrozenSand(plugin,nmsutil, id,this.worldName, this.locX, this.locY, this.locZ, blockid, blockdata);
	        nmsutil.frozenSandManager.fakeBlocks.put(hologram, new HashSet<UUID>());
	        for (Player e : world.getPlayers()) {
	        	nmsutil.frozenSandManager.checkSight(e, null); 
	        }
	        return hologram;
	    }

		public FrozenSandFactory withText(String textid) {
			this.blockid = Integer.parseInt(textid.split(":")[0]);
			this.blockdata = textid.split(":").length == 1? 0: Integer.parseInt(textid.split(":")[1]);
			return this;
		}
	}

