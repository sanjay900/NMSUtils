package com.sanjay900.nmsUtil.events;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EntityMoveEvent extends Event {
		private static final HandlerList handlers = new HandlerList();
		private Entity entity;
		private double lastX;
		private double lastY;
		private double lastZ;
		private double locX;
		private double locY;
		private double locZ;
		private float pitch;
		private float lastPitch;
		private float yaw;
		private float lastYaw;
		
		public EntityMoveEvent(Entity entity, double lastX, double lastY,
				double lastZ, double locX, double locY, double locZ,
				float pitch, float lastPitch, float yaw, float lastYaw) {
			this.lastX = lastX;
			this.lastY = lastY;
			this.lastZ = lastZ;
			this.locX = locX;
			this.locY = locY;
			this.locZ = locZ;
			this.pitch = pitch;
			this.lastPitch = lastPitch;
			this.yaw = yaw;
			this.lastYaw = lastYaw;
			this.entity = entity;
		}

		/**
		 * Gets the world in which the entity moved
		 * 
		 * @return Last and current Entity World
		 */
		public World getWorld() {
			return this.entity.getWorld();
		}

		/**
		 * Gets the X-coordinate value before the current tick
		 * 
		 * @return Last X-coordinate
		 */
		public double getFromX() {
			return lastX;
		}

		/**
		 * Gets the Y-coordinate value before the current tick
		 * 
		 * @return Last Y-coordinate
		 */
		public double getFromY() {
			return lastY;
		}

		/**
		 * Gets the Z-coordinate value before the current tick
		 * 
		 * @return Last Z-coordinate
		 */
		public double getFromZ() {
			return lastZ;
		}

		/**
		 * Gets the yaw angle value before the current tick
		 * 
		 * @return Last yaw angle in degrees
		 */
		public float getFromYaw() {
			return lastYaw;
		}

		/**
		 * Gets the pitch angle value before the current tick
		 * 
		 * @return Last pitch angle in degrees
		 */
		public float getFromPitch() {
			return lastPitch;
		}

		/**
		 * Gets the X-coordinate value of the current tick
		 * 
		 * @return Current X-coordinate
		 */
		public double getToX() {
			return locX;
		}

		/**
		 * Gets the Y-coordinate value of the current tick
		 * 
		 * @return Current Y-coordinate
		 */
		public double getToY() {
			return locY;
		}

		/**
		 * Gets the Z-coordinate value of the current tick
		 * 
		 * @return Current Z-coordinate
		 */
		public double getToZ() {
			return locZ;
		}

		/**
		 * Gets the yaw angle value of the current tick
		 * 
		 * @return Current yaw angle in degrees
		 */
		public float getToYaw() {
			return yaw;
		}

		/**
		 * Gets the pitch angle value of the current tick
		 * 
		 * @return Current pitch angle in degrees
		 */
		public float getToPitch() {
			return pitch;
		}

		public HandlerList getHandlers() {
			return handlers;
		}

		public static HandlerList getHandlerList() {
			return handlers;
		}
	
}
