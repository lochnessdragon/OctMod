package com.github.lochnessdragon.octopus.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class MobCommand {

	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		
		dispatcher.register(Commands.literal("mob").requires(source -> source.hasPermissionLevel(4)).executes(context -> runCommand(context)));
		
	}
	
	private static int runCommand(CommandContext<CommandSource> context) {
		// TODO Auto-generated method stub
		return 0;
	}

}
