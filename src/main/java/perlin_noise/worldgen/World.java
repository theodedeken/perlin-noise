// SPDX-FileCopyrightText: 2020 Theo Dedeken
//
// SPDX-License-Identifier: MIT

package perlin_noise.worldgen;

import javafx.scene.image.PixelWriter;
import perlin_noise.noise.PerlinNoiseOctaved;

import java.util.*;

/**
 * Created by theod on 2-1-2017.
 */
public class World {

    private int xDef;
    private int yDef;
    private int octaves;
    private double rivers;
    private double forestation;
    private WorldTile[] world;
    private int width;
    private int height;
    private int[][] dirlist = { { 1, 1 }, { 1, 0 }, { 1, -1 }, { 0, 1 }, { 0, -1 }, { -1, 1 }, { -1, 0 }, { -1, -1 } };

    public World(int xDef, int yDef, int octaves, double rivers, double forestation) {
        this.xDef = xDef;
        this.yDef = yDef;
        this.octaves = octaves;
        this.rivers = rivers;
        this.forestation = forestation;
    }

    public void generate(int width, int height) {
        this.height = height;
        this.width = width;
        this.world = new WorldTile[width * height];
        generateHeights();
        generateForestation();
        // generateSources();
    }

    public void generateHeights() {
        PerlinNoiseOctaved heights = new PerlinNoiseOctaved(xDef, yDef, octaves);
        double[] values = heights.generateNoise(width, height);
        for (int i = 0; i < width * height; i++) {
            world[i] = new WorldTile(i % width, i / width, (int) (values[i] * 255), false, false);
        }
    }

    public void generateForestation() {
        PerlinNoiseOctaved forests = new PerlinNoiseOctaved(xDef * octaves / 2, yDef * octaves / 2, octaves / 2);
        double[] values = forests.generateNoise(width, height);
        for (int i = 0; i < width * height; i++) {
            if (values[i] > forestation) {
                world[i].setForest(true);
            }
        }
    }

    public void generateSources() {

        List<WorldTile> candidates = new ArrayList<>();
        for (int i = 0; i < width * height; i++) {
            if (world[i].getHeight() >= 224) {
                candidates.add(world[i]);
            }
        }
        int amount = (int) (rivers * candidates.size());
        List<WorldTile> picked = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < amount; i++) {
            picked.add(candidates.get(r.nextInt(candidates.size())));
        }
        generateRivers(picked);

    }

    public void generateRivers(List<WorldTile> sources) {

        for (WorldTile source : sources) {
            generateRiver(source);
        }
    }

    public void generateRiver(WorldTile source) {

        WorldTile[] nbrs = getNeighbours(source);
        Set<WorldTile> river = new HashSet<>();
        WorldTile next = nextFlow(nbrs, source, river);
        river.add(source);
        while (next != null) {
            river.add(next);
            next = nextFlow(getNeighbours(next), next, river);
        }
        for (WorldTile riv : river) {
            riv.setRiver(true);
        }
    }

    public WorldTile nextFlow(WorldTile[] neighbours, WorldTile lastTile, Set<WorldTile> river) {
        WorldTile next = lastTile;
        for (int i = 0; i < neighbours.length; i++) {
            if (!river.contains(neighbours[i]) && neighbours[i].getHeight() <= next.getHeight()) {
                next = neighbours[i];
            }
        }
        if (next.equals(lastTile)) {
            return null;
        }
        return next;
    }

    public WorldTile[] getNeighbours(WorldTile tile) {
        WorldTile[] neighbours = new WorldTile[8];
        for (int i = 0; i < 8; i++) {
            neighbours[i] = getTile(tile.getX() + dirlist[i][0], tile.getY() + dirlist[i][1]);
        }
        return neighbours;
    }

    public WorldTile getTile(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return world[x + y * width];
        } else {
            return new WorldTile(x, y, 512, false, false);
        }
    }

    public void draw(PixelWriter writer) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                writer.setArgb(i, height - j - 1, world[i + j * width].getColor());
            }
        }
    }
}
