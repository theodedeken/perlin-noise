package perlin_noise.noise;

import javafx.scene.image.PixelWriter;

/**
 * Created by theod on 19-12-2016.
 */
public class PerlinNoiseOctaved {
    private PerlinNoise[] perlins;

    public PerlinNoiseOctaved(int xDef, int yDef, int octaves) {
        int xDeff = xDef;
        int yDeff = yDef;
        perlins = new PerlinNoise[octaves];
        for (int i = 0; i < octaves; i++) {
            perlins[i] = new PerlinNoise(xDeff, yDeff);
            xDeff *= 2;
            yDeff *= 2;
        }
    }

    public double[] generateNoise(int width, int height) {
        double[][] noiseLevels = new double[perlins.length][];
        double[] values = new double[width * height];
        for (int i = 0; i < perlins.length; i++) {
            noiseLevels[i] = perlins[i].generateNoise(width, height);
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                double value = 0;
                double factor = 0.5;
                for (int k = 0; k < perlins.length; k++) {
                    value += factor * noiseLevels[k][i + j * width];
                    factor /= 2;
                }
                values[i + j * width] = value;
            }
        }
        values = minmax(values);
        // values = quad(values);
        // writeValues(writer, values, width, height);
        return values;
    }

    public void generateNoise(PixelWriter writer, int width, int height) {
        double[] values = generateNoise(width, height);
        writeHeight(writer, values, width, height);
    }

    public double[] minmax(double[] values) {
        double min = 1;
        double max = 0;
        for (int i = 0; i < values.length; i++) {
            double val = values[i];
            if (val < min) {
                min = val;
            }
            if (val > max) {
                max = val;
            }
        }
        double norm = 1 / (max - min);
        for (int i = 0; i < values.length; i++) {
            values[i] = interp(min, norm, values[i]);
        }
        return values;
    }

    public double[] quad(double[] values) {
        for (int i = 0; i < values.length; i++) {
            values[i] = values[i] * values[i];
        }
        return values;
    }

    private double interp(double min, double norm, double value) {
        return (value - min) * norm;
    }

    public void writeValues(PixelWriter writer, double[] values, int width, int height) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                double value = values[i + j * width];
                int colorcomp = (int) (255 * value);
                int color = 0xff000000 + (colorcomp << 16) + (colorcomp << 8) + colorcomp;
                writer.setArgb(i, height - j - 1, color);
            }
        }
    }

    public void writeHeight(PixelWriter writer, double[] values, int width, int height) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                double value = values[i + j * width];
                int color;
                if (value < .10) {
                    double factor = value * 10;
                    int colorcomp = (int) (255 * factor);
                    color = 0xff000000 + colorcomp;
                } else if (value < .60) {
                    double factor = 1 - (value - .10);
                    int colorcomp = (int) (255 * factor);
                    color = 0xff000000 + (colorcomp << 8);
                } else {
                    double factor = (value - .60) + .25;
                    int colorcomp = (int) (255 * factor);
                    color = 0xff000000 + (colorcomp << 16) + (colorcomp << 8) + colorcomp;
                }
                writer.setArgb(i, height - j - 1, color);
            }
        }
    }
}
