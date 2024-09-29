package src.main;

import src.particles.Particle;
import src.util.QuadTree;
import src.util.Vector2D;

public class Main {
    
    public static void main(String[] args) {
        QuadTree qt = new QuadTree(Vector2D.ZERO(), new Vector2D(128, 128));
        System.out.println(qt);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++){
                qt.insert(new Particle(new Vector2D(10*i, 10*j), 0));
            }
        }
        System.out.println(qt);
    }
}
