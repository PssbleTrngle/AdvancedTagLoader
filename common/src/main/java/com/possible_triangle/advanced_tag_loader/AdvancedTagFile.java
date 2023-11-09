package com.possible_triangle.advanced_tag_loader;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.tags.TagEntry;

import java.util.List;

public record AdvancedTagFile(List<TagEntry> values, List<TagEntry> remove, boolean replace) {

    public static final Codec<AdvancedTagFile> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    TagEntry.CODEC.listOf().optionalFieldOf("values", List.of()).forGetter(AdvancedTagFile::values),
                    TagEntry.CODEC.listOf().optionalFieldOf("remove", List.of()).forGetter(AdvancedTagFile::remove),
                    Codec.BOOL.optionalFieldOf("replace", false).forGetter(AdvancedTagFile::replace)
            ).apply(builder, AdvancedTagFile::new)
    );

}
