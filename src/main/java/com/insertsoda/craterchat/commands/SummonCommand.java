package com.insertsoda.craterchat.commands;

import com.insertsoda.craterchat.CraterChat;
import com.insertsoda.craterchat.api.v1.Command;
import com.insertsoda.craterchat.api.v1.CommandManager;
import com.insertsoda.craterchat.api.v1.CommandSource;
import com.insertsoda.craterchat.api.v1.arguments.types.RelativeFloatType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import finalforeach.cosmicreach.entities.Entity;
import finalforeach.cosmicreach.entities.EntityCreator;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.world.Zone;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SummonCommand implements Command {
    @Override
    public void register(LiteralArgumentBuilder<CommandSource> literalArgumentBuilder) {
        // TODO: make custom "identifier" argument type
        literalArgumentBuilder.then(
                CommandManager.argument("entityId", StringArgumentType.string()).then(
                        CommandManager.argument("x", RelativeFloatType.argument(() -> InGame.getLocalPlayer().getEntity().position.x)).then(
                                CommandManager.argument("y", RelativeFloatType.argument(() -> InGame.getLocalPlayer().getEntity().position.y)).then(
                                        CommandManager.argument("z", RelativeFloatType.argument(() -> InGame.getLocalPlayer().getEntity().position.z))
                                                .executes(context -> {
                                                    String entityId = StringArgumentType.getString(context, "entityId");
                                                    float x = RelativeFloatType.getRelativeFloat(context, "x").getValue();
                                                    float y = RelativeFloatType.getRelativeFloat(context, "y").getValue();
                                                    float z = RelativeFloatType.getRelativeFloat(context, "z").getValue();

                                                    this.handleSummon(entityId, x, y, z, context.getSource().getWorld().getZone(context.getSource().getPlayer().zoneId));

                                                    return 1;
                                                })
                                )
                        )
                ).executes(context -> {
                    String entityId = StringArgumentType.getString(context, "entityId");
                    float x = context.getSource().getPlayer().getEntity().position.x;
                    float y = context.getSource().getPlayer().getEntity().position.y;
                    float z = context.getSource().getPlayer().getEntity().position.z;

                    this.handleSummon(entityId, x, y, z, context.getSource().getWorld().getZone(context.getSource().getPlayer().zoneId));

                    return 1;
                })
        );
    }

    private void handleSummon(String entityId, float x, float y, float z, Zone zone){
        Entity entity = EntityCreator.get(entityId);
        if(entity == null){
            CraterChat.Chat.sendMessage("No entity with the type " + entityId + " exists");
            return;
        }
        entity.setPosition(x, y, z);
        zone.allEntities.add(entity);
        CraterChat.Chat.sendMessage("Summoned entity of type " + entityId + " at " + x + ", " + y + ", " + z);
    }

    @Override
    public @NotNull String getName() {
        return "summon";
    }

    @Override
    public @Nullable String getDescription() {
        return "Summons the specified mob";
    }

    @Override
    public String getPossibleArguments() {
        return "<entity id> <x> <y> <z>";
    }
}
