package com.prohitman.untitledswanmod.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.prohitman.untitledswanmod.client.model.SwanModel;
import com.prohitman.untitledswanmod.entity.SwanEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
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
    public void render(SwanEntity p_225623_1_, float p_225623_2_, float p_225623_3_, MatrixStack matrixstack, IRenderTypeBuffer p_225623_5_, int p_225623_6_) {
        matrixstack.scale(0.75F, 0.75F, 0.75F);
        super.render(p_225623_1_, p_225623_2_, p_225623_3_, matrixstack, p_225623_5_, p_225623_6_);
    }

    public ResourceLocation getTextureLocation(SwanEntity entity) {
        if(!entity.isBaby()){
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
