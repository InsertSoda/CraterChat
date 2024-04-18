package com.insertsoda.craterchat.mixins;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.spongepowered.asm.mixin.Mixin;
import finalforeach.cosmicreach.ui.UI;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(UI.class)
public interface UIAccessorMixin {
    @Accessor
    Viewport getUiViewport();

    @Accessor
    OrthographicCamera getUiCamera();
}
