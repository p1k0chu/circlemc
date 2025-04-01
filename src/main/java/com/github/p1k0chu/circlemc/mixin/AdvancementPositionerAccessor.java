package com.github.p1k0chu.circlemc.mixin;

import net.minecraft.advancement.AdvancementPositioner;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(AdvancementPositioner.class)
public interface AdvancementPositionerAccessor {
    @Accessor("parent")
    AdvancementPositioner getParent();

    @Accessor("children")
    List<AdvancementPositioner> getChildren();

    @Accessor
    int getDepth();
    @Accessor
    float getRow();

    @Invoker("increaseRowRecursively")
    void invokeIncreaseRowRecursively(float deltaRow);
}
