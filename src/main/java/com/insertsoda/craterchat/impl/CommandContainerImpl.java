package com.insertsoda.craterchat.impl;

import com.insertsoda.craterchat.api.v1.Command;
import com.insertsoda.craterchat.api.v1.CommandContainer;
import com.insertsoda.craterchat.api.v1.CommandMetadata;
import org.quiltmc.loader.api.ModContainer;

public class CommandContainerImpl implements CommandContainer {
    private Command command;
    private CommandMetadata metadata;

    public CommandContainerImpl(Command command, ModContainer sourceModContainer){
        this.command = command;
        this.metadata = new CommandMetadataImpl(command.getName(), command.getDescription(), sourceModContainer);
    }

    public Command getCommand(){
        return this.command;
    }

    public CommandMetadata getMetadata(){
        return this.metadata;
    }
}
