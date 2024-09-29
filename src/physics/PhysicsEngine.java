package src.physics;

import src.particles.Particle;
import src.util.QuadTree;
import src.util.Vector2D;

public class PhysicsEngine {
    private QuadTree particles;
    private double deltaTime;

    public PhysicsEngine(double dt) {
        particles = new QuadTree(Vector2D.ZERO(), new Vector2D(128, 128));
        this.deltaTime = dt;
    }

    public void doParticleTimeStep(Particle p, Vector2D force, double deltaTime) {
        // k1 = f/m * dt
        final Vector2D a1 = force.scale(1.0/p.getMass());
        final Vector2D k1 = a1.scale(deltaTime);

        // k2 = (f + 1/2*k1)/m * dt
        final Vector2D a2 = force.plus(k1.scale(0.5)).scale(1/p.getMass());
        final Vector2D k2 = a2.scale(deltaTime);

        // k3 = (f + 1/2*k2)/m * dt
        final Vector2D a3 = force.plus(k2.scale(0.5)).scale(1/p.getMass());
        final Vector2D k3 = a3.scale(deltaTime);

        // k4 = (f + 1/2*k3)/m * dt
        final Vector2D a4 = force.plus(k3.scale(0.5)).scale(1/p.getMass());
        final Vector2D k4 = a4.scale(deltaTime);

        // (k1 + 2*k2 + 2*k3 + k4)/6
        final Vector2D deltaV = k1.plus(k2.scale(2)).plus(k3.scale(2)).plus(k4).scale(1/6.0);
        final Vector2D newVel = p.getVelocity().plus(deltaV);
        final Vector2D newPos = p.getPosition().plus(newVel.scale(deltaTime));
        
        p.setPosition(newPos);
        p.setVelocity(newVel);
    }
}
