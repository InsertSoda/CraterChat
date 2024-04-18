package com.insertsoda.craterchat.api.v1;

import org.quiltmc.loader.api.ModContainer;

public interface CommandContainer {
    Command getCommand();

    CommandMetadata getMetadata();
}
