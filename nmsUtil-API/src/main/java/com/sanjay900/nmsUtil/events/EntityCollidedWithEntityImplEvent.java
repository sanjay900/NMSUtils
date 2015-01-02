package com.sanjay900.nmsUtil.events;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.sanjay900.nmsUtil.EntityImpl;

public class EntityCollidedWithEntityImplEvent extends Event implements Cancellable{
		private static final HandlerList handlers = new HandlerList();
		private EntityImpl cube;
		private Entity collider;
		private boolean cancelled = false;
		public EntityCollidedWithEntityImplEvent(EntityImpl cube, Entity en) {
			this.cube = cube;
			this.collider = en;
		}
		/**
		 * 
		 * @return Entity that initiated the collision
		 */
		public Entity getCollisionEntity() {
			return collider;
		}
		/**
		 * 
		 * @return The cube that was collided with
		 */
		public EntityImpl getImplementedEntity() {
			return cube;
		}
		public HandlerList getHandlers() {
			return handlers;
		}

		public static HandlerList getHandlerList() {
			return handlers;
		}
		@Override
		public boolean isCancelled() {
			return cancelled;
		}
		@Override
		public void setCancelled(boolean arg0) {
			cancelled = arg0;
		}
	
}
