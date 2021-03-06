package com.sanjay900.nmsUtil.events;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EntityDespawnEvent extends Event {
		private static final HandlerList handlers = new HandlerList();
		private Entity entity;
		
		public EntityDespawnEvent(Entity entity) {
			this.entity = entity;
		}
		public Entity getEntity() {
			return entity;
		}
		public HandlerList getHandlers() {
			return handlers;
		}

		public static HandlerList getHandlerList() {
			return handlers;
		}
	
}
