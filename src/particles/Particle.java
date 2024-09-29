package src.particles;

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

    public String toString() {
        return "Pos: " + this.pos + ", Vel: " + this.vel + ", Mass: " + this.mass;
    }

    @Override
    public boolean equals (Object other) {
        if (!(other instanceof Particle)) return false;
        return this.pos.equals(((Particle) other).pos) && this.vel.equals(((Particle) other).vel) && this.mass == ((Particle) other).mass;
    }
}