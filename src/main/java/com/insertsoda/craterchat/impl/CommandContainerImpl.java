package com.insertsoda.craterchat.impl;

import com.insertsoda.craterchat.api.v1.Command;
import com.insertsoda.craterchat.api.v1.CommandContainer;
import com.insertsoda.craterchat.api.v1.CommandMetadata;
import org.quiltmc.loader.api.ModContainer;

public class CommandContainerImpl implements CommandContainer {
    private Command command;
    private CommandMetadata metadata;

    public CommandContainerImpl(Command command, ModContainer sourceModContainer){
        this(command, sourceModContainer, null);
    }

    public CommandContainerImpl(Command command, ModContainer sourceModContainer, String aliasName){
        this.command = command;
        String name = command.getName();
        if(aliasName != null){
            name = aliasName;
        }
        this.metadata = new CommandMetadataImpl(name, command.getDescription(), sourceModContainer, command.getPossibleArguments(), command.getAliases(), true);
    }

    public Command getCommand(){
        return this.command;
    }

    public CommandMetadata getMetadata(){
        return this.metadata;
    }
}
