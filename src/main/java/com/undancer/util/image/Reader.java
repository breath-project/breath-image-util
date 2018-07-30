package com.undancer.util.image;

import com.google.zxing.*;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.GenericMultipleBarcodeReader;
import com.google.zxing.multi.MultipleBarcodeReader;
import org.apache.commons.lang3.BitField;
import org.apache.commons.lang3.BooleanUtils;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public class Reader {
    public static void main2(String[] args) {
        int v = 1;

        System.out.println(BooleanUtils.toBoolean(v));

        BooleanUtils.toBoolean(1, 2, 3);
        MultipleBarcodeReader f;
        GenericMultipleBarcodeReader reader = new GenericMultipleBarcodeReader(null);

        try {
            reader.decodeMultiple(null);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        BufferedImage bufferedImage = ImageUtils.readBufferedImage("果仁.jpg");
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        BitField field = new BitField(0x00000001);
        int[] inPixels = Arrays.stream(bufferedImage.getRGB(0, 0, width, height, null, 0, width))
                .map(operand -> field.getValue(operand) == 0x00000001 ? 0xFFFFFF : 0x000000)
                .toArray();

        MultiFormatReader multiFormatReader = new MultiFormatReader();
        GenericMultipleBarcodeReader reader = new GenericMultipleBarcodeReader(multiFormatReader);
        try {
            LuminanceSource luminanceSource = new RGBLuminanceSource(width, height, inPixels);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(luminanceSource));

            Result[] results = reader.decodeMultiple(bitmap);
            for (Result result : results) {
                System.out.println(result.getBarcodeFormat() + " -> " + result.getText());
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }
}
