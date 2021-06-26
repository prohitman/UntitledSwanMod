package com.prohitman.untitledswanmod.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.prohitman.untitledswanmod.client.model.GSwanModel;
import com.prohitman.untitledswanmod.entity.SwanEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class GSwanEntityRenderer extends GeoEntityRenderer<SwanEntity> {
    private static final ResourceLocation TEXTURE_NORMAL = new ResourceLocation("untitledswanmod:textures/entity/swan.png");
    private static final ResourceLocation TEXTURE_BLACK = new ResourceLocation("untitledswanmod:textures/entity/black_swan.png");
    private static final ResourceLocation TEXTURE_BABY = new ResourceLocation("untitledswanmod:textures/entity/baby_swan.png");


    public GSwanEntityRenderer(EntityRendererManager p_i50961_1_) {
        super(p_i50961_1_, new GSwanModel());
        this.shadowSize = 0.7F;
    }

    @Override
    public void renderEarly(SwanEntity animatable, MatrixStack stackIn, float ticks, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        stackIn.scale(0.75F, 0.75F, 0.75F);
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
    }

    public ResourceLocation getEntityTexture(SwanEntity entity) {
        if(!entity.isChild()){
            switch (entity.getSwanType()){

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
}
