package com.undancer.util.image;

import com.jhlabs.image.GrayscaleFilter;
import com.jhlabs.image.PointFilter;
import org.apache.commons.lang3.BitField;
import org.apache.commons.lang3.BooleanUtils;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

public class App {


    final static class BitPlaneFilter extends PointFilter {

        private int bitPlane = 0;

        public BitPlaneFilter(int bitPlane) {
            this.bitPlane = bitPlane;
        }

//        		return pixelA & 0xFFF8F8F8 | (pixelB & 0x00E0E0E0) >> 5;


        public int filterRGB(int x, int y, int rgb) {
            int gray = rgb & 0xFF;
            BitField bit = new BitField(0b1 << bitPlane);
            boolean fill = BooleanUtils.toBoolean(bit.getValue(gray));
//            System.out.println(Long.toBinaryString(0xFFF8F8F8));
//            System.out.println(Long.toBinaryString(0x00E0E0E0));
//            System.out.println(Long.toBinaryString(0xFF070707));
//            System.exit(0);
            if (fill) {
                return 0xFFFFFF;
            }
            return 0x000000;
        }
    }

    public static BufferedImage bitPlaneImage(BufferedImage inputImage, int plane) {
        BufferedImage bufferedImage = null;
        BufferedImageOp op = new BitPlaneFilter(plane);
        bufferedImage = op.filter(inputImage, null);
        return bufferedImage;
    }

    public static void main(String[] args) {
        BufferedImage sourceImage = ImageUtils.readBufferedImage("果仁.jpg");
        if (sourceImage != null) {
            BufferedImageOp op = new GrayscaleFilter();
            BufferedImage grayScaleImage = op.filter(sourceImage, null);

            for (int n = 0; n < 8; n++) {
                BufferedImage targetImage = bitPlaneImage(grayScaleImage, n);

                ImageUtils.save(targetImage, String.format("target-%d", n));

            }
        }


    }

}
