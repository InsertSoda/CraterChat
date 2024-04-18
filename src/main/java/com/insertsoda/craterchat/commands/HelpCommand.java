package com.insertsoda.craterchat.commands;

import com.insertsoda.craterchat.CraterChat;
import com.insertsoda.craterchat.api.v1.Command;
import com.insertsoda.craterchat.api.v1.CommandContainer;
import com.insertsoda.craterchat.api.v1.CommandSource;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import org.jetbrains.annotations.NotNull;

public class HelpCommand implements Command {
    @Override
    public void register(LiteralArgumentBuilder<CommandSource> literalArgumentBuilder) {
        literalArgumentBuilder.executes(context -> {

            StringBuilder helpMessage = new StringBuilder("==Help==");

            for (CommandContainer commandContainer : CraterChat.getRegisteredCommands()) {
                helpMessage.append("\n").append("/").append(commandContainer.getMetadata().getName()).append(" || ");
                if(commandContainer.getMetadata().getDescription() == null){
                    helpMessage.append("No description provided");
                } else {
                    helpMessage.append(commandContainer.getMetadata().getDescription());
                }

                helpMessage.append(" (Added by ").append(commandContainer.getMetadata().getSourceModContainer().metadata().name()).append(")");
            }

            CraterChat.Chat.sendMessage(helpMessage.toString());
            return 1;
        });
    }

    @Override
    public String getDescription() {
        return "Returns a list of every command with a description";
    }

    @Override
    public @NotNull String getName() {
        return "help";
    }
}
