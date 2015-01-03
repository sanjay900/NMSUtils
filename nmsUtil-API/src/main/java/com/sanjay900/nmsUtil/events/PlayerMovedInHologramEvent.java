package com.sanjay900.nmsUtil.events;

import java.util.ArrayList;

import org.PortalStick.util.Button;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerMovedInHologramEvent extends Event {
		private static final HandlerList handlers = new HandlerList();
		private ArrayList<Button> buttons;
		private Player player;
	
		public PlayerMovedInHologramEvent(Player player,
				ArrayList<Button> buttons) {
			this.player = player;
			this.buttons = buttons;
		}
		public ArrayList<Button> getButtonsPressed() {
			return buttons;
		}
		public Player getPlayer() {
			return player;
		}

		public HandlerList getHandlers() {
			return handlers;
		}

		public static HandlerList getHandlerList() {
			return handlers;
		}
	
}
