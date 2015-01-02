package com.sanjay900.nmsUtil.events;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.sanjay900.nmsUtil.EntityCubeImpl;

public class CubeGroundTickEvent extends Event {
		private static final HandlerList handlers = new HandlerList();
		private EntityCubeImpl cube;
		private Location location;
		public CubeGroundTickEvent(EntityCubeImpl cube, Location location) {
			this.cube = cube;
			this.location = location;
		}
		public Location getLocation() {
			return location;
		}
		public EntityCubeImpl getCube() {
			return cube;
		}
		public HandlerList getHandlers() {
			return handlers;
		}

		public static HandlerList getHandlerList() {
			return handlers;
		}
	
}
