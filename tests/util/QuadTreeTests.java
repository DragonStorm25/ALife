package tests.util;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import src.particles.Particle;
import src.util.QuadTree;
import src.util.Vector2D;

public class QuadTreeTests {
    
    @Test
    void particleInsertion() {
        QuadTree qt = new QuadTree(Vector2D.ZERO(), new Vector2D(128, 128));
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++){
                qt.insert(new Particle(new Vector2D(10*i, 10*j), 0));
            }
        }
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++){
                assertTrue(qt.particleAtPoint(new Vector2D(10*i, 10*j)));
            }
        }
    }

    @Test
    void particlesWithinDistance() {
        QuadTree qt = new QuadTree(Vector2D.ZERO(), new Vector2D(128, 128));
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++){
                qt.insert(new Particle(new Vector2D(10*i, 10*j), 0));
            }
        }
        List<Particle> nearby = qt.getParticlesWithinDistance(new Vector2D(10, 10), 15);
        List<Vector2D> nearbyList = nearby.stream().map(Particle::getPosition).toList();
        List<Vector2D> expectedList = new ArrayList<>();
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                expectedList.add(new Vector2D(i*10, j*10));
        assertTrue(nearbyList.size() == expectedList.size() && nearbyList.containsAll(expectedList) && expectedList.containsAll(nearbyList));
    }
}
