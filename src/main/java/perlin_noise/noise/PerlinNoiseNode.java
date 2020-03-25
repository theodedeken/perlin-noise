// SPDX-FileCopyrightText: 2020 Theo Dedeken
//
// SPDX-License-Identifier: MIT

package perlin_noise.noise;

import perlin_noise.math.Vector2D;

/**
 * Created by theod on 18-12-2016.
 */
public class PerlinNoiseNode {

    private double angle;
    private Vector2D angleVector;
    private Vector2D pos;
    private double normaliser;

    /*
     * Getters and Setters
     */
    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public Vector2D getAngleVector() {
        return angleVector;
    }

    public void setAngleVector(Vector2D angleVector) {
        this.angleVector = angleVector;
    }

    public Vector2D getPos() {
        return pos;
    }

    public void setPos(Vector2D pos) {
        this.pos = pos;
    }

    public double getNormaliser() {
        return normaliser;
    }

    public void setNormaliser(double normaliser) {
        this.normaliser = normaliser;
    }

    public PerlinNoiseNode(double angle) {
        this.angle = angle;
        this.angleVector = new Vector2D(angle);
    }

    public double getInfluence(Vector2D coord) {
        Vector2D relPos = pos.minus(coord);
        Vector2D normRelPos = relPos.mult(normaliser);
        return normRelPos.dot(angleVector);
    }

}
