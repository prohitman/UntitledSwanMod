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
        this.texWidth = 64;
        this.texHeight = 64;

        body = new ModelRenderer(this);
        body.setPos(0.0F, 22.0F, -2.0F);
        setRotationAngle(body, -0.2618F, 0.0F, 0.0F);
        body.texOffs(0, 39).addBox(-5.0F, -13.0F, -8.0F, 10.0F, 9.0F, 16.0F, 0.0F, false);

        bone = new ModelRenderer(this);
        bone.setPos(0.5F, -9.0F, 6.5F);
        body.addChild(bone);


        cube_r1 = new ModelRenderer(this);
        cube_r1.setPos(0.0F, 0.0F, 0.0F);
        bone.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.3491F, 0.0F, 0.0F);
        cube_r1.texOffs(0, 25).addBox(-5.0F, -3.0F, 0.0F, 9.0F, 6.0F, 7.0F, 0.0F, false);

        head = new ModelRenderer(this);
        head.setPos(0.0F, 12.0F, -8.0F);
        head.texOffs(0, 0).addBox(-2.5F, -20.0F, 0.0F, 5.0F, 4.0F, 6.0F, 0.0F, false);
        head.texOffs(0, 14).addBox(-1.5F, -18.0F, -4.0F, 3.0F, 2.0F, 4.0F, 0.0F, false);

        cube_r2 = new ModelRenderer(this);
        cube_r2.setPos(0.0F, -1.0F, 0.0F);
        head.addChild(cube_r2);
        setRotationAngle(cube_r2, -0.3927F, 0.0F, 0.0F);
        cube_r2.texOffs(29, 0).addBox(-2.0F, -17.0F, -3.0F, 4.0F, 17.0F, 4.0F, 0.0F, false);

        right_wing = new ModelRenderer(this);
        right_wing.setPos(5.0F, 9.0F, -4.0F);
        right_wing.texOffs(32, 14).addBox(0.0F, 0.0F, -2.0F, 1.0F, 9.0F, 15.0F, 0.0F, false);

        left_wing = new ModelRenderer(this);
        left_wing.setPos(-5.0F, 9.0F, -4.0F);
        left_wing.texOffs(32, 14).addBox(-1.0F, 0.0F, -2.0F, 1.0F, 9.0F, 15.0F, 0.0F, false);

        left_leg = new ModelRenderer(this);
        left_leg.setPos(-3.0F, 19.0F, 3.0F);
        left_leg.texOffs(56, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);
        left_leg.texOffs(46, 9).addBox(-2.0F, 5.0F, -4.0F, 4.0F, 0.0F, 5.0F, 0.0F, false);

        right_leg = new ModelRenderer(this);
        right_leg.setPos(3.0F, 19.0F, 3.0F);
        right_leg.texOffs(56, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);
        right_leg.texOffs(46, 9).addBox(-2.0F, 5.0F, -4.0F, 4.0F, 0.0F, 5.0F, 0.0F, false);
    }

    @Override
    protected Iterable<ModelRenderer> headParts() {
        return ImmutableList.of(this.head);
    }

    @Override
    protected Iterable<ModelRenderer> bodyParts() {
        return ImmutableList.of(this.body, this.left_leg, this.right_leg, this.left_wing, this.right_wing);
    }

    @Override
    public void setupAnim(SwanEntity p_225597_1_, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
