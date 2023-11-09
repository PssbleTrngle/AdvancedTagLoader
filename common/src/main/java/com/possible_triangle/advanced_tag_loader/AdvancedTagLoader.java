package com.possible_triangle.advanced_tag_loader;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.JsonOps;
import com.possible_triangle.advanced_tag_loader.mixin.TagLoaderAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagLoader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AdvancedTagLoader {

    private static ResourceLocation trimTagId(ResourceLocation fileId, String directory) {
        return new ResourceLocation(fileId.getNamespace(), fileId.getPath().substring(directory.length() + 1, fileId.getPath().length() - 5));
    }

    public static Map<ResourceLocation, List<TagLoader.EntryWithSource>> load(ResourceManager manager, TagLoaderAccessor accessor) {

        var builder = new ImmutableMap.Builder<ResourceLocation, List<TagLoader.EntryWithSource>>();

        var definitions = manager.listResourceStacks(accessor.getDirectory(), it -> it.getPath().endsWith(".json"));

        definitions.forEach((fileId, resources) -> {
            var id = trimTagId(fileId, accessor.getDirectory());
            var entries = loadResource(accessor, fileId, resources.stream());
            builder.put(id, entries.toList());
        });

        return builder.build();
    }

    private static String encodeSource(String source, AdvancedTagFile file) {
        return source + ":" + file.action();
    }

    private static TagModificationAction decodeSource(String source) {
        if (!source.contains(":")) throw new IllegalStateException("Un-encoded source: '%s'".formatted(source));
        return TagModificationAction.valueOf(source.substring(source.lastIndexOf(":") + 1));
    }

    public static Stream<TagLoader.EntryWithSource> loadResource(TagLoaderAccessor accessor, ResourceLocation fileId, Stream<Resource> resources) {
        var id = trimTagId(fileId, accessor.getDirectory());

        return resources.<List<TagLoader.EntryWithSource>>reduce(new ArrayList<>(), (previous, resource) -> {
            var source = resource.sourcePackId();

            try (var reader = resource.openAsReader()) {

                var json = JsonParser.parseReader(reader);
                var result = AdvancedTagFile.CODEC.parse(JsonOps.INSTANCE, json);
                var tag = result.getOrThrow(false, Constants.LOGGER::error);


                var next = tag.entries().stream().map(it -> new TagLoader.EntryWithSource(it, encodeSource(source, tag)));
                if (tag.action() == TagModificationAction.REPLACE) return next.toList();

                next.forEach(previous::add);
                return previous;

            } catch (Throwable ex) {
                Constants.LOGGER.error("Couldn't read tag list {} from {} in data pack {}", id, fileId, source, ex);
                return previous;
            }
        }, (a, b) -> {
            a.addAll(b);
            return a;
        }).stream();
    }

    public static <T> Either<Collection<TagLoader.EntryWithSource>, Collection<T>> build(TagEntry.Lookup<T> lookup, List<TagLoader.EntryWithSource> entries) {
        List<TagLoader.EntryWithSource> missingEntries = new ArrayList<>();

        var byAction = new HashMap<TagModificationAction, List<T>>();

        for (TagLoader.EntryWithSource it : entries) {
            TagEntry entry = it.entry();
            var action = decodeSource(it.source());
            var list = byAction.computeIfAbsent(action, $ -> new ArrayList<>());

            if (!entry.build(lookup, list::add)) {
                missingEntries.add(it);
            }
        }

        if(!missingEntries.isEmpty()) return Either.left(missingEntries);

        var removed = byAction.getOrDefault(TagModificationAction.REMOVE, List.of());
        var added = byAction.getOrDefault(TagModificationAction.ADD, List.of());

        var values = added.stream().filter(it -> !removed.contains(it)).collect(Collectors.toSet());

        return Either.right(values);
    }

}
