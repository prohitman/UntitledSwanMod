package com.prohitman.untitledswanmod.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.prohitman.untitledswanmod.client.model.GSwanModel;
import com.prohitman.untitledswanmod.entity.SwanEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class GSwanEntityRenderer extends GeoMobRenderer<SwanEntity> {
    private static final ResourceLocation TEXTURE_NORMAL = new ResourceLocation("untitledswanmod:textures/entity/swan.png");
    private static final ResourceLocation TEXTURE_BLACK = new ResourceLocation("untitledswanmod:textures/entity/black_swan.png");
    private static final ResourceLocation TEXTURE_BABY = new ResourceLocation("untitledswanmod:textures/entity/baby_swan.png");


    public GSwanEntityRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GSwanModel());
        this.shadowRadius = 0.7F;
    }

    @Override
    public ResourceLocation getTextureLocation(SwanEntity swanEntity) {
        if(!swanEntity.isBaby()){
            switch (swanEntity.getSwanType()){
                case 1:
                    return TEXTURE_BLACK;
                default:
                    return TEXTURE_NORMAL;
            }
        }
        else{
            return TEXTURE_BABY;
        }
    }

    @Override
    public void renderEarly(SwanEntity animatable, PoseStack stackIn, float ticks, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
        float scale = animatable.isBaby() ? 0.5F : 0.75F;
        stackIn.scale(scale, scale, scale);
    }
}
