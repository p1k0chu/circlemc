package com.github.p1k0chu.circlemc;

import net.minecraft.advancement.AdvancementPositioner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IAdvancementPositioner {
    float circlemc$findMaxRowRecursively(float maxRow);
    @NotNull AdvancementPositioner circlemc$getRoot();

    float circlemc$findMinRowRecursively(float minRow);
}
