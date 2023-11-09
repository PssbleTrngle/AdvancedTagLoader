package com.possible_triangle.advanced_tag_loader;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.tags.TagEntry;
import net.minecraft.util.StringRepresentable;

import java.util.List;
import java.util.Optional;

import static com.possible_triangle.advanced_tag_loader.TagModificationAction.*;

public record AdvancedTagFile(List<TagEntry> entries, TagModificationAction action) {

    public static final Codec<AdvancedTagFile> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    TagEntry.CODEC.listOf().fieldOf("values").forGetter(AdvancedTagFile::entries),
                    StringRepresentable.fromEnum(TagModificationAction::values).optionalFieldOf("action").forGetter(it -> Optional.of(it.action)),
                    Codec.BOOL.optionalFieldOf("replace", false).forGetter(it -> it.action == REPLACE)
            ).apply(builder, (entries, optionalAction, replace) -> {
                var action = optionalAction.orElse(replace ? REPLACE : ADD);
                return new AdvancedTagFile(entries, action);
            })
    );

}
