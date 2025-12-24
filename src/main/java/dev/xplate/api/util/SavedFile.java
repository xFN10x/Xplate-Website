package dev.xplate.api.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public abstract class SavedFile {
    protected static Logger LOG = Logger.getGlobal();

    public static Path getDataFolder() {
        return Path.of(System.getProperty("user.home"), "XplateAPI");
    }

    protected transient Timer cleaningTimer = new Timer("clean" + getClass().getSimpleName());
    private transient TimerTask cleanTask = new TimerTask() {

        @Override
        public void run() {
            try {
                wash();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    };

    /**
     * When dirty, the file will automaticly be saved after a 2 second timer,
     * resetting when ever this becomes dirty.
     */
    protected transient boolean dirty = true;
    protected transient Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public SavedFile load() throws JsonSyntaxException, IOException {
        if (saveLocation().toFile().exists())
            return gson.fromJson(Files.readString(saveLocation()), getClass());
        else
            try {
                return getClass().getConstructor(null).newInstance(null);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
                return null;
            }
    }

    public void save() throws IOException {
        if (!saveLocation().toFile().exists()) {
            FileUtils.createParentDirectories(saveLocation().toFile());
            saveLocation().toFile().createNewFile();
        }
        Files.writeString(saveLocation(), gson.toJson(this), StandardOpenOption.CREATE, StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }

    /**
     * When dirty, the file will automaticly be saved after a 2 second timer,
     * resetting when ever this becomes dirty.
     */
    public void dirty() {
        if (cleanTask.scheduledExecutionTime() != 0)
            cleanTask.cancel();
        cleaningTimer.schedule(cleanTask, 2000l);
        dirty = true;
    }

    public void wash() throws IOException {
        save();
        dirty = false;
        LOG.info("Washed " + getClass().getSimpleName() + " to location: " + saveLocation().toString());
    }

    abstract public Path saveLocation();

}
