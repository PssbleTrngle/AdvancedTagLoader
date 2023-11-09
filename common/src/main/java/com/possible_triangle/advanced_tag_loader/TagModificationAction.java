package com.possible_triangle.advanced_tag_loader;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum TagModificationAction implements StringRepresentable {
    ADD, REMOVE, REPLACE;


    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
