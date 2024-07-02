package com.insertsoda.craterchat.api.v1;

import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.world.World;

public interface CommandSource {

    Player getPlayer();

    World getWorld();
}
