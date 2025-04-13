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
        final float MIN_ROWS_IN_360 = 8f;
        final float DISTANCE_IN_DEPTH = 1.4f;
        final double RADIAN_360 = 2.0 * Math.PI;

        final AdvancementPositioner root = circlemc$getRoot();
        final float maxRow = ((IAdvancementPositioner) root).circlemc$findMaxRowRecursively(Float.NEGATIVE_INFINITY);
        final float minRow = ((IAdvancementPositioner) root).circlemc$findMinRowRecursively(Float.POSITIVE_INFINITY);

        float rowsIn360 = maxRow - minRow;

        if (rowsIn360 < MIN_ROWS_IN_360) rowsIn360 = MIN_ROWS_IN_360;

        final float x = (float) Math.cos(RADIAN_360 * row / rowsIn360) * depth * DISTANCE_IN_DEPTH;
        final float y = (float) Math.sin(RADIAN_360 * row / rowsIn360) * depth * DISTANCE_IN_DEPTH;

        display.setPos(x, y);
        ci.cancel();
    }

    @Override
    public @NotNull AdvancementPositioner circlemc$getRoot() {
        AdvancementPositioner positioner = (AdvancementPositioner) (Object) this;

        while (true) {
            final AdvancementPositioner parent = ((AdvancementPositionerAccessor) positioner).getParent();
            if (parent == null) {
                return positioner;
            }
            positioner = parent;
        }
    }

    @Override
    public float circlemc$findMaxRowRecursively(float maxRow) {
        if (this.row > maxRow) {
            maxRow = this.row;
        }

        for (AdvancementPositioner advPos : ((AdvancementPositionerAccessor) this).getChildren()) {
            maxRow = ((IAdvancementPositioner) advPos).circlemc$findMaxRowRecursively(maxRow);
        }

        return maxRow;
    }

    @Override
    public float circlemc$findMinRowRecursively(float minRow) {
        if (this.row < minRow) {
            minRow = this.row;
        }

        for (AdvancementPositioner advPos : ((AdvancementPositionerAccessor) this).getChildren()) {
            minRow = ((IAdvancementPositioner) advPos).circlemc$findMinRowRecursively(minRow);
        }

        return minRow;
    }
}
