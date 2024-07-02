package com.insertsoda.craterchat.api.v1;

public interface CommandContainer {
    Command getCommand();

    CommandMetadata getMetadata();
}
