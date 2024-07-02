package com.insertsoda.craterchat.commands;

import com.insertsoda.craterchat.CraterChat;
import com.insertsoda.craterchat.api.v1.Command;
import com.insertsoda.craterchat.api.v1.CommandSource;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;

public class VersionCommand implements Command {

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Override
    public void register(LiteralArgumentBuilder<CommandSource> literalArgumentBuilder) {
        literalArgumentBuilder.executes(context -> {
            ModContainer craterChatModContainer = QuiltLoader.getModContainer("craterchat").get();
            ModContainer cosmicReachModContainer = QuiltLoader.getModContainer("cosmicreach").get();

            String versionMessage = craterChatModContainer.metadata().name() + " v" + craterChatModContainer.metadata().version() +
                    "\n" +
                    "Running on Cosmic Reach v" + cosmicReachModContainer.metadata().version() +
                    "\n" +
                    "Homepage: " + craterChatModContainer.metadata().getContactInfo("homepage") +
                    "\n" +
                    "Source Code: " + craterChatModContainer.metadata().getContactInfo("sources") +
                    "\n" +
                    "API/Wiki: " + craterChatModContainer.metadata().getContactInfo("wiki");

            CraterChat.Chat.sendMessage(versionMessage);

            return 1;
        });
    }

    @Override
    public @Nullable String getDescription() {
        return "Tells you the version information of CraterChat";
    }

    @Override
    public @NotNull String getName() {
        return "version";
    }
}
