package src.main;

import java.util.List;

import src.particles.Particle;
import src.util.QuadTree;
import src.util.Vector2D;

public class Main {
    
    public static void main(String[] args) {
        QuadTree qt = new QuadTree(Vector2D.ZERO(), new Vector2D(128, 128));
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++){
                qt.insert(new Particle(new Vector2D(10*i, 10*j), 0));
            }
        }
        System.out.println(qt);
        List<Particle> nearby = qt.getParticlesWithinDistance(new Vector2D(10, 10), 15);
        for (Particle p : nearby) {
            System.out.println(p);
        }
    }
}
