package tests.util;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test 
    void particleRemoval() {
        QuadTree qt = new QuadTree(Vector2D.ZERO(), new Vector2D(128, 128));
        Particle removedParticle = null;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++){
                Particle p = new Particle(new Vector2D(10*i, 10*j), 0);
                if (i == 1 && j == 1)
                    removedParticle = p;
                qt.insert(p);
            }
        }
        qt.remove(removedParticle);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++){
                if (i == 1 && j == 1)
                    assertFalse(qt.particleAtPoint(new Vector2D(10*i, 10*j)));
                else
                    assertTrue(qt.particleAtPoint(new Vector2D(10*i, 10*j)));
            }
        }
    }

    @Test
    void particleMove() {
        QuadTree qt = new QuadTree(Vector2D.ZERO(), new Vector2D(128, 128));
        for (int i = 0; i < QuadTree.MAX_PARTICLES_BEFORE_SPLIT; i++) {
            Particle p = new Particle(new Vector2D(16 + i, 16+i), 0);
            qt.insert(p);
        }
        Particle p2 = new Particle(new Vector2D(32, 32), 0);
        qt.insert(p2);
        qt.move(p2, new Vector2D(100, 100));
        assertEquals(qt.topLeft.particles.size(), 0);
        assertEquals(qt.bottomRight.particles.size(), 1);
    }

    @Test
    void treePrune() {
        QuadTree qt = new QuadTree(Vector2D.ZERO(), new Vector2D(256, 256));
        Particle[] particles = new Particle[100];
        for (int i = 0; i < particles.length; i++) {
            Particle p = new Particle(new Vector2D(16 + i, 16+i), 0);
            qt.insert(p);
            particles[i] = p;
        }
        qt.prune();
        for (int i = 0; i < particles.length; i++) {
            assertTrue(qt.particleAtPoint(new Vector2D(16 + i, 16+i)));
        }
        for (int i = 0; i < particles.length; i++) {
            qt.remove(particles[i]);
        }
        qt.prune();
        assertTrue(qt.topLeft == null);
        assertTrue(qt.topRight == null);
        assertTrue(qt.bottomLeft == null);
        assertTrue(qt.bottomRight == null);
    }
}
