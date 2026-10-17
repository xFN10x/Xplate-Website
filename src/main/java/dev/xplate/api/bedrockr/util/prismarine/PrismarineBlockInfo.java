package dev.xplate.api.bedrockr.util.prismarine;

import java.util.Map;

public class PrismarineBlockInfo implements Comparable<PrismarineBlockInfo> {
    public int id;
    public String name;
    public String displayName;
    public float hardness;
    public float resistance;
    public int stackSize;
    public boolean diggable;
    public String material;
    public boolean transparent;
    public int emitLight;
    public int filterLight;
    public int defaultState;
    public int minStateId;
    public int maxStateId;
    public Map<String, Boolean> harvestTools;
    public int[] drops;
    public String boundingBox;

    @Override
    public int compareTo(PrismarineBlockInfo o) {
        return displayName.compareToIgnoreCase(o.displayName);
    }
}
