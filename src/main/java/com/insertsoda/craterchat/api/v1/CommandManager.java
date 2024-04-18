package com.insertsoda.craterchat.api.v1;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;

public class CommandManager {
    public static LiteralArgumentBuilder<CommandSource> literal(String name){
        return LiteralArgumentBuilder.literal(name);
    }

    public static <T> RequiredArgumentBuilder<CommandSource, T> argument(String name, ArgumentType<T> type){
        return RequiredArgumentBuilder.argument(name, type);
    }
}
