package com.sanjay900.nmsUtil.v1_8_R1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.server.v1_8_R1.Block;
import net.minecraft.server.v1_8_R1.BlockPosition;
import net.minecraft.server.v1_8_R1.Entity;
import net.minecraft.server.v1_8_R1.EntityFallingBlock;
import net.minecraft.server.v1_8_R1.World;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import com.sanjay900.nmsUtil.EntityCubeImpl;
import com.sanjay900.nmsUtil.EntityImpl;
import com.sanjay900.nmsUtil.events.CubeGroundTickEvent;
import com.sanjay900.nmsUtil.events.EntityCollidedWithEntityImplEvent;
import com.sanjay900.nmsUtil.events.EntityImplCollideEntityImplEvent;
public class EntityCube extends EntityFallingBlock implements EntityCubeImpl {
	private int id;
	private byte data;
	private HashMap<String,Object> storedData = new HashMap<>();
	public EntityCube(World world) {
		super(world);
	}

	public EntityCube(Object world, double d0, double d1, double d2, int id, byte data,  HashMap<String,Object> storedData) {
		super((World)world);
		this.data = data;
		this.id = id;
		this.block = net.minecraft.server.v1_8_R1.Block.getById(id).fromLegacyData(data);
		this.k = true;
		this.a(0.98F, 0.98F);
		this.setPosition(d0, d1, d2);
		this.motX = 0.0D;
		this.motY = 0.0D;
		this.motZ = 0.0D;
		this.lastX = d0;
		this.lastY = d1;
		this.lastZ = d2;
		this.storedData = storedData;
		this.ticksLived = 1;
	}
	@Override
	public boolean ae() {
		return this.passenger == null;
	}
	@Override
	public void s_() {
		Block block = this.block.getBlock();

		if (block.getMaterial() == net.minecraft.server.v1_8_R1.Material.AIR) {
			this.die();
		} else {
			this.lastX = this.locX;
			this.lastY = this.locY;
			this.lastZ = this.locZ;
			BlockPosition blockposition;

			if (this.ticksLived++ == 0) {
				blockposition = new BlockPosition(this);
				if (this.world.getType(blockposition).getBlock() == block) {


				} else if (!this.world.isStatic) {
					this.die();
					return;
				}
			}

			this.motY -= 0.03999999910593033D;
			this.move(this.motX, this.motY, this.motZ);
			this.motX *= 0.9800000190734863D;
			this.motY *= 0.9800000190734863D;
			this.motZ *= 0.9800000190734863D;
			if (!this.world.isStatic) {
				blockposition = new BlockPosition(this);
				if (this.onGround) {
					Bukkit.getPluginManager().callEvent(new CubeGroundTickEvent(this,this.getBukkitEntity().getLocation()));
				}
			}
			this.checkCollision();

		}
	}
	public List<org.bukkit.entity.Entity> getCollidedEntities() {
		
		return getCollidedEntities(0.20000000298023224);
	}
	public List<org.bukkit.entity.Entity> getCollidedEntities(double size) {
		final List<org.bukkit.entity.Entity> entities = new ArrayList<>();
		final List list = this.world.getEntities((Entity)this, this.getBoundingBox().grow(size, 0.0, size));
		if (this.ad() && list != null && !list.isEmpty()) {
			for (int i = 0; i < list.size(); ++i) {
				final Entity entity = (Entity) list.get(i);
				if (entity.ae()) {
					entities.add(entity.getBukkitEntity());
				}
			}
		}
		return entities;
	}
	public void checkCollision() {
		final List list = this.world.getEntities((Entity)this, this.getBoundingBox().grow(0.20000000298023224, 0.0, 0.20000000298023224));
		if (this.ad() && list != null && !list.isEmpty()) {
			for (int i = 0; i < list.size(); ++i) {
				final Entity entity = (Entity) list.get(i);
				if (entity.ae()) {
					if (entity instanceof EntityImpl) {
						EntityImplCollideEntityImplEvent ev = new EntityImplCollideEntityImplEvent(this, (EntityImpl)entity);
						Bukkit.getPluginManager().callEvent(ev);
						if (!ev.isCancelled()) {
							entity.collide((Entity)this);
						}
					} else {
						EntityCollidedWithEntityImplEvent ev = new EntityCollidedWithEntityImplEvent(this, entity.getBukkitEntity());
						Bukkit.getPluginManager().callEvent(ev);
						if (!ev.isCancelled()) {
							entity.collide((Entity)this);
						}
					}
				}
			}
		}
	}
	@Override
	public HashMap<String, Object> getStoredData() {
		return storedData;
	}
	@Override
	public void collide(Entity en) {
		EntityCollidedWithEntityImplEvent ev = new EntityCollidedWithEntityImplEvent(this, en.getBukkitEntity());
		Bukkit.getPluginManager().callEvent(ev);
		if (!ev.isCancelled()) {
			super.collide(en);
		}
	}
	@Override
	public Material getMaterial() {
		return Material.getMaterial(id);
	}

	@Override
	public byte getMaterialData() {
		return data;
	}

	@Override
	public <DataType> DataType getStored(String key) {	
		if (storedData.containsKey(key)) {
			return (DataType) storedData.get(key);
		}
		return null;	
	}
}
