package com.prohitman.untitledswanmod.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.prohitman.untitledswanmod.entity.SwanEntity;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.swing.text.html.parser.Entity;
@OnlyIn(Dist.CLIENT)
public class SwanModel extends AgeableModel<SwanEntity> {
    private final ModelRenderer body;
    private final ModelRenderer bone;
    private final ModelRenderer cube_r1;
    private final ModelRenderer head;
    private final ModelRenderer cube_r2;
    private final ModelRenderer right_wing;
    private final ModelRenderer left_wing;
    private final ModelRenderer left_leg;
    private final ModelRenderer right_leg;

    public SwanModel() {
        textureWidth = 64;
        textureHeight = 64;

        body = new ModelRenderer(this);
        body.setRotationPoint(0.0F, 22.0F, -2.0F);
        setRotationAngle(body, -0.2618F, 0.0F, 0.0F);
        body.setTextureOffset(0, 39).addBox(-5.0F, -13.0F, -8.0F, 10.0F, 9.0F, 16.0F, 0.0F, false);

        bone = new ModelRenderer(this);
        bone.setRotationPoint(0.5F, -9.0F, 6.5F);
        body.addChild(bone);


        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
        bone.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.3491F, 0.0F, 0.0F);
        cube_r1.setTextureOffset(0, 25).addBox(-5.0F, -3.0F, 0.0F, 9.0F, 6.0F, 7.0F, 0.0F, false);

        head = new ModelRenderer(this);
        head.setRotationPoint(0.0F, 12.0F, -8.0F);
        head.setTextureOffset(0, 0).addBox(-2.5F, -20.0F, 0.0F, 5.0F, 4.0F, 6.0F, 0.0F, false);
        head.setTextureOffset(0, 14).addBox(-1.5F, -18.0F, -4.0F, 3.0F, 2.0F, 4.0F, 0.0F, false);

        cube_r2 = new ModelRenderer(this);
        cube_r2.setRotationPoint(0.0F, -1.0F, 0.0F);
        head.addChild(cube_r2);
        setRotationAngle(cube_r2, -0.3927F, 0.0F, 0.0F);
        cube_r2.setTextureOffset(29, 0).addBox(-2.0F, -17.0F, -3.0F, 4.0F, 17.0F, 4.0F, 0.0F, false);

        right_wing = new ModelRenderer(this);
        right_wing.setRotationPoint(5.0F, 9.0F, -4.0F);
        right_wing.setTextureOffset(32, 14).addBox(0.0F, 0.0F, -2.0F, 1.0F, 9.0F, 15.0F, 0.0F, false);

        left_wing = new ModelRenderer(this);
        left_wing.setRotationPoint(-5.0F, 9.0F, -4.0F);
        left_wing.setTextureOffset(32, 14).addBox(-1.0F, 0.0F, -2.0F, 1.0F, 9.0F, 15.0F, 0.0F, false);

        left_leg = new ModelRenderer(this);
        left_leg.setRotationPoint(-3.0F, 19.0F, 3.0F);
        left_leg.setTextureOffset(56, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);
        left_leg.setTextureOffset(46, 9).addBox(-2.0F, 5.0F, -4.0F, 4.0F, 0.0F, 5.0F, 0.0F, false);

        right_leg = new ModelRenderer(this);
        right_leg.setRotationPoint(3.0F, 19.0F, 3.0F);
        right_leg.setTextureOffset(56, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);
        right_leg.setTextureOffset(46, 9).addBox(-2.0F, 5.0F, -4.0F, 4.0F, 0.0F, 5.0F, 0.0F, false);
    }

     /**
     * Sets this entity's model rotation angles
     */
    public void setRotationAngles(SwanEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.rotateAngleX = headPitch * ((float)Math.PI / 180F);
        this.head.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
        this.right_leg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.left_leg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    protected Iterable<ModelRenderer> getHeadParts() {
        return ImmutableList.of(this.head);
    }

    @Override
    protected Iterable<ModelRenderer> getBodyParts() {
        return ImmutableList.of(this.body, this.left_leg, this.right_leg, this.left_wing, this.right_wing);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.isChild) {
            matrixStackIn.push();
            float f = 1.0F / 2.0f;
            matrixStackIn.scale(f, f, f);
            matrixStackIn.translate(0.0D, 1.5, (double)(0.2f / 16.0F));
            this.getHeadParts().forEach((p_228230_8_) -> {
                p_228230_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            });
            matrixStackIn.pop();
            matrixStackIn.push();
            float f1 = 1.0F / 2.0f;
            matrixStackIn.scale(f1, f1, f1);
            matrixStackIn.translate(0.0D, 1.5 , 0.0D);
            this.getBodyParts().forEach((p_228229_8_) -> {
                p_228229_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            });
            matrixStackIn.pop();
        } else {
            this.getHeadParts().forEach((p_228228_8_) -> {
                p_228228_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            });
            this.getBodyParts().forEach((p_228227_8_) -> {
                p_228227_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            });
        }    }
}
