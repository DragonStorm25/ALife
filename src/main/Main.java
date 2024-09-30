package src.main;

import src.graphics.Renderer;
import src.particles.ChargedParticle;
import src.physics.PhysicsEngine;
import src.util.Vector2D;

public class Main {
    
    public static void main(String[] args) {
        PhysicsEngine pe = new PhysicsEngine(0.1);
        Renderer r = new Renderer(pe);
        r.toggleShow();
        pe.particles.insert(new ChargedParticle(new Vector2D(100, 100), 2, 10));
        pe.particles.insert(new ChargedParticle(new Vector2D(101, 101), 2, 10));
        pe.startEngine();
    }
}
