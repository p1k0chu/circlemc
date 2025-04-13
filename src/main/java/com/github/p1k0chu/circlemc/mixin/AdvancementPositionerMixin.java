package com.github.p1k0chu.circlemc.mixin;

import com.github.p1k0chu.circlemc.IAdvancementPositioner;
import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.advancement.AdvancementPositioner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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

    @Shadow
    @Nullable
    protected abstract AdvancementPositioner getLastChild();

    @Shadow
    @Nullable
    protected abstract AdvancementPositioner getFirstChild();

    @Inject(method = "method_53710", at = @At("HEAD"), cancellable = true)
    void applyPosition(AdvancementDisplay display, CallbackInfo ci) {
        final float DISTANCE_IN_DEPTH = 1.4f;
        final double RADIAN_360 = 2.0 * Math.PI;

        final IAdvancementPositioner root = (IAdvancementPositioner) circlemc$getRoot();

        // plus one because full 360 comes back and overlaps
        final float rowsIn360 = root.circlemc$findMaxRowRecursively(Float.NEGATIVE_INFINITY) + 1;

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

        final var lastChild = this.getLastChild();
        if (lastChild != null) {
            maxRow = ((IAdvancementPositioner) lastChild).circlemc$findMaxRowRecursively(maxRow);
        }

        return maxRow;
    }

    @Override
    public float circlemc$findMinRowRecursively(float minRow) {
        if (this.row < minRow) {
            minRow = this.row;
        }

        final var firstChild = this.getFirstChild();
        if (firstChild != null) {
            minRow = ((IAdvancementPositioner) firstChild).circlemc$findMinRowRecursively(minRow);
        }

        return minRow;
    }
}
