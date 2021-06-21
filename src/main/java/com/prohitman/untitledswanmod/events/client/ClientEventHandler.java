package com.prohitman.untitledswanmod.events.client;

import com.prohitman.untitledswanmod.UntitledSwanMod;
import com.prohitman.untitledswanmod.client.renderer.SwanEntityRenderer;
import com.prohitman.untitledswanmod.entity.SwanEggEntity;
import com.prohitman.untitledswanmod.init.ModEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = UntitledSwanMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        ItemRenderer itemRendererIn = Minecraft.getInstance().getItemRenderer();

        RenderingRegistry.registerEntityRenderingHandler(ModEntities.SWAN_ENTITY.get(), SwanEntityRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.SWAN_EGG_ENTITY.get(),manager -> new SpriteRenderer(manager, itemRendererIn));
    }
}
