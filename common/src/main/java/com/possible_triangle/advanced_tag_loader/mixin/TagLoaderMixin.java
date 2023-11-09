package com.possible_triangle.advanced_tag_loader.mixin;

import com.mojang.datafixers.util.Either;
import com.possible_triangle.advanced_tag_loader.AdvancedTagLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Mixin(TagLoader.class)
public class TagLoaderMixin<T> {

    @Inject(at = @At("HEAD"), method = "load(Lnet/minecraft/server/packs/resources/ResourceManager;)Ljava/util/Map;", cancellable = true)
    public void injectLoad(ResourceManager manager, CallbackInfoReturnable<Map<ResourceLocation, List<TagLoader.EntryWithSource>>> cir) {
        var accessor = (TagLoaderAccessor) this;
        cir.setReturnValue(AdvancedTagLoader.load(manager, accessor));
    }

    @Inject(at = @At("HEAD"), method = "build(Lnet/minecraft/tags/TagEntry$Lookup;Ljava/util/List;)Lcom/mojang/datafixers/util/Either;", cancellable = true)
    public void injectBuild(TagEntry.Lookup<T> lookup, List<TagLoader.EntryWithSource> entries, CallbackInfoReturnable<Either<Collection<TagLoader.EntryWithSource>, Collection<T>>> cir) {
        cir.setReturnValue(AdvancedTagLoader.build(lookup, entries));
    }

}
