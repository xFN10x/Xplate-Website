package dev.xplate.api.bedrockr.util;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.awt.image.BufferedImage;

public record SamplesBlockTextures(
        @Nonnull BufferedImage top_all,
        @Nullable BufferedImage bottom,
        @Nullable BufferedImage north_side,
        @Nullable BufferedImage south,
        @Nullable BufferedImage east,
        @Nullable BufferedImage west,
        SamplesBlockTextureRef.BlockTextureType type
) {
    
}
