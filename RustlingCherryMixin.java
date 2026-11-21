package com.wendaoshanghai.petals.mixin;

import com.wendaoshanghai.petals.config.PetalConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Optional compatibility with the user's Rustling Leaves 2.8.3.
 * Its CherryLeavesBlockMixin redirects vanilla flower emission to LeafManager.
 * Only the cherry replacement is suppressed; wind, regular leaves and ground litter remain.
 * Mixin is optional (@Pseudo): absent Rustling Leaves must not cause a missing target crash.
 */
@Pseudo
@Mixin(targets = "com.pockyl.rustling_leaves.client.LeafManager", remap = false)
public abstract class RustlingCherryMixin {
    @Inject(method = "replaceCherryPetal", at = @At("HEAD"), cancellable = true, remap = false, require = 0)
    private static void wendaopetals$replace(Level level, BlockPos pos, BlockState state,
                                            CallbackInfoReturnable<Boolean> cir) {
        if (PetalConfig.ENABLED.get() && PetalConfig.SUPPRESS_RUSTLING_CHERRY.get()) {
            cir.setReturnValue(true);
        }
    }
}
