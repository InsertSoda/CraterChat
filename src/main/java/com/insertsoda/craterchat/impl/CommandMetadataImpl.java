package com.insertsoda.craterchat.impl;

import com.insertsoda.craterchat.api.v1.CommandMetadata;
import org.quiltmc.loader.api.ModContainer;

import java.util.List;

public class CommandMetadataImpl implements CommandMetadata {
    private final String name;
    private final String description;
    private final ModContainer sourceModContainer;
    private final String possibleArguments;

    private List<String> aliases;

    boolean isAlias;

    public CommandMetadataImpl(String name, String description, ModContainer sourceModContainer, String possibleArguments, List<String> aliases, boolean isAlias){
        this.name = name;
        this.description = description;
        this.sourceModContainer = sourceModContainer;
        this.possibleArguments = possibleArguments;
        this.aliases = aliases;
        this.isAlias = isAlias;
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

    @Override
    public String getPossibleArguments() {
        return this.possibleArguments;
    }

    @Override
    public List<String> getAliases() {
        return this.aliases;
    }

    @Override
    public boolean isAlias() {
        return this.isAlias;
    }

}
