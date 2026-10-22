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
import java.util.Objects;

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
        String bod = res.body();
        log.info("Got JSON " + path + ", " + bod.substring(0, Math.min(50, bod.length())));
        return JsonParser.parseString(bod).getAsJsonObject();
    }

    public static BufferedImage getImage(String path) throws IOException, InterruptedException {
        HttpRequest req = makeGET("https://raw.githubusercontent.com/Mojang/bedrock-samples/refs/heads/main/" + path);
        HttpResponse<InputStream> res = CLIENT.send(req, HttpResponse.BodyHandlers.ofInputStream());
        return ImageIO.read(res.body());
    }
    
    
    ///Takes BlockTextureRefs, and compile the textures inside of them into a map, which holds the name of the texture from inside the blocktextureref, (key,) and then the path reletive to "/resource_pack/" in the samples repo.
    public static HashMap<String, String> convertTextureNamesToFiles(SamplesBlockTextureRef... refs) throws IOException, InterruptedException {
        JsonObject terrainTex = getJson("resource_pack/textures/terrain_texture.json");
        JsonObject textureData = terrainTex.get("texture_data").getAsJsonObject();
        HashMap<String, String> building = new HashMap<>();
        for (SamplesBlockTextureRef ref : refs) {
            JsonElement textureObj = textureData.get(ref.t);
            /*
            "textures": [
                "textures/blocks/grass_top",
                "textures/blocks/grass_top",
             ...
            ]
             */
            if (textureObj.isJsonArray()) building.put(ref.t, textureObj.getAsJsonArray().get(0).getAsString());
           
            /*
            "textures": [
                {
                 "path": "textures/blocks/grass_side",
                 "overlay_color": "#df6827" //TODO: implement tint
               }
            ]
             */
            if (textureObj.isJsonObject()) building.put(ref.t, textureObj.getAsJsonObject().get("path").getAsString());
           
            /*
            "textures": "textures/blocks/grass_carried"
             */
            if (textureObj.isJsonPrimitive()) building.put(ref.t, textureObj.getAsString());
        }
        //TODO: download these now
        return building;
    }
    
    ///@return A map, the key being the block id, and value being a BlockTexture object determining how the block texture will be rendered
    public static HashMap<String, SamplesBlockTextureRef> getTextureNames() {
        try {
            JsonObject blocks = getJson("resource_pack/blocks.json");
            HashMap<String, SamplesBlockTextureRef> building = new HashMap<>();
            for (Map.Entry<String, JsonElement> entry : blocks.entrySet()) {
                String key = entry.getKey();
                if (key.equals("format_version")) continue;
                log.info("Getting texture names... {}", entry.getKey());
                JsonObject fullval = entry.getValue().getAsJsonObject();
                if (!fullval.has("textures")) continue;
                JsonElement val = fullval.get("textures");
                if (val.isJsonPrimitive()) {
                    building.put(key, new SamplesBlockTextureRef(
                            val.getAsString(), false
                    ));
                    continue;
                } else if (val instanceof JsonObject obj)
                //log type
                if (hasAll(obj, "side", "up", "down")) {
                    building.put(key, new SamplesBlockTextureRef(
                            obj.get("side").getAsString(),
                            obj.get("up").getAsString(),
                            obj.get("down").getAsString()
                    ));
                } else if (hasAll(obj, "north", "up", "down", "south", "east", "west")) {
                    building.put(key, new SamplesBlockTextureRef(
                            obj.get("up").getAsString(),
                            obj.get("down").getAsString(),
                            obj.get("north").getAsString(),
                            obj.get("south").getAsString(),
                            obj.get("east").getAsString(),
                            obj.get("west").getAsString()
                    ));
                } else {
                    building.put(key, new SamplesBlockTextureRef(
                            obj.entrySet().iterator().next().getValue().getAsString(),
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
