package src.util;

import java.util.List;
import src.main.*;

public class QuadTree {
    private static final double MIN_SIZE = 1;

    public Vector2D minPoint, maxPoint, midPoint;
    public List<Particle> particles;
    public int maxParticlesBeforeSplit = 1;
    public QuadTree topLeft, topRight, bottomLeft, bottomRight;

    public QuadTree(){
        this(Vector2D.ZERO(), Vector2D.ZERO());
    }

    public QuadTree(Vector2D min, Vector2D max){
        this.minPoint = min;
        this.maxPoint = max;
        this.midPoint = min.plus(max).scale(0.5);
    }

    public void insert(Particle p) {
        if (particles.size() == maxParticlesBeforeSplit) { // Needs to be split
            if (!inBoundary(p.getPosition()))
                return;

            final Vector2D size = minPoint.plus(maxPoint.scale(-1));
            if (size.getX() < MIN_SIZE || size.getY() < MIN_SIZE) { // Cannot split, just add it (regardless of max particles)
                this.particles.add(p);
                return;
            }

            // Now to check which quad it's in
            if (p.getPosition().getX() < this.midPoint.getX()) { // Left
                if (p.getPosition().getY() < this.midPoint.getY()) { // Top left
                    if (topLeft == null)
                        topLeft = new QuadTree(this.minPoint.clone(), this.midPoint.clone());
                    topLeft.particles.add(p);
                } else { // Bottom left
                    if (bottomLeft == null)
                        bottomLeft = new QuadTree(new Vector2D(this.minPoint.getX(), this.midPoint.getY()), new Vector2D(this.midPoint.getX(), this.maxPoint.getY()));
                    bottomLeft.particles.add(p);
                }
            } else { // Right
                if (p.getPosition().getY() < this.midPoint.getY()) { // Top right
                    if (topRight == null)
                        topRight = new QuadTree(new Vector2D(this.midPoint.getX(), this.minPoint.getY()), new Vector2D(this.maxPoint.getX(), this.midPoint.getY()));
                    topRight.particles.add(p);
                } else { // Bottom right
                    if (bottomRight == null)
                        bottomRight = new QuadTree(this.midPoint.clone(), this.maxPoint.clone());
                    bottomRight.particles.add(p);
                }
            }
        } else { // No need to split
            this.particles.add(p);
        }
    }

    private boolean inBoundary(Vector2D pos) {
        return pos.getX() >= minPoint.getX() && pos.getX() <= maxPoint.getX() && pos.getY() >= minPoint.getY() && pos.getY() <= maxPoint.getY();
    }
}
