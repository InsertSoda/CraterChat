package com.insertsoda.craterchat.commands;

import com.insertsoda.craterchat.CraterChat;
import com.insertsoda.craterchat.api.v1.Command;
import com.insertsoda.craterchat.api.v1.CommandSource;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

public class ClearChatCommand implements Command {
    @Override
    public void register(LiteralArgumentBuilder<CommandSource> literalArgumentBuilder) {
        literalArgumentBuilder.executes(context -> {
            CraterChat.Chat.clearChat();
            return 1;
        });
    }

    @Override
    public String getDescription() {
        return "Clears the chat";
    }

    @Override
    public String getName() {
        return "clearchat";
    }
}
