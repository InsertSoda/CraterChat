package com.insertsoda.craterchat.api.v1;

import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.world.World;
import org.jetbrains.annotations.Nullable;

public interface CommandSource {

    Player getPlayer();

    World getWorld();
}
