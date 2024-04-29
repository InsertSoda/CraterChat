package com.insertsoda.craterchat;

import com.insertsoda.craterchat.commands.HelpCommand;
import com.insertsoda.craterchat.commands.PluginsCommand;
import com.insertsoda.craterchat.impl.CommandContainerImpl;
import com.insertsoda.craterchat.api.v1.Command;
import com.insertsoda.craterchat.api.v1.CommandContainer;
import com.insertsoda.craterchat.api.v1.CommandSource;
import com.insertsoda.craterchat.api.v1.CraterChatPlugin;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.crmodders.cosmicquilt.api.entrypoint.ModInitializer;
import org.jetbrains.annotations.ApiStatus;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.loader.api.entrypoint.EntrypointContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class CraterChat implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("CraterChat");

	public static Chat Chat = new Chat();

	private static final LinkedHashMap<String, CommandContainer> registeredCommands = new LinkedHashMap<>();

	public static LinkedHashMap<String, CommandContainer> getRegisteredCommands() {
		return registeredCommands;
	}

	@Override
	public void onInitialize(ModContainer mod) {
		for (EntrypointContainer<CraterChatPlugin> plugin : QuiltLoader.getEntrypointContainers("craterchatplugin", CraterChatPlugin.class) ) {
			for (Class<? extends Command> commandClass : plugin.getEntrypoint().register()) {
				try {
					Command command = commandClass.getDeclaredConstructor().newInstance();
					CommandContainerImpl commandContainer = new CommandContainerImpl(command, plugin.getProvider());
					if(!registeredCommands.containsKey(commandContainer.getMetadata().getName())) {
						LiteralArgumentBuilder<CommandSource> literalArgumentBuilder = LiteralArgumentBuilder.literal(commandContainer.getMetadata().getName());
						command.register(literalArgumentBuilder);
						Chat.getCommandDispatcher().register(literalArgumentBuilder);
						registeredCommands.put(commandContainer.getMetadata().getName(), commandContainer);

						for (String alias : commandContainer.getMetadata().getAliases()) {
							if(!registeredCommands.containsKey(alias)){
								LiteralArgumentBuilder<CommandSource> aliasLiteralArgumentBuilder = LiteralArgumentBuilder.literal(alias);
								command.register(aliasLiteralArgumentBuilder);
								Chat.getCommandDispatcher().register(aliasLiteralArgumentBuilder);
								registeredCommands.put(alias, new CommandContainerImpl(command, plugin.getProvider(), alias));
							} else {
								LOGGER.warn(commandContainer.getMetadata().getSourceModContainer().metadata().name() + " attempted to register command alias /" + commandContainer.getMetadata().getName() + ", but it was already taken by another mod!");
							}
						}
					} else {
						LOGGER.warn(commandContainer.getMetadata().getSourceModContainer().metadata().name() + " attempted to register command /" + commandContainer.getMetadata().getName() + ", but it was already taken by another mod!");
					}
				} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
					throw new RuntimeException(e);
				}
            }

			LOGGER.info("Loaded the commands from " + plugin.getProvider().metadata().name());

		}

		LOGGER.info("CraterChat initialized");
	}

	private static void refreshCaches(){
		((HelpCommand) registeredCommands.get("help").getCommand()).refreshCommandsCache();
		((PluginsCommand) registeredCommands.get("plugins").getCommand()).refreshPluginsCache();
	}

	/**
	 * Allows you to check if a command under a specific name is already registered.
	 * @param commandName Name of the command
	 * @return true or false depending on if a command under the specified name is registered.
	 */
	public static boolean isCommandRegistered(String commandName){
		return registeredCommands.containsKey(commandName);
	}

	/**
	 * Allows you to remove a command while the game is running.
	 * @param commandName Name of the command
	 */
	@ApiStatus.Experimental
	public static void removeCommand(String commandName){
		registeredCommands.remove(commandName);
	}

	/**
	 * Allows you to register a command while the game is running. It isn't recommended to use this as your main way of registering commands.
	 * @param command The command to be registered.
	 * @param modContainer The Quilt ModContainer from which the command originates from.
	 * @return true or false depending on whether the command under its command name was successfully registered (results from registering aliases are ignored).
	 */
	@ApiStatus.Experimental
	public static boolean registerCommand(Command command, ModContainer modContainer){
		CommandContainer commandContainer = new CommandContainerImpl(command, modContainer);

		if(!registeredCommands.containsKey(commandContainer.getMetadata().getName())) {
			LiteralArgumentBuilder<CommandSource> literalArgumentBuilder = LiteralArgumentBuilder.literal(commandContainer.getMetadata().getName());
			command.register(literalArgumentBuilder);
			Chat.getCommandDispatcher().register(literalArgumentBuilder);
			registeredCommands.put(commandContainer.getMetadata().getName(), commandContainer);

			for (String alias : commandContainer.getMetadata().getAliases()) {
				if(!registeredCommands.containsKey(alias)){
					LiteralArgumentBuilder<CommandSource> aliasLiteralArgumentBuilder = LiteralArgumentBuilder.literal(alias);
					command.register(aliasLiteralArgumentBuilder);
					Chat.getCommandDispatcher().register(aliasLiteralArgumentBuilder);
					registeredCommands.put(alias, new CommandContainerImpl(command, modContainer, alias));
				} else {
					LOGGER.warn(commandContainer.getMetadata().getSourceModContainer().metadata().name() + " attempted to register command alias /" + commandContainer.getMetadata().getName() + ", but it was already taken by another mod!");
				}
			}

			refreshCaches();

			return true;
		} else {
			LOGGER.warn(commandContainer.getMetadata().getSourceModContainer().metadata().name() + " attempted to register command /" + commandContainer.getMetadata().getName() + ", but it was already taken by another mod!");
			return false;
		}
	}
}

