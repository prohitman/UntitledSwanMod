package com.prohitman.untitledswanmod.events;

import com.prohitman.untitledswanmod.UntitledSwanMod;
import com.prohitman.untitledswanmod.entity.SwanEntity;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.monster.RavagerEntity;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = UntitledSwanMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventBusSubscriber {
    @SubscribeEvent
    public static void scareRavager(LivingSpawnEvent event) {
        if (event.getEntityLiving() instanceof RavagerEntity) {
            RavagerEntity ravagerEntity = (RavagerEntity) event.getEntityLiving();
            ravagerEntity.goalSelector.addGoal(8, new AvoidEntityGoal<>(ravagerEntity, SwanEntity.class, 6.0F, 1.2D, 1.2D));
            ravagerEntity.getCollisionBorderSize();
        }
    }
}
