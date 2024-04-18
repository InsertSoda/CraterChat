package com.insertsoda.craterchat.impl;

import com.insertsoda.craterchat.api.v1.CommandSource;
import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.world.World;
import org.jetbrains.annotations.Nullable;

public class CommandSourceImpl implements CommandSource {
    Player player;
    World world;

    public CommandSourceImpl(@Nullable Player player, @Nullable World world){
        this.player = player;
        this.world = world;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    public World getWorld(){
        return this.world;
    }
}
