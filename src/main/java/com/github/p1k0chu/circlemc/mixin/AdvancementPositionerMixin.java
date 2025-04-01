package com.github.p1k0chu.circlemc.mixin;

import com.github.p1k0chu.circlemc.IAdvancementPositioner;
import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.advancement.AdvancementPositioner;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(AdvancementPositioner.class)
public abstract class AdvancementPositionerMixin implements IAdvancementPositioner {
    @Shadow
    private int depth;

    @Shadow
    private float row;

    @Inject(method = "method_53710", at = @At("HEAD"), cancellable = true)
    void applyPosition(AdvancementDisplay display, CallbackInfo ci) {
        AdvancementPositioner root = circlemc$getRoot();
        float maxRow = ((IAdvancementPositioner)root).circlemc$findMaxRowRecursively(Float.NEGATIVE_INFINITY);
        float minRow = ((IAdvancementPositioner)root).circlemc$findMinRowRecursively(Float.POSITIVE_INFINITY);

        float deltaRow = maxRow - minRow;

        float x = (float) Math.cos(2 * Math.PI * row / deltaRow) * depth / 2;
        float y = (float) Math.sin(2 * Math.PI * row / deltaRow) * depth / 2;

        display.setPos(x, y);
        ci.cancel();
    }

    @Override
    public @NotNull AdvancementPositioner circlemc$getRoot() {
        AdvancementPositioner positioner = (AdvancementPositioner) (Object)this;

        while(true) {
            AdvancementPositioner parent = ((AdvancementPositionerAccessor) positioner).getParent();
            if(parent == null) {
                return positioner;
            }
            positioner = parent;
        }
    }

    @Override
    public float circlemc$findMaxRowRecursively(float maxRow) {
        if(this.row > maxRow) {
            maxRow = this.row;
        }

        for(AdvancementPositioner advPos : ((AdvancementPositionerAccessor)this).getChildren()) {
            maxRow = ((IAdvancementPositioner) advPos).circlemc$findMaxRowRecursively(maxRow);
        }

        return maxRow;
    }

    @Override
    public float circlemc$findMinRowRecursively(float minRow) {
        if(this.row < minRow) {
            minRow = this.row;
        }

        for(AdvancementPositioner advPos : ((AdvancementPositionerAccessor)this).getChildren()) {
            minRow = ((IAdvancementPositioner) advPos).circlemc$findMinRowRecursively(minRow);
        }

        return minRow;
    }
}
