package src.util;

public class Vector2D {
    public static final Vector2D ZERO () {return new Vector2D(0, 0);}

    private final double x, y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getMagnitude() {
        return Math.sqrt(x*x + y*y);
    }

    public double getMagnitudeSquared() {
        return x*x + y*y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Vector2D normalized() {
        final double mag = this.getMagnitude();
        return new Vector2D(this.x/mag, this.y/mag);
    }

    public Vector2D plus(Vector2D other) {
        return new Vector2D(this.x + other.x, this.y + other.y);
    }

    public Vector2D scale(double scalar) {
        return new Vector2D(scalar * this.x, scalar * this.y);
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Vector2D)) return false;
        return this.x == ((Vector2D)other).x && this.y == ((Vector2D)other).y;
    }
}
