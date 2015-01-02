package com.sanjay900.nmsUtil.v1_8_R1;

import java.util.HashMap;

import org.bukkit.Bukkit;

import com.sanjay900.nmsUtil.EntityFireballImpl;
import com.sanjay900.nmsUtil.events.EntityImplCollideBlockEvent;
import com.sanjay900.nmsUtil.events.EntityCollidedWithEntityImplEvent;

import net.minecraft.server.v1_8_R1.EntityFireball;
import net.minecraft.server.v1_8_R1.MovingObjectPosition;
import net.minecraft.server.v1_8_R1.World;

public class EntityCannonFireball extends EntityFireball implements EntityFireballImpl {
	private HashMap<String,Object> storedData = new HashMap<>();
	public EntityCannonFireball(Object world, double d0, double d1, double d2, double d3, double d4, double d5) {
		super((World)world,d0,d1,d2,d3,d4,d5);
		
	}
	protected void a(MovingObjectPosition movingobjectposition) {
        if (!this.world.isStatic) {
            if (movingobjectposition.entity != null) {
            	EntityCollidedWithEntityImplEvent ev = new EntityCollidedWithEntityImplEvent(this,movingobjectposition.entity.getBukkitEntity());
            	Bukkit.getPluginManager().callEvent(ev);
            	if (!ev.isCancelled())
            		this.die();
            	return;
            }
            Bukkit.getPluginManager().callEvent(new EntityImplCollideBlockEvent(this,this.getBukkitEntity().getWorld().getBlockAt((int)movingobjectposition.pos.a,(int)movingobjectposition.pos.b,(int)movingobjectposition.pos.c)));
            this.die();
        }

    }
	@Override
	public HashMap<String, Object> getStoredData() {
		return storedData;
	}
}
