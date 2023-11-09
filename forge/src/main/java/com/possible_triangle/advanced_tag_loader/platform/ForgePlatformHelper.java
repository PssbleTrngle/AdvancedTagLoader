package com.possible_triangle.advanced_tag_loader.platform;

import com.possible_triangle.advanced_tag_loader.Constants;
import com.possible_triangle.advanced_tag_loader.platform.services.IPlatformHelper;
import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ForgePlatformHelper implements IPlatformHelper {

    public static final DeferredRegister<LootItemFunctionType> ITEM_FUNCTIONS = DeferredRegister.create(Registry.LOOT_FUNCTION_REGISTRY, Constants.MOD_ID);

    @Override
    public Supplier<LootItemFunctionType> registerLootFunction(String id, Supplier<Serializer<? extends LootItemFunction>> serializer) {
        return ITEM_FUNCTIONS.register(id, () -> new LootItemFunctionType(serializer.get()));
    }

}
