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

	private static boolean isCraterChatInitialized = false;

	public static LinkedHashMap<String, CommandContainer> getRegisteredCommands() {
		return registeredCommands;
	}

	public static boolean isIsCraterChatInitialized() {
		return isCraterChatInitialized;
	}

	@Override
	public void onInitialize(ModContainer mod) {
		for (EntrypointContainer<CraterChatPlugin> plugin : QuiltLoader.getEntrypointContainers("craterchatplugin", CraterChatPlugin.class) ) {
			for (Class<? extends Command> commandClass : plugin.getEntrypoint().register()) {
				try {
					Command command = commandClass.getDeclaredConstructor().newInstance();
					CommandContainerImpl commandContainer = new CommandContainerImpl(command, plugin.getProvider());
					if(!registeredCommands.containsKey(commandContainer.getMetadata().getName()) || Objects.equals(plugin.getProvider().metadata().id(), "craterchat")) {
						registeredCommands.put(commandContainer.getMetadata().getName(), commandContainer);

						for (String alias : commandContainer.getMetadata().getAliases()) {
							if(!registeredCommands.containsKey(alias) || Objects.equals(plugin.getProvider().metadata().id(), "craterchat")){
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

		CraterChat.Chat.buildCommandDispatcher();
		isCraterChatInitialized = true;

		LOGGER.info("CraterChat initialized");
	}

	private static void refreshCaches(){
		if(isCraterChatInitialized) {
			((HelpCommand) registeredCommands.get("help").getCommand()).refreshCommandsCache();
			((PluginsCommand) registeredCommands.get("plugins").getCommand()).refreshPluginsCache();
		}
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
		removeCommands(List.of(commandName));
	}

	public static void removeCommands(List<String> commandNames){
		for (String commandName : commandNames) {
			registeredCommands.remove(commandName);
		}
		CraterChat.Chat.buildCommandDispatcher();
	}

	/**
	 * Allows you to register a command while the game is running. It isn't recommended to use this as your main way of registering commands.
	 * @param command The command to be registered.
	 * @param modContainer The Quilt ModContainer from which the command originates from.
	 * @return true or false depending on whether the command under its command name was successfully registered (results from registering aliases are ignored).
	 */
	@ApiStatus.Experimental
	public static boolean registerCommand(Command command, ModContainer modContainer){
		return registerCommands(List.of(command), modContainer);
	}

	/**
	 * Allows you to register multiple commands while the game is running. It isn't recommended to use this as your main way of registering commands.
	 * @param commands The list of commands to be registered.
	 * @param modContainer The Quilt ModContainer from which the command originates from.
	 * @return true or false depending on whether all the commands under their respective command name were successfully registered (results from registering aliases are ignored).
	 */
	@ApiStatus.Experimental
	public static boolean registerCommands(List<Command> commands, ModContainer modContainer){

		boolean allSuccessful = true;

		for(Command command : commands){
			CommandContainer commandContainer = new CommandContainerImpl(command, modContainer);

			if(!registeredCommands.containsKey(commandContainer.getMetadata().getName())) {
				registeredCommands.put(commandContainer.getMetadata().getName(), commandContainer);

				for (String alias : commandContainer.getMetadata().getAliases()) {
					if(!registeredCommands.containsKey(alias)){
						registeredCommands.put(alias, new CommandContainerImpl(command, modContainer, alias));
					} else {
						LOGGER.warn(commandContainer.getMetadata().getSourceModContainer().metadata().name() + " attempted to register command alias /" + commandContainer.getMetadata().getName() + ", but it was already taken by another mod!");
					}
				}
			} else {
				LOGGER.warn(commandContainer.getMetadata().getSourceModContainer().metadata().name() + " attempted to register command /" + commandContainer.getMetadata().getName() + ", but it was already taken by another mod!");
				allSuccessful = false;
			}
		}

		refreshCaches();
		CraterChat.Chat.buildCommandDispatcher();

		return allSuccessful;
	}
}

