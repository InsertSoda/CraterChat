package com.insertsoda.craterchat.mixins;

import com.insertsoda.craterchat.CraterChat;
import finalforeach.cosmicreach.settings.Keybind;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/*
    These 2 mixins are responsible for preventing keybinds from activating while the chat is open
    otherwise you'd be able to walk around while typing in the chat
 */
@Mixin(Keybind.class)
public class KeybindMixin {
    @Inject(method = "isPressed", at = @At("HEAD"), cancellable = true)
    public void isPressed(CallbackInfoReturnable<Boolean> cir){
        if(CraterChat.Chat.isOpen()){
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "isJustPressed", at = @At("HEAD"), cancellable = true)
    public void isJustPressed(CallbackInfoReturnable<Boolean> cir){
        if(CraterChat.Chat.isOpen()){
            cir.setReturnValue(false);
        }
    }
}
