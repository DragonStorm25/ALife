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

    public void doTimeStep(Vector2D force, double deltaTime) {
        // k1 = f/m * dt
        final Vector2D a1 = force.scale(1.0/this.mass);
        final Vector2D k1 = a1.scale(deltaTime);

        // k2 = (f + 1/2*k1)/m * dt
        final Vector2D a2 = force.plus(k1.scale(0.5)).scale(1/this.mass);
        final Vector2D k2 = a2.scale(deltaTime);

        // k3 = (f + 1/2*k2)/m * dt
        final Vector2D a3 = force.plus(k2.scale(0.5)).scale(1/this.mass);
        final Vector2D k3 = a3.scale(deltaTime);

        // k4 = (f + 1/2*k3)/m * dt
        final Vector2D a4 = force.plus(k3.scale(0.5)).scale(1/this.mass);
        final Vector2D k4 = a4.scale(deltaTime);

        // (k1 + 2*k2 + 2*k3 + k4)/6
        final Vector2D deltaV = k1.plus(k2.scale(2)).plus(k3.scale(2)).plus(k4).scale(1/6.0);
        this.vel = this.vel.plus(deltaV);
        this.pos = this.pos.plus(this.vel.scale(deltaTime));
    }

    public String toString() {
        return "Pos: " + this.pos + ", Vel: " + this.vel + ", Mass: " + this.mass;
    }
}