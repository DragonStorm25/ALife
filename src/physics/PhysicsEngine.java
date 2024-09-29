package src.physics;

import java.util.ArrayList;
import java.util.List;

import src.main.*;
import src.particles.Particle;

public class PhysicsEngine {
    private List<Particle> particles;
    private double deltaTime;

    public PhysicsEngine() {
        this.particles = new ArrayList<>();
    }
}
