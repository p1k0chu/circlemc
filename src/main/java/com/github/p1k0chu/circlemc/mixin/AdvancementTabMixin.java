package com.github.p1k0chu.circlemc.mixin;

import net.minecraft.client.gui.screen.advancement.AdvancementTab;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AdvancementTab.class)
public class AdvancementTabMixin {
    @Shadow private double originX;

    @Shadow private double originY;

    @Inject(method = "move(DD)V", at = @At(value = "HEAD"), cancellable = true)
    void move(double offsetX, double offsetY, CallbackInfo ci) {
        this.originX += offsetX;
        this.originY += offsetY;

        ci.cancel();
    }
}
