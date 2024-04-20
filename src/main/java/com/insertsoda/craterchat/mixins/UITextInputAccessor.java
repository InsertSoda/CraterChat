package com.insertsoda.craterchat.mixins;

import finalforeach.cosmicreach.ui.UITextInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(UITextInput.class)
public interface UITextInputAccessor {
    @Accessor
    int getDesiredCharIdx();
}
