package dev.xplate.api.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public abstract class SavedFile {
    public static Path getDataFolder() {
        return Path.of(System.getProperty("user.home"), "XplateAPI");
    }

    protected transient Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public SavedFile load() throws JsonSyntaxException, IOException {
        return gson.fromJson(Files.readString(saveLocation()), getClass());
    }

    public void save() throws IOException {
        Files.writeString(saveLocation(), gson.toJson(this), StandardOpenOption.CREATE, StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }

    abstract public Path saveLocation();

}
