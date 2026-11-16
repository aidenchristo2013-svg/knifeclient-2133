package com.example.knifeclient.mixin;

import com.example.knifeclient.ModState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Brackets the drawing of a player model: ModState.begin() runs before it (and picks which parts to
 * hide / tint / scale if this is YOU), ModState.end() runs after, so nothing leaks to other players.
 */
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {
    @Inject(method = "render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("HEAD"))
    private void knifeclient$begin(LivingEntityRenderState state, MatrixStack matrices,
                                   VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        Object self = this;
        if (self instanceof PlayerEntityRenderer playerRenderer) {
            ModState.begin((PlayerEntityModel) playerRenderer.getModel());
        }
    }

    @Inject(method = "render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("RETURN"))
    private void knifeclient$end(LivingEntityRenderState state, MatrixStack matrices,
                                 VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        ModState.end();
    }
}
