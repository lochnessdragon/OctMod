package com.github.lochnessdragon.octopus.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.github.lochnessdragon.octopus.OctopusMod;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.tree.CommandNode;

import net.minecraft.client.gui.screen.EditGamerulesScreen.Gamerule;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.EntitySummonArgument;
import net.minecraft.command.arguments.SuggestionProviders;
import net.minecraft.command.impl.GameRuleCommand;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.GameRules;
import net.minecraftforge.registries.ForgeRegistries;

public class MobCommand {

	//public static Map<EntityType, Boolean> CAN_GRIEF_MAP = new HashMap<EntityType, Boolean>();
	//public static Map<EntityType, Boolean> CAN_DAMAGE_MAP = new HashMap<EntityType, Boolean>();

	public static void register(CommandDispatcher<CommandSource> dispatcher) {

		dispatcher.register(Commands.literal("mob").requires(source -> source.hasPermissionLevel(4)).then(Commands
				.argument("entity", EntitySummonArgument.entitySummon())
				.suggests((ctx, builder) -> ISuggestionProvider
						.suggest(ForgeRegistries.ENTITIES.getKeys().stream().map(ResourceLocation::toString), builder))
				.then(Commands.argument("gamerule", StringArgumentType.word())
						.suggests((ctx, builder) -> ISuggestionProvider
								.suggest(new String[] { "canGrief", "canDamage" }, builder))
						.then(Commands.argument("boolean", BoolArgumentType.bool())
								.suggests((ctx, builder) -> ISuggestionProvider
										.suggest(new String[] { "true", "false" }, builder))
								.executes(context -> runCommand(context)))
						.executes(context -> runCommand(context)))));

	}

	private static int runCommand(CommandContext<CommandSource> context) {
		// TODO Auto-generated method stub
		// OctopusMod.LOGGER.info("Mob Command Run!");
		if (context.getSource().getWorld().isRemote()) {
			OctopusMod.LOGGER.info("Command must be run on the server!");
		} else {
			// context.getSource().sendFeedback(new StringTextComponent("Mob Command
			// Executing"), true);

			boolean containsBoolean = false;

			for (ParsedCommandNode<CommandSource> node : context.getNodes()) {
				if (node.getNode().getName().equals("boolean")) {
					containsBoolean = true;
				}
			}

			String gamerule = StringArgumentType.getString(context, "gamerule");

			if (gamerule.equals("canGrief")) {

				try {
					if (containsBoolean) {
						OctopusMod.mobConfigData.getCanGrief().put(
								Registry.ENTITY_TYPE.getOrDefault(EntitySummonArgument.getEntityId(context, "entity")),
								BoolArgumentType.getBool(context, "boolean"));
						OctopusMod.LOGGER.info("Can grief for {} is now set to {}",
								EntitySummonArgument.getEntityId(context, "entity").toString(),
								OctopusMod.mobConfigData.getCanGrief().get(Registry.ENTITY_TYPE
										.getOrDefault(EntitySummonArgument.getEntityId(context, "entity"))));
						context.getSource()
								.sendFeedback(
										new StringTextComponent(
												String.format("Gamerule canGrief for entity: %s has been updated",
														EntitySummonArgument.getEntityId(context, "entity").toString())),
										false);
						OctopusMod.mobConfigData.setDirty(true);
					} else {
						// Print info information
						OctopusMod.LOGGER.info("Can grief for {} set to {}",
								EntitySummonArgument.getEntityId(context, "entity").toString(),
								OctopusMod.mobConfigData.getCanGrief().getOrDefault(Registry.ENTITY_TYPE
										.getOrDefault(EntitySummonArgument.getEntityId(context, "entity")), true));
						context.getSource()
								.sendFeedback(
										new StringTextComponent(
												String.format("Gamerule canGrief for entity: %s is set to %b",
														EntitySummonArgument.getEntityId(context, "entity").toString(),
														OctopusMod.mobConfigData.getCanGrief().getOrDefault(Registry.ENTITY_TYPE.getOrDefault(
																EntitySummonArgument.getEntityId(context, "entity")), true))),
										false);
					}
				} catch (CommandSyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (gamerule.equals("canDamage")) {
				try {
					if (containsBoolean) {
						OctopusMod.mobConfigData.getCanDamage().put(
								Registry.ENTITY_TYPE.getOrDefault(EntitySummonArgument.getEntityId(context, "entity")),
								BoolArgumentType.getBool(context, "boolean"));
						OctopusMod.LOGGER.info("Can damage for {} is now set to {}",
								EntitySummonArgument.getEntityId(context, "entity").toString(),
								OctopusMod.mobConfigData.getCanDamage().get(Registry.ENTITY_TYPE
										.getOrDefault(EntitySummonArgument.getEntityId(context, "entity"))));
						context.getSource()
								.sendFeedback(
										new StringTextComponent(
												String.format("Gamerule canDamage for entity: %s has been updated",
														EntitySummonArgument.getEntityId(context, "entity").toString())),
										false);
						OctopusMod.mobConfigData.setDirty(true);
					} else {
						// Print info information
						OctopusMod.LOGGER.info("Can damage for {} set to {}",
								EntitySummonArgument.getEntityId(context, "entity").toString(),
								OctopusMod.mobConfigData.getCanDamage().getOrDefault(Registry.ENTITY_TYPE
										.getOrDefault(EntitySummonArgument.getEntityId(context, "entity")), true));
						context.getSource()
								.sendFeedback(
										new StringTextComponent(
												String.format("Gamerule canDamage for entity: %s is set to %b",
														EntitySummonArgument.getEntityId(context, "entity").toString(),
														OctopusMod.mobConfigData.getCanDamage().getOrDefault(Registry.ENTITY_TYPE.getOrDefault(
																EntitySummonArgument.getEntityId(context, "entity")), true))),
										false);
					}
				} catch (CommandSyntaxException e) {
					e.printStackTrace();
				}
			} else {
				context.getSource().sendErrorMessage(new StringTextComponent(String.format("Invalid gamerule: %s", gamerule)));
			}
		}

		return 1;
	}

}
