package com.insertsoda.craterchat.api.v1;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Command {
    void register(LiteralArgumentBuilder<CommandSource> literalArgumentBuilder);

    @Nullable
    String getDescription();

    @NotNull
    String getName();
}
