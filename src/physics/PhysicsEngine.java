package src.physics;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import src.particles.ChargedParticle;
import src.particles.Particle;
import src.util.QuadTree;
import src.util.Vector2D;

public class PhysicsEngine {
    public static final double INTERACTION_DISTANCE = 10;

    private QuadTree particles;
    private double deltaTime;
    private Map<Integer, Vector2D> forces;

    public PhysicsEngine(double dt) {
        particles = new QuadTree(Vector2D.ZERO(), new Vector2D(128, 128));
        this.deltaTime = dt;
        this.forces = new HashMap<>();
    }

    public void doTimeStep() {
        this.forces = new HashMap<>();
        doTimeStep(particles);
        
    }

    private void doTimeStep(QuadTree qt) {
        for (int i = 0; i < qt.particles.size(); i++) {
            Particle p = qt.particles.get(i);
            Vector2D sumForce = Vector2D.ZERO();
            if (p instanceof ChargedParticle) {
                final List<Particle> nearbyParticles = particles.getParticlesWithinDistance(p.getPosition(), INTERACTION_DISTANCE);
                for (int j = 0; j < nearbyParticles.size(); j++) {
                    if (nearbyParticles.get(i) instanceof ChargedParticle)
                        sumForce = sumForce.plus(ChargedParticle.getChargeForce(((ChargedParticle)p), ((ChargedParticle)nearbyParticles.get(j))));
                }
            }
            forces.put(p.getId(), sumForce);
        }
        if (qt.topLeft != null) doTimeStep(qt.topLeft);
        if (qt.topRight != null) doTimeStep(qt.topRight);
        if (qt.bottomLeft != null) doTimeStep(qt.bottomLeft);
        if (qt.bottomRight != null) doTimeStep(qt.bottomRight);
    }

    public void doParticleTimeStep(Particle p, Vector2D force) {
        // k1 = f/m * dt
        final Vector2D a1 = force.scale(1.0/p.getMass());
        final Vector2D k1 = a1.scale(this.deltaTime);

        // k2 = (f + 1/2*k1)/m * dt
        final Vector2D a2 = force.plus(k1.scale(0.5)).scale(1/p.getMass());
        final Vector2D k2 = a2.scale(this.deltaTime);

        // k3 = (f + 1/2*k2)/m * dt
        final Vector2D a3 = force.plus(k2.scale(0.5)).scale(1/p.getMass());
        final Vector2D k3 = a3.scale(this.deltaTime);

        // k4 = (f + 1/2*k3)/m * dt
        final Vector2D a4 = force.plus(k3.scale(0.5)).scale(1/p.getMass());
        final Vector2D k4 = a4.scale(this.deltaTime);

        // (k1 + 2*k2 + 2*k3 + k4)/6
        final Vector2D deltaV = k1.plus(k2.scale(2)).plus(k3.scale(2)).plus(k4).scale(1/6.0);
        final Vector2D newVel = p.getVelocity().plus(deltaV);
        final Vector2D newPos = p.getPosition().plus(newVel.scale(this.deltaTime));
        
        p.setPosition(newPos);
        p.setVelocity(newVel);
    }
}
