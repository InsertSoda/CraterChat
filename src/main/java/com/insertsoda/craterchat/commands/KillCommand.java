package com.insertsoda.craterchat.commands;

import com.insertsoda.craterchat.api.v1.Command;
import com.insertsoda.craterchat.api.v1.CommandSource;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KillCommand implements Command {

    @Override
    public void register(LiteralArgumentBuilder<CommandSource> literalArgumentBuilder) {
        literalArgumentBuilder.executes(context -> {
            context.getSource().getPlayer().getEntity().hitpoints = 0;
           return 1;
        });
    }

    @Override
    public @NotNull String getName() {
        return "kill";
    }

    @Override
    public @Nullable String getDescription() {
        return "Kills the player";
    }
}
