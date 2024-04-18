package com.insertsoda.craterchat.impl;

import com.insertsoda.craterchat.api.v1.CommandMetadata;
import org.quiltmc.loader.api.ModContainer;

public class CommandMetadataImpl implements CommandMetadata {
    private String name;
    private String description;
    private ModContainer sourceModContainer;

    public CommandMetadataImpl(String name, String description, ModContainer sourceModContainer){
        this.name = name;
        this.description = description;
        this.sourceModContainer = sourceModContainer;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public ModContainer getSourceModContainer() {
        return this.sourceModContainer;
    }
}
