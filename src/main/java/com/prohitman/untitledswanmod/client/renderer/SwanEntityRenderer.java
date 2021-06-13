package com.prohitman.untitledswanmod.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.prohitman.untitledswanmod.client.model.SwanModel;
import com.prohitman.untitledswanmod.entity.SwanEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SwanEntityRenderer extends MobRenderer<SwanEntity, SwanModel> {
    private static final ResourceLocation TEXTURE_NORMAL = new ResourceLocation("untitledswanmod:textures/entity/swan.png");
    private static final ResourceLocation TEXTURE_BLACK = new ResourceLocation("untitledswanmod:textures/entity/black_swan.png");
    private static final ResourceLocation TEXTURE_BABY = new ResourceLocation("untitledswanmod:textures/entity/baby_swan.png");


    public SwanEntityRenderer(EntityRendererManager p_i50961_1_) {
        super(p_i50961_1_, new SwanModel(), 0.75F);
    }

    @Override
    protected void preRenderCallback(SwanEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(0.75F, 0.75F, 0.75F);
        super.preRenderCallback(entitylivingbaseIn, matrixStackIn, partialTickTime);
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
