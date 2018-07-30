package com.undancer.util.image;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

public class Main {

    public static Table<Integer, Integer, Boolean> readWaterMessage(String content) {
        Table<Integer, Integer, Boolean> table = HashBasedTable.create();

        BitMatrix matrix = readBitMatrix(content);
        if (matrix != null) {
            for (int y = 0; y < matrix.getHeight(); y++) {
                for (int x = 0; x < matrix.getWidth(); x++) {
                    table.put(x, y, matrix.get(x, y));
                }
            }
        }

        return table;
    }

    public static BitMatrix readBitMatrix(String content) {
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix matrix = null;
        try {
            matrix = writer.encode(content, BarcodeFormat.QR_CODE, 0, 0);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return matrix;
    }


    public static void main(String[] args) {

        String content = "undancer";
        BufferedImage image = ImageUtils.readBufferedImage("果仁.jpg");
        Table<Integer, Integer, Boolean> table = readWaterMessage(content);


//        for (String propertyName : image.getPropertyNames()) {
//            System.out.println(String.format("%s -> %s", propertyName, image.getProperty(propertyName)));
//        }


        image = filter(image, null, table);

        ImageUtils.save(image, "target");

    }

    public static BufferedImage filter(BufferedImage src, BufferedImage dst, Table<Integer, Integer, Boolean> water) {
        int width = src.getWidth();
        int height = src.getHeight();

        if (dst == null) {
            ColorModel dstCM = src.getColorModel();
            dst = new BufferedImage(dstCM, dstCM.createCompatibleWritableRaster(src.getWidth(), src.getHeight()), dstCM.isAlphaPremultiplied(), null);
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                Integer source = src.getRGB(x, y);
                boolean blacked = water.get(x, y) == null || water.get(x, y) == Boolean.TRUE;
                Integer target = blacked ? 0x000000 : 0x010101;

                int sourceMark = 0xFFF8F8F8;
                sourceMark = 0xFFFEFEFE;

                int sourceValue = source & sourceMark;
                int targetValue = (target & 0x00E0E0E0) >> 5;
                targetValue = target;

//                System.out.println(String.format("s:%S -> t:%S", Long.toHexString(sourceValue), Long.toHexString(targetValue)));

                int rgb = sourceValue | targetValue;

//                rgb = source;
//                rgb = target;

                dst.setRGB(x, y, rgb);
            }
        }

        return dst;
    }
}
