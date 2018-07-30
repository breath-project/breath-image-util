package com.undancer.util.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ImageUtils {

    public static BufferedImage readBufferedImage(String filename) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream is = classLoader.getResourceAsStream(filename);
        BufferedImage bufferedImage = null;
        try (is) {
            bufferedImage = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bufferedImage;
    }

    public static BufferedImage readBufferedImage(Path imagePath) {
        BufferedImage bufferedImage = null;
        if (Files.exists(imagePath)) {
            try (InputStream is = Files.newInputStream(imagePath)) {
                bufferedImage = ImageIO.read(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bufferedImage;
    }

    public static void save(BufferedImage image, String name) {
        String formatName = "PNG";
//        Iterator<ImageWriter> iterable = ImageIO.getImageWritersByFormatName("PNG");
//        ImageWriter writer = Iterators.getOnlyElement(iterable);
//        ImageWriteParam param = writer.getDefaultWriteParam();
//        param.setCompressionQuality(1.00f);
//        param.setCompressionMode(ImageWriteParam.MODE_COPY_FROM_METADATA);

        try {
//            File file = new File(name + ".png");
//            FileImageOutputStream output = new FileImageOutputStream(file);
//            writer.setOutput(output);
//            IIOImage iioImage = new IIOImage(image, null, null);
//            writer.write(null, iioImage, param);

            ImageIO.write(image, formatName, new File(name + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
