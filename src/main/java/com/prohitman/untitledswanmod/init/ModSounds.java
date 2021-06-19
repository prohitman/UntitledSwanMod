package com.prohitman.untitledswanmod.init;

import com.prohitman.untitledswanmod.UntitledSwanMod;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, UntitledSwanMod.MOD_ID);

    public static final RegistryObject<SoundEvent> SWAN_AMBIENT = SOUND_EVENTS.register("entity.swan.ambient", () -> new SoundEvent(new ResourceLocation(UntitledSwanMod.MOD_ID, "entity.swan.ambient")));
    public static final RegistryObject<SoundEvent> SWAN_STEP_WATER = SOUND_EVENTS.register("entity.swan.step_water", () -> new SoundEvent(new ResourceLocation(UntitledSwanMod.MOD_ID, "entity.swan.step_water")));
    public static final RegistryObject<SoundEvent> SWAN_HURT = SOUND_EVENTS.register("entity.swan.hurt", () -> new SoundEvent(new ResourceLocation(UntitledSwanMod.MOD_ID, "entity.swan.hurt")));
    public static final RegistryObject<SoundEvent> SWAN_DEATH = SOUND_EVENTS.register("entity.swan.death", () -> new SoundEvent(new ResourceLocation(UntitledSwanMod.MOD_ID, "entity.swan.death")));

}
