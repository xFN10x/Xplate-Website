package dev.xplate.api.bedrockr.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class BRSamplesUtil {
    
    protected final static HttpClient CLIENT = HttpClient.newBuilder().build();
    private static final Logger log = LogManager.getLogger(BRSamplesUtil.class);

    public static HttpRequest makeGET(String url) {
         return HttpRequest.newBuilder(URI.create(url))
                .GET().header("User-Agent", "Prismarine4J").build();
    }
    
    public static JsonObject jsonGET(String url) {
        try {
            HttpRequest req = makeGET(url);
            HttpResponse<String> res = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());
            return JsonParser.parseString(res.body()).getAsJsonObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static boolean hasAll(JsonObject obj, String... checks) {
        int amount = 0;
        for (String check : checks) {
            if (obj.has(check)) amount++;
        }
        return amount == checks.length;
    }

    public static JsonObject getJson(String path) throws IOException, InterruptedException {
        HttpRequest req = makeGET("https://raw.githubusercontent.com/Mojang/bedrock-samples/refs/heads/main/" + path);
        HttpResponse<String> res = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());
        return JsonParser.parseString(res.body()).getAsJsonObject();
    }

    public static BufferedImage getImage(String path) throws IOException, InterruptedException {
        HttpRequest req = makeGET("https://raw.githubusercontent.com/Mojang/bedrock-samples/refs/heads/main/" + path);
        HttpResponse<InputStream> res = CLIENT.send(req, HttpResponse.BodyHandlers.ofInputStream());
        return ImageIO.read(res.body());
    }
    
    protected static @Nullable BufferedImage downloadResPackImageOrNull(String path) {
        if (path == null) return null;
        
    }
    
    public static HashMap<String, String> convertTextureNamesToFiles(SamplesBlockTextureRef... refs) throws IOException, InterruptedException {
        JsonObject terrainTex = getJson("resource_pack/textures/terrain_texture.json");
        HashMap<Object, Object> build = new HashMap<>();
        for (SamplesBlockTextureRef ref : refs) {
            
        }
    }
    
    ///@return A map, the key being the block id, and value being a BlockTexture object determining how the block texture will be rendered
    public static HashMap<String, SamplesBlockTextureRef> getTextureNames() {
        try {
            JsonObject blocks = getJson("resources/blocks.json");
            HashMap<String, SamplesBlockTextureRef> building = new HashMap<>();
            for (Map.Entry<String, JsonElement> entry : blocks.entrySet()) {
                String key = entry.getKey();
                JsonObject val = entry.getValue().getAsJsonObject();
                if (val.isJsonPrimitive()) {
                    building.put(key, new SamplesBlockTextureRef(
                            val.getAsString(), false
                    ));
                    continue;
                }
                //log type
                if (hasAll(val, "side", "up", "down")) {
                    building.put(key, new SamplesBlockTextureRef(
                            val.get("side").getAsString(),
                            val.get("top").getAsString(),
                            val.get("bottom").getAsString()
                    ));
                } else if (hasAll(val, "north", "up", "down", "south", "east", "west")) {
                    building.put(key, new SamplesBlockTextureRef(
                            val.get("top").getAsString(),
                            val.get("bottom").getAsString(),
                            val.get("north").getAsString(),
                            val.get("south").getAsString(),
                            val.get("east").getAsString(),
                            val.get("west").getAsString()
                    ));
                } else {
                    building.put(key, new SamplesBlockTextureRef(
                            val.entrySet().iterator().next().getValue().getAsString(),
                            true
                    ));
                }
            }
            return building;
        } catch (Exception e) {
            log.error("Failed to get textures from samples!", e);
            return new HashMap<>();
        }
    }
}
