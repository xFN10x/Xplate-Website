package dev.xplate.api.bedrockr.util;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class SamplesBlockTextureRef {

    public @Nullable String side = null;
    public final @Nonnull String t;
    public @Nullable String d = null;
    public @Nullable String n = null;
    public @Nullable String s = null;
    public @Nullable String e = null;
    public @Nullable String w = null;
    
    public enum BlockTextureType {
        SINGLE,
        SINGLE_ALT,
        LOG,
        SIX_SIDED
    }
    
    public final BlockTextureType type;

    public SamplesBlockTextureRef(String singleTex, boolean alt) {
        this.t = singleTex;
        this.type = alt ? BlockTextureType.SINGLE_ALT : BlockTextureType.SINGLE;
    }

    public SamplesBlockTextureRef(String side, String top, String bottom) {
        this.side = side;
        this.t = top;
        this.d = bottom;
        this.type = BlockTextureType.LOG;
    }

    public SamplesBlockTextureRef(String top, String bottom, String north, String south, String east, String west) {
        this.t = top;
        this.d = bottom;
        this.n = north;
        this.s = south;
        this.e = east;
        this.w = west;
        this.type = BlockTextureType.SIX_SIDED;
    }
}
