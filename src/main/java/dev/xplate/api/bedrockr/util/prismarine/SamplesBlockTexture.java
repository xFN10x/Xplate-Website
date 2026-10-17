package dev.xplate.api.bedrockr.util.prismarine;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class SamplesBlockTexture {

    public @Nullable String side;
    public @Nullable String t;
    public @Nullable String d;
    public @Nonnull String n;
    public @Nullable String s;
    public @Nullable String e;
    public @Nullable String w;

    public SamplesBlockTexture(String singleTex) {
        this.n = singleTex;
    }

    public SamplesBlockTexture(String side, String top, String bottom) {
        this.side = side;
        this.t = top;
        this.d = bottom;
    }

    public SamplesBlockTexture(String top, String bottom, String north, String south, String east, String west) {
        this.t = top;
        this.d = bottom;
        this.n = north;
        this.s = south;
        this.e = east;
        this.w = west;
    }
}
