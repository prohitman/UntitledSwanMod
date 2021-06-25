package com.prohitman.untitledswanmod.client.renderer;

import com.prohitman.untitledswanmod.entity.SwanEggEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;

public class SwanEggEntityRenderer extends SpriteRenderer<SwanEggEntity> {
    public SwanEggEntityRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, Minecraft.getInstance().getItemRenderer());
    }

}
