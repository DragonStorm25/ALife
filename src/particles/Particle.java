package src.particles;

import src.util.Vector2D;

public class Particle {
    public static final double DEFAULT_INTERACTION_DISTANCE = 50;
    private static int ID_COUNTER = 0;

    private Vector2D pos, vel;
    private int mass;
    private final int id;
    protected double interactionDistance = DEFAULT_INTERACTION_DISTANCE;

    public Particle(int mass) {
        this(Vector2D.ZERO(), mass);
    }

    public Particle(Vector2D pos, int mass) {
        this(pos, Vector2D.ZERO(), mass);
    }

    public Particle(Vector2D pos, Vector2D vel, int mass) {
        this.pos = pos;
        this.vel = vel;
        this.mass = mass;
        this.id = ID_COUNTER++;
    }

    public Vector2D getPosition() {
        return this.pos;
    }

    public void setPosition(Vector2D pos) {
        this.pos = pos;
    }

    public Vector2D getVelocity() {
        return this.vel.clone();
    }

    public void setVelocity(Vector2D vel) {
        this.vel = vel;
    }

    public int getMass() {
        return this.mass;
    }

    public int getId() {
        return this.id;
    }

    public double getInteractionDistance() {
        return this.interactionDistance;
    }

    public String toString() {
        return "Pos: " + this.pos + ", Vel: " + this.vel + ", Mass: " + this.mass;
    }

    public boolean equalsValue (Object other) {
        if (!(other instanceof Particle)) return false;
        return this.pos.equals(((Particle) other).pos) && this.vel.equals(((Particle) other).vel) && this.mass == ((Particle) other).mass;
    }

    @Override
    public boolean equals (Object other) {
        if (!(other instanceof Particle)) return false;
        return this.id == ((Particle)other).id;
    }
}