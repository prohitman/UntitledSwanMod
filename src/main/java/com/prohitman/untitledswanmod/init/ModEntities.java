package com.prohitman.untitledswanmod.init;

import com.prohitman.untitledswanmod.UntitledSwanMod;
import com.prohitman.untitledswanmod.entity.SwanEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, UntitledSwanMod.MOD_ID);

    public static final RegistryObject<EntityType<SwanEntity>> SWAN_ENTITY = ENTITY_TYPES.register(
            "swan_entity",
            () -> EntityType.Builder.of(SwanEntity::new, EntityClassification.CREATURE).sized(1.0F, 1.0F)
                    .build(new ResourceLocation(UntitledSwanMod.MOD_ID, "swan_entity").toString()));

}
