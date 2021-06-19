package com.prohitman.untitledswanmod.init;

import com.prohitman.untitledswanmod.UntitledSwanMod;
import com.prohitman.untitledswanmod.entity.SwanEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, UntitledSwanMod.MOD_ID);

    public static final RegistryObject<EntityType<SwanEntity>> SWAN_ENTITY = ENTITY_TYPES.register(
            "swan_entity",
            () -> EntityType.Builder.create(SwanEntity::new, EntityClassification.CREATURE).size(0.75F, 1.25F)
                    .build(new ResourceLocation(UntitledSwanMod.MOD_ID, "swan_entity").toString()));

}
