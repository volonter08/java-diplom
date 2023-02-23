package ru.netology.graphics.image;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;

public class MyTextGraphicsConverter implements TextGraphicsConverter {
    private TextColorSchema schema;
    private int maxWidth;
    private int maxHeight;
    private double maxRatio;

    public MyTextGraphicsConverter() {
        this.schema = new MyTextColorSchema();
        this.maxHeight = Integer.MAX_VALUE;
        this.maxWidth = Integer.MAX_VALUE;
        this.maxRatio = Integer.MAX_VALUE;
    }

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));
        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();
        double ratio = (double) imgWidth / imgHeight;
        if (ratio > this.maxRatio) {
            throw new BadImageSizeException(ratio, maxRatio);
        }
        if (imgWidth > this.maxWidth) {
            imgWidth = this.maxWidth;
            imgHeight = (int) (imgWidth / ratio);
        }
        if (imgHeight > this.maxHeight) {
            imgHeight = this.maxHeight;
            imgWidth = (int) (this.maxHeight * ratio);
        }
        Image scaleImage = img.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);
        BufferedImage newImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = newImg.createGraphics();
        graphics.drawImage(scaleImage, 0, 0, null);
        WritableRaster writableRaster = newImg.getRaster();
        StringBuilder stringBuilder = new StringBuilder();
        for (int h = 0; h < imgHeight; h++) {
            for (int w = 0; w < imgWidth; w++) {
                int color = writableRaster.getPixel(w, h, new int[3])[0];
                char c = schema.convert(color);
                stringBuilder.append(new char[]{c, c});
            }
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }
}
