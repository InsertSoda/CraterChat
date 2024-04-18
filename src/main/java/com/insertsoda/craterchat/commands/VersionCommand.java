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
    @Override
    public void register(LiteralArgumentBuilder<CommandSource> literalArgumentBuilder) {
        literalArgumentBuilder.executes(context -> {
            ModContainer craterChatModContainer = QuiltLoader.getModContainer("craterchat").get();
            ModContainer cosmicReachModContainer = QuiltLoader.getModContainer("cosmicreach").get();

            StringBuilder versionMessage = new StringBuilder();

            versionMessage.append(craterChatModContainer.metadata().name()).append(" v").append(craterChatModContainer.metadata().version());
            versionMessage.append("\n");
            versionMessage.append("Running on Cosmic Reach v").append(cosmicReachModContainer.metadata().version());
            versionMessage.append("\n");
            versionMessage.append("Homepage: ").append(craterChatModContainer.metadata().getContactInfo("homepage"));
            versionMessage.append("\n");
            versionMessage.append("Source Code: ").append(craterChatModContainer.metadata().getContactInfo("sources"));
            versionMessage.append("\n");
            versionMessage.append("API/Wiki: ").append(craterChatModContainer.metadata().getContactInfo("wiki"));

            CraterChat.Chat.sendMessage(versionMessage.toString());

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
