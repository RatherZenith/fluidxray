package io.github.ratherzenith.fluidxray.mixin;

import io.github.ratherzenith.fluidxray.FluidXrayState;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Vanilla already skips the faces a fluid shares with the same fluid, and it skips the top face when a block
 * above occludes it. The remaining faces (a fluid face against a solid block or any other non-air block) are
 * exactly what makes a liquid look like a solid, opaque mass while you are inside it.
 *
 * <p>{@code shouldRenderFace} is the single predicate behind the bottom face and the four side faces of a
 * fluid block. When the current mode culls fluid faces ({@code default} and {@code killFog}), it is forced to
 * {@code false} unless the neighbour is air, so only the faces actually exposed to air are kept - i.e. the
 * surface shell of the body of liquid.</p>
 *
 * <p>The method is {@code public static}, so the handler is static as well and no access transformer is
 * required.</p>
 */
@Mixin(LiquidBlockRenderer.class)
public abstract class LiquidBlockRendererMixin {

    @Inject(method = "shouldRenderFace", at = @At("RETURN"), cancellable = true)
    private static void fluidxray$onlyKeepAirExposedFaces(BlockAndTintGetter level, BlockPos pos,
                                                          FluidState fluidState, BlockState blockState,
                                                          Direction side, FluidState neighborFluidState,
                                                          CallbackInfoReturnable<Boolean> callback) {
        if (!callback.getReturnValueZ() || !FluidXrayState.getMode().cullsFluidFaces()) {
            return;
        }

        BlockState neighbor = level.getBlockState(pos.relative(side));
        if (!neighbor.isAir()) {
            callback.setReturnValue(Boolean.FALSE);
        }
    }
}
