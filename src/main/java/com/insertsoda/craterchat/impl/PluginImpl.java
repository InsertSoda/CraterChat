package com.insertsoda.craterchat.impl;

import com.insertsoda.craterchat.api.v1.Command;
import com.insertsoda.craterchat.api.v1.CraterChatPlugin;
import com.insertsoda.craterchat.commands.ClearChatCommand;
import com.insertsoda.craterchat.commands.HelpCommand;
import com.insertsoda.craterchat.commands.TeleportCommand;
import com.insertsoda.craterchat.commands.VersionCommand;

import java.util.List;

public class PluginImpl implements CraterChatPlugin {
    @Override
    public List<Class<? extends Command>> register() {
        return List.of(
                ClearChatCommand.class,
                HelpCommand.class,
                TeleportCommand.class,
                VersionCommand.class
        );
    }
}
