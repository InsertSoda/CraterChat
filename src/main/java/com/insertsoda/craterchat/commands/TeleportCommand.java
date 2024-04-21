package com.insertsoda.craterchat.commands;

import com.insertsoda.craterchat.CraterChat;
import com.insertsoda.craterchat.api.v1.Command;
import com.insertsoda.craterchat.api.v1.CommandManager;
import com.insertsoda.craterchat.api.v1.CommandSource;
import com.insertsoda.craterchat.api.v1.arguments.types.RelativeFloatType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import finalforeach.cosmicreach.gamestates.InGame;
import org.jetbrains.annotations.NotNull;

public class TeleportCommand implements Command {
    @Override
    public void register(LiteralArgumentBuilder<CommandSource> literalArgumentBuilder) {
        literalArgumentBuilder.then(
                CommandManager.argument("x", RelativeFloatType.argument(() -> InGame.getLocalPlayer().getEntity().position.x)).then(
                        CommandManager.argument("y", RelativeFloatType.argument(() -> InGame.getLocalPlayer().getEntity().position.y)).then(
                                CommandManager.argument("z", RelativeFloatType.argument(() -> InGame.getLocalPlayer().getEntity().position.z))
                                        .executes(context -> {
                                            float x = RelativeFloatType.getRelativeFloat(context, "x").getValue();
                                            float y = RelativeFloatType.getRelativeFloat(context, "y").getValue();
                                            float z = RelativeFloatType.getRelativeFloat(context, "z").getValue();

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
        return "Teleports the player to the specified position";
    }

    @Override
    public @NotNull String getName() {
        return "teleport";
    }

    @Override
    public String getPossibleArguments(){
        return "<x> <y> <z>";
    }
}
