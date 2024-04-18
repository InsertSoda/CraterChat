package com.insertsoda.craterchat.api.v1;

import java.util.List;

public interface CraterChatPlugin {
    List<Class<? extends Command>> register();
}
