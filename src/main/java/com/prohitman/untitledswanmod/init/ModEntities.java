package com.prohitman.untitledswanmod.init;

import com.prohitman.untitledswanmod.UntitledSwanMod;
import com.prohitman.untitledswanmod.entity.SwanEggEntity;
import com.prohitman.untitledswanmod.entity.SwanEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, UntitledSwanMod.MOD_ID);

    public static final RegistryObject<EntityType<SwanEntity>> SWAN_ENTITY = ENTITY_TYPES.register(
            "swan_entity",
            () -> EntityType.Builder.of(SwanEntity::new, MobCategory.CREATURE).size(0.75F, 1.25F)
                    .build(new ResourceLocation(UntitledSwanMod.MOD_ID, "swan_entity").toString()));
    public static final RegistryObject<EntityType<SwanEggEntity>> SWAN_EGG_ENTITY = ENTITY_TYPES.register(
            "swan_egg_entity",
            () -> EntityType.Builder.<SwanEggEntity>of(SwanEggEntity::new, MobCategory.MISC).size(0.25F, 0.25F)
                    .build(new ResourceLocation(UntitledSwanMod.MOD_ID, "swan_egg_entity").toString()));
}
