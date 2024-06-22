package com.insertsoda.craterchat.commands;

import com.insertsoda.craterchat.CraterChat;
import com.insertsoda.craterchat.api.v1.Command;
import com.insertsoda.craterchat.api.v1.CommandManager;
import com.insertsoda.craterchat.api.v1.CommandSource;
import com.insertsoda.craterchat.api.v1.arguments.types.RelativeFloatType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import finalforeach.cosmicreach.gamestates.InGame;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HpCommand implements Command {
    @Override
    public void register(LiteralArgumentBuilder<CommandSource> literalArgumentBuilder) {
        literalArgumentBuilder.then(
                CommandManager.literal("add").then(
                        CommandManager.argument("amount", FloatArgumentType.floatArg()).executes(context -> {
                            float amount = FloatArgumentType.getFloat(context, "amount");
                            context.getSource().getPlayer().getEntity().hit(-amount);
                            CraterChat.Chat.sendMessage("Added " + amount + " hp");
                            return 1;
                        })
                )
        ).then(
                CommandManager.literal("remove").then(
                        CommandManager.argument("amount", FloatArgumentType.floatArg()).executes(context -> {
                            float amount = FloatArgumentType.getFloat(context, "amount");
                            context.getSource().getPlayer().getEntity().hit(amount);
                            CraterChat.Chat.sendMessage("Removed " + amount + " hp");
                            return 1;
                        })
                )
        ).then(
                CommandManager.literal("set").then(
                        CommandManager.argument("amount", FloatArgumentType.floatArg()).executes(context -> {
                            float amount = FloatArgumentType.getFloat(context, "amount");
                            context.getSource().getPlayer().getEntity().hitpoints = amount;
                            CraterChat.Chat.sendMessage("Set hp to " + amount);
                            return 1;
                        })
                )
        ).then(
                CommandManager.literal("get").executes((context) -> {
                    CraterChat.Chat.sendMessage("You currently have " + context.getSource().getPlayer().getEntity().hitpoints + " hp");
                    return 1;
                })
        );
    }

    @Override
    public @NotNull String getName() {
        return "hp";
    }

    @Override
    public @Nullable String getDescription() {
        return "Add, remove, set or get the player's hp";
    }

    @Override
    public String getPossibleArguments() {
        return "add|remove|set|get <amount>";
    }

    @Override
    public List<String> getAliases() {
        return List.of("health");
    }
}
