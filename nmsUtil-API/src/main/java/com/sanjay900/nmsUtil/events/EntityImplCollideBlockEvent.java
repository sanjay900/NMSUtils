package com.sanjay900.nmsUtil.events;

import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.sanjay900.nmsUtil.EntityImpl;

public class EntityImplCollideBlockEvent extends Event {
		private static final HandlerList handlers = new HandlerList();
		private EntityImpl cube;
		private Block block;
		public EntityImplCollideBlockEvent(EntityImpl cube, Block hit) {
			this.cube = cube;
			this.block = hit;
		}
		public Block getBlock() {
			return block;
		}
		public EntityImpl getCube() {
			return cube;
		}
		public HandlerList getHandlers() {
			return handlers;
		}

		public static HandlerList getHandlerList() {
			return handlers;
		}
	
}
