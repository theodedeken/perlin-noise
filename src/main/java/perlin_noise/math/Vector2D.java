package perlin_noise.math;

/**
 * Created by theod on 18-12-2016.
 */
public class Vector2D {

    private double x;
    private double y;

    /*
     * Getters and Setters
     */
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(double angle) {
        this.x = Math.cos(angle);
        this.y = Math.sin(angle);
    }

    public double dot(Vector2D b) {
        return x * b.getX() + y * b.getY();
    }

    public Vector2D minus(Vector2D b) {
        return new Vector2D(x - b.getX(), y - b.getY());
    }

    public Vector2D mult(double factor) {
        return new Vector2D(x * factor, y * factor);
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public void fadePerlin() {
        x = ppolynomial(x);
        y = ppolynomial(y);
    }

    private double ppolynomial(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    public Vector2D normalise() {
        return mult(1 / length());
    }

    public void fadeStep(int steps) {
        double divlen = 1.0 / steps;
        x = x - x % divlen;
        y = y - y % divlen;
    }

    public void fadefunction() {
        x = ffunction(x);
        y = ffunction(y);
    }

    private double ffunction(double t) {
        return -2 * t * t * t + 3 * t * t;
    }

}
