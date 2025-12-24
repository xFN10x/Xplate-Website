package dev.xplate.api.bedrockr;

import java.nio.file.Path;

import dev.xplate.api.util.SavedFile;

public class SavedbedrockRData extends SavedFile {

    @Override
    public Path saveLocation() {
        return getDataFolder().resolve("bedrockRData.json");
    }

}
