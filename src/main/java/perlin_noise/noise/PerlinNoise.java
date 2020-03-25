// SPDX-FileCopyrightText: 2020 Theo Dedeken
//
// SPDX-License-Identifier: MIT

package perlin_noise.noise;

import javafx.scene.image.PixelWriter;
import perlin_noise.math.Vector2D;

import java.util.Random;

/**
 * Created by theod on 18-12-2016.
 */
public class PerlinNoise {

    private int xDef;
    private int yDef;
    private PerlinNoiseNode[] nodes;
    private Random random;

    public PerlinNoise(int xDef, int yDef) {
        this.xDef = xDef;
        this.yDef = yDef;
        random = new Random();
        nodes = new PerlinNoiseNode[xDef * yDef];
        for (int i = 0; i < xDef * yDef; i++) {
            nodes[i] = new PerlinNoiseNode(random.nextDouble() * 2 * Math.PI);
        }
    }

    public void generateNoise(PixelWriter writer, int width, int height) {
        double[] values = generateNoise(width, height);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int colorcomp = (int) (255 * values[i + j * width]);
                int color = 0xff000000 + (colorcomp << 16) + (colorcomp << 8) + colorcomp;
                writer.setArgb(i, height - j - 1, color);
            }
        }
    }

    public double[] generateNoise(int width, int height) {
        double[] values = new double[width * height];
        double defWidth = (double) width / (xDef - 1);
        double defHeight = (double) height / (yDef - 1);
        double normaliser = 1 / Math.sqrt((defWidth * defWidth) + (defHeight * defHeight));
        for (int i = 0; i < xDef; i++) {
            for (int j = 0; j < yDef; j++) {
                nodes[i + j * xDef].setPos(new Vector2D(i * defWidth, j * defHeight));
                nodes[i + j * xDef].setNormaliser(normaliser);
            }
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Vector2D pos = new Vector2D(i, j);
                int xLeft = (int) (i / defWidth);
                int xRight = xLeft + 1;
                int yBot = (int) (j / defHeight);
                int yTop = yBot + 1;
                // g2 | g4
                // ---------
                // g1 | g3
                double g1 = nodes[xLeft + yBot * xDef].getInfluence(pos);
                double g2 = nodes[xLeft + yTop * xDef].getInfluence(pos);
                double g3 = nodes[xRight + yBot * xDef].getInfluence(pos);
                double g4 = nodes[xRight + yTop * xDef].getInfluence(pos);
                Vector2D uv = pos.minus(nodes[xLeft + yBot * xDef].getPos());
                uv.setX(uv.getX() / defWidth);
                uv.setY(uv.getY() / defHeight);
                uv.fadePerlin();
                // uv.fadeStep(5);
                // uv.fadefunction();
                double x1 = lerp(g1, g3, uv.getX());
                double x2 = lerp(g2, g4, uv.getX());
                double influence = lerp(x1, x2, uv.getY());
                values[i + j * width] = (influence + 1) / 2;
            }
        }
        return values;
    }

    public double lerp(double a, double b, double value) {
        double dist = b - a;
        return a + dist * value;
    }
}
