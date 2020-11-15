package com.github.lochnessdragon.octopus.registry;

import com.github.lochnessdragon.octopus.command.MobCommand;
import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.command.CommandSource;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class OctopusRegistry {
	public static void registerCommands(CommandDispatcher<CommandSource> dispatcher) {
		MobCommand.register(dispatcher);
	}
}
