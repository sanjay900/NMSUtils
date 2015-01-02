package com.sanjay900.nmsUtil.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.sanjay900.nmsUtil.EntityImpl;

public class EntityImplCollideEntityImplEvent extends Event implements Cancellable{
		private static final HandlerList handlers = new HandlerList();
		private EntityImpl cube;
		private EntityImpl collider;
		private boolean cancelled = false;
		public EntityImplCollideEntityImplEvent(EntityImpl cube, EntityImpl en) {
			this.cube = en;
			this.collider = cube;
		}
		/**
		 * 
		 * @return The EntityImpl that collided with another entityImpl
		 */
		public EntityImpl getImplementedCollider() {
			return collider;
		}
		/**
		 * 
		 * @return The EntityImpl that was collided with
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
