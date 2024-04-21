package com.insertsoda.craterchat.api.v1;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface Command {
    void register(LiteralArgumentBuilder<CommandSource> literalArgumentBuilder);

    @NotNull
    String getName();

    @Nullable
    String getDescription();

    /**
     *  Used to say what possible arguments are possible in the /help command
     *  For example, for the /help command it returns "<page>"
     *  So that when you run the /help command it will say:
     *  `/help <page> || Returns a list of every command with a description`
     */
    default String getPossibleArguments(){
        return "";
    }

    /**
     *  Returns a list of aliases that can be used for the command instead
     */
    default List<String> getAliases(){
        return List.of();
    }
}
