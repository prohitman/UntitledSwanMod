package com.prohitman.untitledswanmod.init;

import com.prohitman.untitledswanmod.UntitledSwanMod;
import com.prohitman.untitledswanmod.items.ModSpawnEggItem;
import com.prohitman.untitledswanmod.items.SwanEggItem;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, UntitledSwanMod.MOD_ID);

    public static final RegistryObject<Item> SWAN_EGG = ITEMS.register("swan_egg", () -> new SwanEggItem(new Item.Properties().maxStackSize(64).group(ItemGroup.FOOD)));
    public static final RegistryObject<Item> RAW_SWAN_MEAT = ITEMS.register("raw_swan_meat", () -> new Item(new Item.Properties().maxStackSize(64).group(ItemGroup.FOOD).food(new Food.Builder().hunger(2).saturation(0.3F).effect(new EffectInstance(Effects.HUNGER, 600, 0), 0.3F).meat().build())));
    public static final RegistryObject<Item> COOKED_SWAN_MEAT = ITEMS.register("cooked_swan_meat", () -> new Item(new Item.Properties().maxStackSize(64).group(ItemGroup.FOOD).food(new Food.Builder().hunger(6).saturation(0.6F).meat().build())));

    public static final RegistryObject<Item> SWAN_SPAWN_EGG = ITEMS.register("swan_spawn_egg", () -> new ModSpawnEggItem(ModEntities.SWAN_ENTITY, 0xFFFFFF, 0xFFAA00, new Item.Properties().group(ItemGroup.MISC)));
}
