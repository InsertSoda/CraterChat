package com.insertsoda.craterchat.commands;

import com.insertsoda.craterchat.CraterChat;
import com.insertsoda.craterchat.api.v1.Command;
import com.insertsoda.craterchat.api.v1.CommandManager;
import com.insertsoda.craterchat.api.v1.CommandSource;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import finalforeach.cosmicreach.gamestates.InGame;
import org.jetbrains.annotations.NotNull;

public class TeleportCommand implements Command {
    @Override
    public void register(LiteralArgumentBuilder<CommandSource> literalArgumentBuilder) {
        literalArgumentBuilder.then(
                CommandManager.argument("x", FloatArgumentType.floatArg()).then(
                        CommandManager.argument("y", FloatArgumentType.floatArg(0, 255)).then(
                                CommandManager.argument("z", FloatArgumentType.floatArg())
                                        .executes(context -> {
                                            float x = FloatArgumentType.getFloat(context, "x");
                                            float y = FloatArgumentType.getFloat(context, "y");
                                            float z = FloatArgumentType.getFloat(context, "z");

                                            context.getSource().getPlayer().setPosition(x,y,z);

                                            CraterChat.Chat.sendMessage("Teleported Player to " + x + ", " + y + ", " + z);

                                            return 1;
                                        })
                        )
                )
        );
    }

    @Override
    public String getDescription() {
        return "Teleports the player, y is in range of 0 - 255";
    }

    @Override
    public @NotNull String getName() {
        return "teleport";
    }
}
