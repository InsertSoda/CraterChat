package com.insertsoda.craterchat.mixins;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.insertsoda.craterchat.Chat;
import com.insertsoda.craterchat.CraterChat;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.ui.UI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(UI.class)
public class UIMixin {
    @Inject(method = "render", at = @At("TAIL"))
    public void render(CallbackInfo ci){

        if(Chat.chatKeybind.isJustPressed() && !CraterChat.Chat.isOpen() && GameState.currentGameState instanceof InGame){
            CraterChat.Chat.toggle();
        }

        Viewport uiViewport = ((UIAccessorMixin) this).getUiViewport();
        OrthographicCamera uiCamera = ((UIAccessorMixin) this).getUiCamera();
        CraterChat.Chat.render(uiViewport, uiCamera);
    }
}
