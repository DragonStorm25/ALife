package src.main;

import src.util.Vector2D;

public class Particle {
    private Vector2D pos, vel;
    private int mass;

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
    }

    public Vector2D getPosition() {
        return this.pos.clone();
    }

    public Vector2D getVelocity() {
        return this.vel.clone();
    }

    public int getMass() {
        return this.mass;
    }
}