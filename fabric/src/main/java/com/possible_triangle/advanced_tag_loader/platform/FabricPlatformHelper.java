package com.possible_triangle.advanced_tag_loader.platform;

import com.possible_triangle.advanced_tag_loader.Constants;
import com.possible_triangle.advanced_tag_loader.platform.services.IPlatformHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

import java.util.function.Supplier;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public Supplier<LootItemFunctionType> registerLootFunction(String id, Supplier<Serializer<? extends LootItemFunction>> serializer) {
        var registered = Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(Constants.MOD_ID, id), new LootItemFunctionType(serializer.get()));
        return () -> registered;
    }

}
