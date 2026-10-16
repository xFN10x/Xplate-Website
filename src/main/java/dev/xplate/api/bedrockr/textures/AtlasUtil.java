package dev.xplate.api.bedrockr.textures;

import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import static dev.xplate.api.util.SavedFile.getDataFolder;
import static java.lang.Math.*;

public class AtlasUtil {

    static void main(String[] args) {
        JFrame jWindow = new JFrame("");
        jWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jFileChooser.setMultiSelectionEnabled(true);
        jFileChooser.showOpenDialog(null);

        ArrayList<BufferedImage> ary = new ArrayList<>();
        for (File selectedFile : jFileChooser.getSelectedFiles()) {
            try {
                ary.add(ImageIO.read(selectedFile));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        JLabel jLabel = new JLabel();
        jLabel.setIcon(new ImageIcon(makeAtlas("test",ary.toArray(new BufferedImage[0]))));
        //jWindow.setVisible(true);
    }

    public static BufferedImage makeAtlas(String name,BufferedImage... images) {
        int maxSize = 0;
        for (BufferedImage image : images) {

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
        for (BufferedImage image : images) {
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
            lastY += image.getHeight();
        }
        try {
            File imgOut = getDataFolder().resolve("atlas/" + name + "-atlas.png").toFile();
            FileUtils.createParentDirectories(imgOut);
            ImageIO.write(img, "png", imgOut);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return img;
    }

    public static int closest2(int x) {
        return (int) pow(2, ceil(log(x)/log(2)));
    }
}
