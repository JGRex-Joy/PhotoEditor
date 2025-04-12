package com.example.imageproject;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.*;

import java.awt.image.BufferedImage;

public class ProcessorOfImage {

    public static Image processImage(Image image, String effect) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        if (effect.equalsIgnoreCase("Blurring")) {
            return blur(image);
        }

        PixelReader pixelReader = image.getPixelReader();
        RGB_data[][] pixels = new RGB_data[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = pixelReader.getArgb(x, y);

                int red = (argb >> 16) & 0xff;
                int green = (argb >> 8) & 0xff;
                int blue = (argb) & 0xff;

                RGB_data pixel;

                if (effect.equalsIgnoreCase("Grayscale")) {
                    pixel = grayscale(red, green, blue);

                } else if (effect.equalsIgnoreCase("Inverse")) {
                    pixel = inverse(red, green, blue);

                } else {
                    pixel = new RGB_data(red, green, blue);
                }

                pixels[y][x] = pixel;
            }
        }

        WritableImage writableImage = new WritableImage(width, height);
        PixelWriter pixelWriter = writableImage.getPixelWriter();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                RGB_data pixel = pixels[y][x];
                int argb = (0xff << 24) | (pixel.getRed() << 16) | (pixel.getGreen() << 8) | pixel.getBlue();
                pixelWriter.setArgb(x, y, argb);
            }
        }
        return writableImage;
    }

    public static RGB_data grayscale(int red, int green, int blue) {
        int average = (red + green + blue) / 3;
        return new RGB_data(average, average, average);
    }

    public static RGB_data inverse(int red, int green, int blue) {
        int inverseRed = 255 - red;
        int inverseGreen = 255 - green;
        int inverseBlue = 255 - blue;
        return new RGB_data(inverseRed, inverseGreen, inverseBlue);
    }

    public static Image blur(Image image) {
        BufferedImage bI = SwingFXUtils.fromFXImage(image, null);
        System.out.println(bI + " blur buffered");

        BufferedImage bufferedImage = new BufferedImage(bI.getWidth(), bI.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int y = 2; y < bI.getHeight() - 2; y++) {
            for (int x = 2; x < bI.getWidth() - 2; x++) {
                int red = 0, green = 0, blue = 0;

                for (int dy = -2; dy <= 2; dy++) {
                    for (int dx = -2; dx <= 2; dx++) {
                        int rgb = bI.getRGB(x + dx, y + dy);
                        red += (rgb >> 16) & 0xff;
                        green += (rgb >> 8) & 0xff;
                        blue += rgb & 0xff;
                    }
                }

                red /= 25;
                green /= 25;
                blue /= 25;

                int alpha = (bI.getRGB(x, y) >> 24) & 0xff;
                int p = (alpha << 24) | (red << 16) | (green << 8) | blue;

                bufferedImage.setRGB(x, y, p);
            }
        }

        Image img_af = SwingFXUtils.toFXImage(bufferedImage, null);
        return img_af;
    }

    private static Image gaussianBlur(Image image) {
        int radius = 5;
        double sigma = 3.0;

        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        PixelReader reader = image.getPixelReader();
        WritableImage output = new WritableImage(width, height);
        PixelWriter writer = output.getPixelWriter();

        double[][] kernel = createGaussianKernel(radius, sigma);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double r = 0, g = 0, b = 0;
                double totalWeight = 0.0;

                for (int dy = -radius; dy <= radius; dy++) {
                    for (int dx = -radius; dx <= radius; dx++) {
                        int ix = x + dx;
                        int iy = y + dy;

                        if (ix < 0 || ix >= width || iy < 0 || iy >= height)
                            continue;

                        int argb = reader.getArgb(ix, iy);
                        int red = (argb >> 16) & 0xff;
                        int green = (argb >> 8) & 0xff;
                        int blue = argb & 0xff;

                        double weight = kernel[dy + radius][dx + radius];

                        r += red * weight;
                        g += green * weight;
                        b += blue * weight;
                        totalWeight += weight;
                    }
                }

                int ir = Math.min(255, Math.max(0, (int) Math.round(r / totalWeight)));
                int ig = Math.min(255, Math.max(0, (int) Math.round(g / totalWeight)));
                int ib = Math.min(255, Math.max(0, (int) Math.round(b / totalWeight)));

                int newArgb = (0xff << 24) | (ir << 16) | (ig << 8) | ib;
                writer.setArgb(x, y, newArgb);
            }
        }

        return output;
    }
    private static double[][] createGaussianKernel(int radius, double sigma) {
        int size = 2 * radius + 1;
        double[][] kernel = new double[size][size];
        double sum = 0.0;

        for (int y = -radius; y <= radius; y++) {
            for (int x = -radius; x <= radius; x++) {
                double exponent = -(x * x + y * y) / (2 * sigma * sigma);
                kernel[y + radius][x + radius] = Math.exp(exponent);
                sum += kernel[y + radius][x + radius];
            }
        }

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                kernel[y][x] /= sum;
            }
        }

        return kernel;

        // здесь все работает с Божьей помощью
    }
}
