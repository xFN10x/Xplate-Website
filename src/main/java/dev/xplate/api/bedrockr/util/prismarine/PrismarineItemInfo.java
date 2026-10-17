package dev.xplate.api.bedrockr.util.prismarine;

public class PrismarineItemInfo implements Comparable<PrismarineItemInfo> {
    public int id;
    public int stackSize;
    public String name;
    public String displayName;

    @Override
    public int compareTo(PrismarineItemInfo o) {
        return displayName.compareToIgnoreCase(o.displayName);
    }
}
