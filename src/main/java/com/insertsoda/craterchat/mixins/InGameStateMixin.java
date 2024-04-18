package com.insertsoda.craterchat.mixins;

import com.badlogic.gdx.Input;
import com.insertsoda.craterchat.CraterChat;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGame.class)
public class InGameStateMixin {
    /*
        This mixin is responsible for closing the chat with the escape key
        It prevents the pause menu from opening as well
     */
    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/gamestates/InGame;switchToGameState(Lfinalforeach/cosmicreach/gamestates/GameState;)V"))
    public boolean closeChat(GameState gameState){
        if(CraterChat.Chat.isOpen()){
            CraterChat.Chat.toggle();
            return false;
        }
        return true;
    }

    /*
        This mixin is responsible from preventing the mouse from being uncaught when pressing escape while the chat is open
     */
    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lcom/badlogic/gdx/Input;setCursorCatched(Z)V"))
    public boolean preventSetCursorCall(Input instance, boolean b){
        return !CraterChat.Chat.isOpen();
    }

    /*
        This mixin clears the chat and sends a starting message on world load
    */
    @Inject(method = "loadWorld(Lfinalforeach/cosmicreach/world/World;)V", at = @At("TAIL"))
    public void loadWorld(World world, CallbackInfo ci){
        CraterChat.Chat.clearChat();
        CraterChat.Chat.sendMessage("[CraterChat] Press t to open chat and run /help for commands");
    }

}
