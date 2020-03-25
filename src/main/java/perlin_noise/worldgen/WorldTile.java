// SPDX-FileCopyrightText: 2020 Theo Dedeken
//
// SPDX-License-Identifier: MIT

package perlin_noise.worldgen;

/**
 * Created by theod on 2-1-2017.
 */
public class WorldTile {

    private int height;
    private boolean forest;
    private boolean river;
    private int x;
    private int y;

    public WorldTile(int x, int y, int height, boolean forest, boolean river) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.forest = forest;
        this.river = river;
    }

    public void setForest(boolean forest) {
        this.forest = forest;
    }

    public void setRiver(boolean river) {
        this.river = river;
    }

    public int getHeight() {
        return height;
    }

    public int getColor() {
        if (height < 64) {

            return 0xff000000 + 192 + height;

        } else if (river) {
            return 0xff000033;
        } else if (forest) {
            return 0xff003300;
        } else if (height < 192) {

            return 0xff000000 + ((64 + height) << 8);
        } else {
            return 0xff000000 + (height << 16) + (height << 8) + height;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
