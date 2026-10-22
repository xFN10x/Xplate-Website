package dev.xplate.api.bedrockr.textures;

import com.google.gson.JsonObject;
import dev.xplate.api.bedrockr.data.SavedbedrockRData;
import dev.xplate.api.bedrockr.util.SamplesBlockTextureRef;
import dev.xplate.api.bedrockr.util.BRSamplesUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

///imma  try to make this without code from bedrockr
public class BlockTextures {
    
    protected static final Timer checkDownloadTimer = new Timer("check-block-textures-download");
    private static final Logger log = LogManager.getLogger(BlockTextures.class);

    static void main() throws IOException, InterruptedException {
    checkBlocks();    
    }
    
    static {
        checkDownloadTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    checkBlocks();
                } catch (IOException | InterruptedException e) {
                    log.error("Failed to check for block textures!", e);
                }
            }
        }, 1000L * 60 * 10, 10000L);
    }

    public static void downloadAllBlockTextures() throws IOException, InterruptedException {
        int maxThreads = 10;
        final ExecutorService executorService = Executors.newFixedThreadPool(maxThreads);
        final HashMap<String, SamplesBlockTextureRef> textures = BRSamplesUtil.getTextureNames();
        final CountDownLatch latch = new CountDownLatch(textures.size());
        HashMap<String, String> texturePaths = BRSamplesUtil.convertTextureNamesToFiles(textures.values().toArray(new SamplesBlockTextureRef[0]));
        for (Map.Entry<String, SamplesBlockTextureRef> entry : textures.entrySet()) {
            executorService.submit(() -> {
                try {
                    String name = entry.getKey();
                    SamplesBlockTextureRef ref = entry.getValue();

                    final BufferedImage top_all = BRSamplesUtil.getImage("resource_pack/"+texturePaths.get(ref.t));
                    latch.countDown();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
        latch.await();
    }
        public static void checkBlocks() throws IOException, InterruptedException {
        JsonObject json = BRSamplesUtil.jsonGET("https://api.github.com/repos/Mojang/bedrock-samples/releases/latest");
        long checkedID = json.get("id").getAsLong();
        Long lastId = SavedbedrockRData.Load().lastSamplesRelease;
        //if (lastId == null || lastId != checkedID) {
            downloadAllBlockTextures();
        //}
    }
    
}
