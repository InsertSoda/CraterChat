package com.insertsoda.craterchat;

import com.insertsoda.craterchat.impl.CommandContainerImpl;
import com.insertsoda.craterchat.api.v1.Command;
import com.insertsoda.craterchat.api.v1.CommandContainer;
import com.insertsoda.craterchat.api.v1.CommandSource;
import com.insertsoda.craterchat.api.v1.CraterChatPlugin;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import dev.crmodders.cosmicquilt.api.entrypoint.ModInitializer;
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
					LiteralArgumentBuilder<CommandSource> literalArgumentBuilder = LiteralArgumentBuilder.literal(commandContainer.getMetadata().getName());
					command.register(literalArgumentBuilder);
					Chat.getCommandDispatcher().register(literalArgumentBuilder);
					registeredCommands.put(commandContainer.getMetadata().getName(), commandContainer);
				} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
					throw new RuntimeException(e);
				}
            }

			LOGGER.info("Loaded the commands from " + plugin.getProvider().metadata().name());

		}

		LOGGER.info("CraterChat initialized");
	}
}

