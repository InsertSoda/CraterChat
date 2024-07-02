package com.insertsoda.craterchat.api.v1;

import org.quiltmc.loader.api.ModContainer;

import java.util.List;

public interface CommandMetadata {
    String getName();

    String getDescription();

    ModContainer getSourceModContainer();

    String getPossibleArguments();

    List<String> getAliases();

    boolean isAlias();
}
