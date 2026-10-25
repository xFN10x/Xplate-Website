package dev.xplate.api.bedrockr.textures;

import com.google.gson.JsonObject;
import dev.xplate.api.bedrockr.data.SavedbedrockRData;
import dev.xplate.api.bedrockr.textures.render.RenderHandler;
import dev.xplate.api.bedrockr.util.BRSamplesUtil;
import dev.xplate.api.bedrockr.util.SamplesBlockTextureRef;
import dev.xplate.api.bedrockr.util.SamplesBlockTextures;
import dev.xplate.api.util.SavedFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/// imma try to make this without code from bedrockr
public class BlockTextures {

    protected static final Timer checkDownloadTimer = new Timer("check-block-textures-download");
    private static final Logger log = LogManager.getLogger(BlockTextures.class);
    protected static volatile CountDownLatch CHECKING_BLOCKS_LATCH = new CountDownLatch(0);

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

    public static byte[] getBlockTexturesRaw() throws IOException, InterruptedException {
        if (CHECKING_BLOCKS_LATCH.getCount() != 0) {
            log.info("Someone tried to get block textures, but they are already being made.");
            CHECKING_BLOCKS_LATCH.await();
        }
        checkBlocks();

        return AtlasUtil.getAtlasRaw("block_textures");
    }

    public static void downloadAllBlockTextures() {
        int maxThreads = 15;
        Instant start = Instant.now();
        try (final ExecutorService executorService = Executors.newFixedThreadPool(maxThreads)) {
            final HashMap<String, SamplesBlockTextureRef> textures = BRSamplesUtil.getTextureNames();
            final CountDownLatch latch = new CountDownLatch(textures.size());
            HashMap<String, String> texturePaths = BRSamplesUtil
                    .convertTextureNamesToFiles(textures.values().toArray(new SamplesBlockTextureRef[0]));
            //Path texture_save_loc = SavedFile.getDataFolder().resolve("btextures/");
            //Files.createDirectories(texture_save_loc);
            final HashMap<String, BufferedImage> textures_map = new HashMap<>();
            log.info("Downloading block textures...");
            for (Map.Entry<String, SamplesBlockTextureRef> entry : textures.entrySet()) {
                executorService.submit(() -> {
                    try {
                        String name = entry.getKey();
                        SamplesBlockTextureRef ref = entry.getValue();
                        String fullPathT = "resource_pack/" + texturePaths.get(ref.t) + ".png";
                        String fullPathD = "resource_pack/" + texturePaths.get(ref.d) + ".png";
                        String fullPathN = "resource_pack/" + texturePaths.get(ref.n) + ".png";
                        String fullPathS = "resource_pack/" + texturePaths.get(ref.s) + ".png";
                        String fullPathE = "resource_pack/" + texturePaths.get(ref.e) + ".png";
                        String fullPathW = "resource_pack/" + texturePaths.get(ref.w) + ".png";
                        String fbfullPathT = "resource_pack/textures/blocks/" + ref.t + ".png";
                        String fbfullPathD = "resource_pack/textures/blocks/" + ref.d + ".png";
                        String fbfullPathN = "resource_pack/textures/blocks/" + ref.n + ".png";
                        String fbfullPathS = "resource_pack/textures/blocks/" + ref.s + ".png";
                        String fbfullPathE = "resource_pack/textures/blocks/" + ref.e + ".png";
                        String fbfullPathW = "resource_pack/textures/blocks/" + ref.w + ".png";
                        // log.debug("Getting buffered textures from {}. (path {})", name, fullPathT);

                        final BufferedImage top_all = BRSamplesUtil.getImage(fullPathT, fbfullPathT);
                        final BufferedImage down = BRSamplesUtil.getImage(fullPathD, fbfullPathD);
                        final BufferedImage north_side = BRSamplesUtil.getImage(fullPathN, fbfullPathN);
                        final BufferedImage south = BRSamplesUtil.getImage(fullPathS, fbfullPathS);
                        final BufferedImage east = BRSamplesUtil.getImage(fullPathE, fbfullPathE);
                        final BufferedImage west = BRSamplesUtil.getImage(fullPathW, fbfullPathW);
                        BufferedImage bufferedImage = RenderHandler.renderBlock(name,
                                new SamplesBlockTextures(top_all, down, north_side, south, east, west, ref.type));
                        if (top_all == null)
                            log.warn("Top/All is null?? for {}. Path: {}, Fallback: {}", name, fullPathT, fbfullPathT);
                        else
                            textures_map.put(name, bufferedImage);
                    } catch (Exception e) {
                        log.error("Failed to download block texture.", e);
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();
            AtlasUtil.makeAtlas("block_textures", textures_map);

            log.info("Finished downloading block textures in {}sec.", Duration.between(start, Instant.now()));

            JsonObject json = BRSamplesUtil
                    .jsonGET("https://api.github.com/repos/Mojang/bedrock-samples/releases/latest");
            long id = json.get("id").getAsLong();
            SavedbedrockRData data = SavedbedrockRData.Load();
            data.lastSamplesRelease = id;
            data.save();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void checkBlocks() throws IOException, InterruptedException {
        try {
            if (CHECKING_BLOCKS_LATCH.getCount() != 0)
                return;
            CHECKING_BLOCKS_LATCH = new CountDownLatch(1);
            JsonObject json = BRSamplesUtil
                    .jsonGET("https://api.github.com/repos/Mojang/bedrock-samples/releases/latest");
            long checkedID = json.get("id").getAsLong();
            Long lastId = SavedbedrockRData.Load().lastSamplesRelease;
            if (lastId == null || lastId != checkedID) {
                downloadAllBlockTextures();
            }
        } catch (Exception e) {
            throw e;
        } finally {
            CHECKING_BLOCKS_LATCH.countDown();
        }
    }

}
