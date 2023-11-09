package com.possible_triangle.advanced_tag_loader.mixin;

import net.minecraft.tags.TagLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TagLoader.class)
public interface TagLoaderAccessor {

    @Accessor
    String getDirectory();

}
