package src.main;

import src.util.Vector2D;

public class Particle {
    private Vector2D pos, vel;
    private int mass;

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