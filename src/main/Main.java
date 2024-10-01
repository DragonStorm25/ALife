package src.main;

import src.graphics.Renderer;
import src.particles.ChargedParticle;
import src.particles.Particle;
import src.physics.PhysicsEngine;
import src.util.Profiler;
import src.util.Vector2D;

public class Main {
    
    public static void main(String[] args) {
        // Profiler.SINGLETON.print = true;
        // Profiler.SINGLETON.startProfiling();

        PhysicsEngine pe = new PhysicsEngine(0.1);
        Renderer r = new Renderer(pe);
        for (int i = 0; i < 10000; i++) {
            pe.insert(new ChargedParticle(new Vector2D(512+(int)((Math.random()-0.5)*512), 512+(int)((Math.random()-0.5)*512)), 2, 2));
        }
        // Particle p1 = new ChargedParticle(new Vector2D(200, 100), 10, 20);
        // Particle p2 = new ChargedParticle(new Vector2D(100, 100), 10, 20);
        // Particle p3 = new Particle(new Vector2D(800, 100), 10);
        // p1.setVelocity(new Vector2D(10, 0));
        // pe.insert(p2);
        // pe.insert(p1);
        //pe.insert(p3);
        // pe.insert(new ChargedParticle(new Vector2D(511, 100), 10, 20));
        // pe.insert(new ChargedParticle(new Vector2D(513, 100), 10, 20));
        pe.startEngine();
        r.toggleShow();
    }
}
