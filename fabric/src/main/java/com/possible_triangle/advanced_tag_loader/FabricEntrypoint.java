package com.possible_triangle.advanced_tag_loader;

import net.fabricmc.api.ModInitializer;

public class FabricEntrypoint implements ModInitializer {

    @Override
    public void onInitialize() {
        CommonClass.init();
    }
}
