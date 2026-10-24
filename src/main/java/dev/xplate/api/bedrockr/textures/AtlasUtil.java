package dev.xplate.api.bedrockr.textures;

import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.tag.CompoundTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.util.Tuple;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static dev.xplate.api.util.SavedFile.getDataFolder;
import static java.lang.Math.*;

public class AtlasUtil {
    
    private static final Path ATLAS_FOLDER = getDataFolder().resolve("atlas/");
    private static final Logger log = LoggerFactory.getLogger(AtlasUtil.class);

    public record Atlas(String name, BufferedImage img, HashMap<String, Tuple<Integer, Integer>> posMapping, HashMap<String, Tuple<Integer, Integer>> sizeMapping) {
        
        public void save() throws IOException {
            File saveloc = ATLAS_FOLDER.resolve(name + "-atlas.nbt").toFile();
            File imgsaveloc = ATLAS_FOLDER.resolve(name + "-atlas.png").toFile();
            
            ByteArrayOutputStream imgStream = new ByteArrayOutputStream();
            ImageIO.write(img, "png",imgStream);
            
            CompoundTag tag = new CompoundTag();
            tag.putInt("format", 1);
            tag.putByteArray("imgData", imgStream.toByteArray());

            CompoundTag posMappings = new CompoundTag();
            for (Map.Entry<String, Tuple<Integer, Integer>> entry : posMapping.entrySet()) {
                Tuple<Integer, Integer> val = entry.getValue();
                posMappings.putIntArray(entry.getKey(), new int[] {val._1(), val._2()});
            }

            CompoundTag sizeMappings = new CompoundTag();
            for (Map.Entry<String, Tuple<Integer, Integer>> entry : sizeMapping.entrySet()) {
                Tuple<Integer, Integer> val = entry.getValue();
                sizeMappings.putIntArray(entry.getKey(), new int[] {val._1(), val._2()});
            }

            tag.put("posMappings", posMappings);
            tag.put("sizeMappings", sizeMappings);
            NBTUtil.write(tag, saveloc);
            ImageIO.write(img, "png", imgsaveloc);
        }
    }
    
    public static byte[] getAtlasRaw(String name) throws IOException {
            Path path = ATLAS_FOLDER.resolve(name + "-atlas.nbt");
            if (!Files.exists(path)) {
                return null;
            } else {
                return Files.readAllBytes(path);
            }
    }
    
    public static Atlas makeAtlas(String name, HashMap<String, BufferedImage> images) {
        int maxSize = 0;
        for (BufferedImage image : images.values()) {
            maxSize += image.getHeight() * image.getWidth();
        }

        int dimension = closest2(Double.valueOf(Math.sqrt(maxSize)).intValue());
        int w = dimension;
        int h = dimension;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
        int lastX = 0;
        int colMaxW = 0;
        int lastY = 0;
        Graphics2D grap = img.createGraphics();
        HashMap<String, Tuple<Integer, Integer>> posMappings = new HashMap<>();
        HashMap<String, Tuple<Integer, Integer>> sizeMappings = new HashMap<>();
        for (Map.Entry<String, BufferedImage> entry : images.entrySet()) {
            BufferedImage image = entry.getValue();
            String imgName = entry.getKey();
            //Resize?
            if (lastX + image.getWidth() > img.getWidth()) {
                w = closest2((int) pow(ceil(Math.sqrt(w + (image.getWidth() - (img.getWidth() - lastX)))), 2));
                BufferedImage img2 = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
                img2.createGraphics().drawImage(img, 0, 0, null);
                img = img2;
                grap = (Graphics2D) img2.getGraphics();
                System.out.println("RESIZING");
            }
            //Check if the image will clip off the bottom, if it does, 
            if (lastY + image.getHeight() > h) {
                //then move to the next column.
                lastY = 0;
                lastX += colMaxW;
                colMaxW = 0;
            }
            colMaxW = max(colMaxW, image.getWidth());
            grap.drawImage(image, lastX, lastY, null);
            posMappings.put(imgName, new Tuple<>(lastX, lastY));
            sizeMappings.put(imgName, new Tuple<>(image.getWidth(), image.getHeight()));
            lastY += image.getHeight();
        }
        Atlas atlas = new Atlas(name, img, posMappings, sizeMappings);
        try {
            atlas.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return atlas;
    }

    public static int closest2(int x) {
        return (int) pow(2, ceil(log(x)/log(2)));
    }
}
