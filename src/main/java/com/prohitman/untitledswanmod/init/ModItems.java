package com.prohitman.untitledswanmod.init;

import com.prohitman.untitledswanmod.UntitledSwanMod;
import com.prohitman.untitledswanmod.items.ModSpawnEggItem;
import com.prohitman.untitledswanmod.items.SwanEggItem;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, UntitledSwanMod.MOD_ID);

    public static final RegistryObject<Item> SWAN_EGG = ITEMS.register("swan_egg", () -> new SwanEggItem(new Item.Properties().stacksTo(64).tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> RAW_SWAN_MEAT = ITEMS.register("raw_swan_meat", () -> new Item(new Item.Properties().stacksTo(64).tab(CreativeModeTab.TAB_FOOD).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3F).effect(() -> new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.3F).meat().build())));
    public static final RegistryObject<Item> COOKED_SWAN_MEAT = ITEMS.register("cooked_swan_meat", () -> new Item(new Item.Properties().stacksTo(64).tab(CreativeModeTab.TAB_FOOD).food(new FoodProperties.Builder().nutrition(6).saturationMod(0.6F).meat().build())));

    public static final RegistryObject<Item> SWAN_SPAWN_EGG = ITEMS.register("swan_spawn_egg", () -> new ModSpawnEggItem(ModEntities.SWAN_ENTITY, 0xFFFFFF, 0xFFAA00, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
}
