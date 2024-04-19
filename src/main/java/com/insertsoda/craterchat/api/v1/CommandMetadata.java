package com.insertsoda.craterchat.api.v1;

import org.quiltmc.loader.api.ModContainer;

public interface CommandMetadata {
    String getName();

    String getDescription();

    ModContainer getSourceModContainer();

    String getPossibleArguments();
}
