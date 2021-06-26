package com.prohitman.untitledswanmod.client.model;

import com.prohitman.untitledswanmod.entity.SwanEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class GSwanModel extends AnimatedGeoModel<SwanEntity> {
    private static final ResourceLocation TEXTURE_NORMAL = new ResourceLocation("untitledswanmod:textures/entity/swan.png");
    private static final ResourceLocation TEXTURE_BLACK = new ResourceLocation("untitledswanmod:textures/entity/black_swan.png");
    private static final ResourceLocation TEXTURE_BABY = new ResourceLocation("untitledswanmod:textures/entity/baby_swan.png");

    private static final ResourceLocation SWAN_MODEL = new ResourceLocation("untitledswanmod:geo/swan.geo.json");

    private static final ResourceLocation SWAN_ANIMATION = new ResourceLocation("untitledswanmod:animations/swan.animation.json");

    @Override
    public ResourceLocation getModelLocation(SwanEntity object) {
        return SWAN_MODEL;
    }

    @Override
    public ResourceLocation getTextureLocation(SwanEntity swanEntity) {
        if(!swanEntity.isChild()){
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
    public ResourceLocation getAnimationFileLocation(SwanEntity animatable) {
        return SWAN_ANIMATION;
    }

    @Override
    public void setLivingAnimations(SwanEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        if (entity.isChild()) {
            IBone body = this.getAnimationProcessor().getBone("body");
            if (body != null) {
                body.setScaleX(0.5f);
                body.setScaleY(0.5f);
                body.setScaleZ(0.5f);
                body.setPositionY(-5.22f);
            }
        }

        IBone head = this.getAnimationProcessor().getBone("cube_r2");
        if (head != null) {
            EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
            if (!entity.isAggressive()) {
                head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            }
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
    }
}
