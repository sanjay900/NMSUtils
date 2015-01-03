package com.sanjay900.nmsUtil.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerMovedInHologramEvent extends Event {
		private static final HandlerList handlers = new HandlerList();
		private boolean left;
		private boolean right;
		private boolean up;
		private boolean down;
		private Player player;
		
		public PlayerMovedInHologramEvent(Player player, boolean up, boolean down, boolean left, boolean right) {
			this.player = player;
			this.up = up;
			this.down = down;
			this.left = left;
			this.right = right;
		}
		public boolean isLeftPressed() {
			return left;
		}
		public boolean isRightPressed() {
			return right;
		}
		public boolean isUpPressed() {
			return up;
		}
		public boolean isDownPressed() {
			return down;
		}

		public HandlerList getHandlers() {
			return handlers;
		}

		public static HandlerList getHandlerList() {
			return handlers;
		}
	
}
