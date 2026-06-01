package xyz.crunchmunch.mods.gourmand.mixin.fake_block;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.crunchmunch.mods.gourmand.api.IgnoredBlockUpdateRegistry;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockBehaviour$BlockStateBaseMixin {
    @Shadow protected abstract BlockState asState();

    @Inject(method = "updateShape", at = @At("HEAD"), cancellable = true)
    private void disableHandlingIfIgnored(CallbackInfoReturnable<BlockState> cir) {
        if (IgnoredBlockUpdateRegistry.isStateIgnored(this.asState())) {
            cir.setReturnValue(this.asState());
        }
    }

    @Inject(method = "handleNeighborChanged", at = @At("HEAD"), cancellable = true)
    private void disableHandlingIfIgnored(CallbackInfo ci) {
        if (IgnoredBlockUpdateRegistry.isStateIgnored(this.asState())) {
            ci.cancel();
        }
    }
}
