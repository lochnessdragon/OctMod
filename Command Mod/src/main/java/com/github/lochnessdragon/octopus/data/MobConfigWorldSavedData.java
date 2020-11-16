package com.github.lochnessdragon.octopus.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.github.lochnessdragon.octopus.OctopusMod;

import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;

public class MobConfigWorldSavedData extends WorldSavedData {
	private static final String DATA_NAME = OctopusMod.MODID + "_MobConfig";
	
	private Map<EntityType, Boolean> canGriefMap = new HashMap<EntityType, Boolean>();
	private Map<EntityType, Boolean> canDamageMap = new HashMap<EntityType, Boolean>();
	
	public MobConfigWorldSavedData() {
		super(DATA_NAME);
	}
	
	public MobConfigWorldSavedData(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	public Map<EntityType, Boolean> getCanGrief() {
		return canGriefMap;
	}
	
	public Map<EntityType, Boolean> getCanDamage() {
		return canDamageMap;
	}

	@Override
	public void read(CompoundNBT nbt) {
		// TODO Auto-generated method stub
		if(nbt.contains("mobGrief")) {
			CompoundNBT griefNbt = nbt.getCompound("mobGrief");
			
			canGriefMap = nbtToMap(griefNbt);
		} 
		if(nbt.contains("mobDamage")) {
			CompoundNBT damageNbt = nbt.getCompound("mobDamage");
			
			canDamageMap = nbtToMap(damageNbt);
		}
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		
		compound.put("mobGrief", mapToNBT(canGriefMap));
		compound.put("mobDamage", mapToNBT(canDamageMap));
		return compound;
	}
	
	private CompoundNBT mapToNBT(Map<EntityType, Boolean> map) {
		CompoundNBT nbt = new CompoundNBT();
		Iterator<Entry<EntityType, Boolean>> mapEntrySet = map.entrySet().iterator();
		
		while(mapEntrySet.hasNext()) {
			Entry<EntityType, Boolean> entry = mapEntrySet.next();
			nbt.putBoolean(entry.getKey().getRegistryName().toString(), entry.getValue());
		}
		
		return nbt;
	}
	
	private Map<EntityType, Boolean> nbtToMap(CompoundNBT nbt) {
		Map<EntityType, Boolean> map = new HashMap<EntityType, Boolean>();
		Iterator<Entry<RegistryKey<EntityType<?>>, EntityType<?>>> entityType = Registry.ENTITY_TYPE.getEntries().iterator();
		
		while(entityType.hasNext()) {
			Entry<RegistryKey<EntityType<?>>, EntityType<?>> entry = entityType.next();
			if(nbt.contains(entry.getValue().getRegistryName().toString())) {
				map.put(entry.getValue(), nbt.getBoolean(entry.getValue().getRegistryName().toString()));
			}
		}
		
		return map;
	}
	
	public static MobConfigWorldSavedData register(ServerWorld world) {
		MobConfigWorldSavedData data;
		
		if(!world.isRemote) {
			data = world.getSavedData().getOrCreate(() -> {return new MobConfigWorldSavedData();}, DATA_NAME);
		} else {
			data = new MobConfigWorldSavedData();
		}
		
		return data;
	}

}
