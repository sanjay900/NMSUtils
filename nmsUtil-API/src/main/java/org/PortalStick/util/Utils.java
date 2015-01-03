package org.PortalStick.util;

import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class Utils {
	

	private int maxLength = 105;
	public static boolean isSolid(Material type) {
		return (type.isSolid() && !type.name().contains("SIGN"));
	}
	public static void doInventoryUpdate(final Player player, Plugin plugin) {
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {

			@Override
			public void run() {
				player.updateInventory();
			}
		}, 1L);
	}
	public boolean compareLocation(Location l, Location l2) {
		return (l.getWorld().equals(l2.getWorld())
				&& l.getX() == l2.getX())
				&& (l.getY() == l2.getY())
				&& (l.getZ() == l2.getZ());

	}

	/**
	 * Removes a item from a inventory
	 * 
	 * @param inventory
	 *            The inventory to remove from.
	 * @param mat
	 *            The material to remove .
	 * @param amount
	 *            The amount to remove.
	 * @param damage
	 *            The data value or -1 if this does not matter.
	 */

	public void remove(Inventory inv, Material type, int amount,
			short damage) {
		ItemStack[] items = inv.getContents();
		for (int i = 0; i < items.length; i++) {
			ItemStack is = items[i];
			if (is != null && is.getType() == type
					&& is.getData().getData() == damage) {
				int newamount = is.getAmount() - amount;
				if (newamount > 0) {
					is.setAmount(newamount);
					break;
				} else {
					items[i] = null;
					amount = -newamount;
					if (amount == 0)
						break;
				}
			}
		}
		inv.setContents(items);

	}
	public ItemStack getItemData(String itemString)
	{
		int num;
		int id;
		short data;

		String[] split = itemString.split(",");
		if (split.length < 2)
			num = 1;
		else
			num = Integer.parseInt(split[1]);
		split = split[0].split(":");
		if (split.length < 2)
			data = 0;
		else
			data = Short.parseShort(split[1]);

		id = Integer.parseInt(split[0]);
		return new ItemStack(id, num, data);
	}
	public Vector faceToVector(BlockFace face) {
		return new Vector(face.getModX(), face.getModY(), face.getModZ());
	}

	private String getMaxString(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (str.substring(0, i).length() == maxLength) {
				if (str.substring(i, i+1) == "")
					return str.substring(0, i-1);
				else
					return str.substring(0, i);
			}
		}
		return str;
	}
	public void sendMessage(CommandSender player, String msg) {
		int i;
		String part;
		ChatColor lastColor = ChatColor.RESET;
		for (String line : msg.split("`n")) {
			i = 0;
			while (i < line.length()) {
				part = getMaxString(line.substring(i));
				if (i+part.length() < line.length() && part.contains(" "))
					part = part.substring(0, part.lastIndexOf(" "));
				part = lastColor + part;
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', part));
				lastColor = getLastColor(part);
				i = i + part.length() -1;
			}
		}
	}



	public ChatColor getLastColor(String str) {
		int i = 0;
		ChatColor lastColor = ChatColor.RESET;
		while (i < str.length()-2) {
			for (ChatColor color: ChatColor.values()) {
				if (str.substring(i, i+2).equalsIgnoreCase(color.toString()))
					lastColor = color;
			}
			i = i+2;
		}
		return lastColor;
	}
	public Location getSimpleLocation(Location location) {
		location.setX((double)Math.round(location.getX() * 10) / 10);
		location.setY((double)Math.round(location.getY() * 10) / 10);
		location.setZ((double)Math.round(location.getZ() * 10) / 10);
		return location;
	}
	public static Entity getEntity(World world, UUID uid) {
		for (org.bukkit.entity.Entity entity : world.getEntities()) {
			if (entity.getUniqueId().equals(uid)) {
				return entity;
			}
		}
		return null;
	}
	public void setItemNameAndDesc(ItemStack item, String name, String desc) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		if(desc != null)
			meta.setLore(Arrays.asList(desc.split("\n")));
		item.setItemMeta(meta);
	}
	/**
	 * Checks weather the inventory contains a item or not.
	 * 
	 * @param inventory
	 *            The inventory to check..
	 * @param mat
	 *            The material to check .
	 * @param amount
	 *            The amount to check.
	 * @param damage
	 *            The data value or -1 if this does not matter.
	 * @return The amount of items the player has got. If this return 0 then the
	 *         check was successfull.
	 */
	public static int contains(Inventory inventory, Material mat, int amount,
			short damage) {
		int searchAmount = 0;
		for (ItemStack item : inventory.getContents()) {

			if (item == null || !item.getType().equals(mat)) {
				continue;
			}

			if (damage != -1 && item.getDurability() == damage) {
				continue;
			}

			searchAmount += item.getAmount();
		}
		return searchAmount - amount;
	}
}
