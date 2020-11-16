package com.github.lochnessdragon.octopus;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.lochnessdragon.octopus.command.MobCommand;
import com.github.lochnessdragon.octopus.data.MobConfigWorldSavedData;
import com.github.lochnessdragon.octopus.registry.OctopusRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.world.World;
import net.minecraft.world.storage.SaveFormat.LevelSave;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants.WorldEvents;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.ServerLifecycleEvent;

@Mod("octopus")
public class OctopusMod {

	public static Logger LOGGER = LogManager.getLogger();
	public static final String MODID = "octopus";
	
	public static MobConfigWorldSavedData mobConfigData;

	public OctopusMod() {

		// FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onServerStarting);

		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onServerStarting(FMLServerStartingEvent event) {
		LOGGER.info("Registering Commands");
		OctopusRegistry.registerCommands(event.getServer().getCommandManager().getDispatcher());
		LOGGER.info("Registering WorldSavedData");
		mobConfigData = MobConfigWorldSavedData.register(event.getServer().getWorld(World.OVERWORLD));
	}

	@SubscribeEvent
	public void onExplosion(ExplosionEvent.Detonate event) {
		// LOGGER.info(MobCommand.CAN_GRIEF_MAP.);
		if (!(event.getExplosion().getExploder() == null)) {
			if (mobConfigData.getCanGrief().getOrDefault(event.getExplosion().getExploder().getType(),
					true) == false) {
				event.getExplosion().clearAffectedBlockPositions();
			}
		}
	}
	
	@SubscribeEvent
	public void onDamageCreature(LivingDamageEvent event) {
		
		if (!(event.getSource().getTrueSource() == null)) {
			LOGGER.info("True Source of Damage: {}", event.getSource().getTrueSource());
			if (mobConfigData.getCanDamage().getOrDefault(event.getSource().getTrueSource().getType(),
					true) == false) {
				event.setCanceled(true);
			}
		}
		
		if (!(event.getSource().getImmediateSource() == null)) {
			LOGGER.info("Immediate Source of Damage: {}", event.getSource().getImmediateSource());
		}
	}

}
