package com.prohitman.untitledswanmod.events;

import com.prohitman.untitledswanmod.UntitledSwanMod;
import com.prohitman.untitledswanmod.entity.SwanEntity;
import com.prohitman.untitledswanmod.init.ModEntities;
import com.prohitman.untitledswanmod.items.ModSpawnEggItem;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = UntitledSwanMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusSubscriber {
    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.SWAN_ENTITY.get(), SwanEntity.createAttributes().build());
    }

    // Thanks To Cadiboo
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPostRegisterEntities(final RegistryEvent.Register<EntityType<?>> event) {
        ModSpawnEggItem.initUnaddedEggs();
    }
}
