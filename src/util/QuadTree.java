package src.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import src.particles.Particle;

public class QuadTree {
    private static final double MIN_SIZE = 1;
    private static int ID_COUNTER = 0;

    public Vector2D minPoint, maxPoint, midPoint;
    public List<Particle> particles;
    public int maxParticlesBeforeSplit = 10;
    public QuadTree parent;
    public QuadTree topLeft, topRight, bottomLeft, bottomRight;
    private final int id;

    public QuadTree(){
        this(null, Vector2D.ZERO(), Vector2D.ZERO());
    }

    public QuadTree(Vector2D min, Vector2D max){
        this(null, min, max);
    }

    private QuadTree(QuadTree parent, Vector2D min, Vector2D max){
        particles = new ArrayList<>();
        this.minPoint = min;
        this.maxPoint = max;
        this.midPoint = min.plus(max).scale(0.5);
        this.id = ID_COUNTER++;
    }

    public void insert(Particle p) {
        if (this.isFull()) { // Needs to be split
            if (!inBoundary(p.getPosition()))
                return;

            final Vector2D size = maxPoint.plus(minPoint.scale(-1));
            if (size.getX() < MIN_SIZE || size.getY() < MIN_SIZE) { // Cannot split, just add it (regardless of max particles)
                this.particles.add(p);
                return;
            }

            // Now to check which quad it's in
            if (p.getPosition().getX() < this.midPoint.getX()) { // Left
                if (p.getPosition().getY() < this.midPoint.getY()) { // Top left
                    if (topLeft == null)
                        topLeft = new QuadTree(this, this.minPoint.clone(), this.midPoint.clone());
                    topLeft.insert(p);
                } else { // Bottom left
                    if (bottomLeft == null)
                        bottomLeft = new QuadTree(this, new Vector2D(this.minPoint.getX(), this.midPoint.getY()), new Vector2D(this.midPoint.getX(), this.maxPoint.getY()));
                    bottomLeft.insert(p);
                }
            } else { // Right
                if (p.getPosition().getY() < this.midPoint.getY()) { // Top right
                    if (topRight == null)
                        topRight = new QuadTree(this, new Vector2D(this.midPoint.getX(), this.minPoint.getY()), new Vector2D(this.maxPoint.getX(), this.midPoint.getY()));
                    topRight.insert(p);
                } else { // Bottom right
                    if (bottomRight == null)
                        bottomRight = new QuadTree(this, this.midPoint.clone(), this.maxPoint.clone());
                    bottomRight.insert(p);
                }
            }
        } else { // No need to split
            this.particles.add(p);
        }
    }

    public void remove(Particle p) {
        if (!inBoundary(p.getPosition()))
            return;

        if (this.particles.contains(p))
            this.particles.remove(p);

        // Now to check which quad it's in
        if (p.getPosition().getX() < this.midPoint.getX()) { // Left
            if (p.getPosition().getY() < this.midPoint.getY()) { // Top left
                if (topLeft == null)
                    return;
                topLeft.remove(p);
            } else { // Bottom left
                if (bottomLeft == null)
                    return;
                bottomLeft.remove(p);
            }
        } else { // Right
            if (p.getPosition().getY() < this.midPoint.getY()) { // Top right
                if (topRight == null)
                    return;
                topRight.remove(p);
            } else { // Bottom right
                if (bottomRight == null)
                    return;
                bottomRight.remove(p);
            }
        }
    }

    public void move(Particle p, Vector2D newPos) {
        if (!inBoundary(p.getPosition()))
            return;

        if (!inBoundary(newPos))
            return;

        QuadTree particleQuad = this.firstEmptyTreeWithPoint(p.getPosition());
        QuadTree newPosQuad = this.firstEmptyTreeWithPoint(newPos);

        if (particleQuad.equals(newPosQuad)) { // No movement to other quad! Easy
            p.setPosition(newPos);
        } else { // Need to move particle to other quad
            this.remove(p);
            p.setPosition(newPos);
            newPosQuad.insert(p);
        }
    }

    public boolean particleAtPoint(Vector2D point) {
        if (!inBoundary(point))
            return false;

        if (particles.stream().map(Particle::getPosition).toList().contains(point))
            return true;
 
        if (point.getX() < this.midPoint.getX()) { // Left
            if (point.getY() < this.midPoint.getY()) { // Top left
                if (topLeft == null)
                    return false;
                return topLeft.particleAtPoint(point);
            } else { // Bottom left
                if (bottomLeft == null)
                    return false;
                return bottomLeft.particleAtPoint(point);
            }
        } else { // Right
            if (point.getY() < this.midPoint.getY()) { // Top right
                if (topRight == null)
                    return false;
                return topRight.particleAtPoint(point);
            } else { // Bottom right
                if (bottomRight == null)
                    return false;
                return bottomRight.particleAtPoint(point);
            }
        }
    }

    public List<Particle> getParticlesWithinDistance(Vector2D point, double distance) {
        List<Particle> possibleParticles = this.rectQuery(point.plus(new Vector2D(-distance, -distance)), point.plus(new Vector2D(distance, distance)));
        List<Particle> actualParticles = new ArrayList<>();
        for (Particle p : possibleParticles) {
            final double px = p.getPosition().getX(), py = p.getPosition().getY();
            final double pox = point.getX(), poy = point.getY();
            if ((px - pox) * (px - pox) + (py - poy) * (py - poy) < distance*distance)
                actualParticles.add(p);
        }
        return actualParticles;
    }

    public List<Particle> rectQuery(Vector2D min, Vector2D max) {
        boolean topLeft = true, topRight = true, bottomLeft = true, bottomRight = true;
        final List<Particle> particles = new ArrayList<>();
        if (min.getX() >= midPoint.getX()) { // Only have to search right half
            topLeft = false;
            bottomLeft = false;
        } 
        if (max.getX() < midPoint.getX()) { // Only have to search left half
            topRight = false;
            bottomRight = false;
        }
        if (max.getY() < midPoint.getY()) { // Only have to search top half
            bottomLeft = false;
            bottomRight = false;
        } 
        if (min.getY() >= midPoint.getY()) { // Only have to search bottom half
            topLeft = false;
            topRight = false;
        }

        if (topLeft && this.topLeft != null) particles.addAll(this.topLeft.rectQuery(min, max));
        if (topRight && this.topRight != null) particles.addAll(this.topRight.rectQuery(min, max));
        if (bottomLeft && this.bottomLeft != null) particles.addAll(this.bottomLeft.rectQuery(min, max));
        if (bottomRight && this.bottomRight != null) particles.addAll(this.bottomRight.rectQuery(min, max));

        particles.addAll(this.particles);

        return particles;
    }

    private boolean inBoundary(Vector2D pos) {
        return pos.getX() >= minPoint.getX() && pos.getX() <= maxPoint.getX() && pos.getY() >= minPoint.getY() && pos.getY() <= maxPoint.getY();
    }

    private QuadTree firstEmptyTreeWithPoint(Vector2D pos) {
        if (pos.getX() < this.midPoint.getX()) { // Left
            if (pos.getY() < this.midPoint.getY()) { // Top left
                if (topLeft == null)
                    return this;
                if (!topLeft.isFull())
                    return topLeft;
                return topLeft.firstEmptyTreeWithPoint(pos);
            } else { // Bottom left
                if (bottomLeft == null)
                    return this;
                if (!bottomLeft.isFull())
                    return bottomLeft;
                return bottomLeft.firstEmptyTreeWithPoint(pos);
            }
        } else { // Right
            if (pos.getY() < this.midPoint.getY()) { // Top right
                if (topRight == null)
                    return this;
                if (!topRight.isFull())
                    return topRight;
                return topRight.firstEmptyTreeWithPoint(pos);
            } else { // Bottom right
                if (bottomRight == null)
                    return this;
                if (!bottomRight.isFull())
                    return bottomRight;
                return bottomRight.firstEmptyTreeWithPoint(pos);
            }
        }
    }

    public boolean isFull() {
        return this.particles.size() == this.maxParticlesBeforeSplit;
    }

    public String toString() {
        return this.toString(0);
    }

    public String toString(int indents) {
        String indentString = "\t".repeat(indents);

        String s = indentString + "Particles: " + particles.stream().map(Particle::toString).collect(Collectors.joining(", ")) + "\n";
        s += indentString + "TopLeft: " + (topLeft == null ? "null" : ("\n" + topLeft.toString(indents + 1))) + "\n";
        s += indentString + "TopRight: " + (topRight == null ? "null" : ("\n" + topRight.toString(indents + 1))) + "\n";
        s += indentString + "BottomLeft: " + (bottomLeft == null ? "null" : ("\n" + bottomLeft.toString(indents + 1))) + "\n";
        s += indentString + "BottomRight: " + (bottomLeft == null ? "null" : ("\n" + bottomLeft.toString(indents + 1))) + "\n";
        return s;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof QuadTree)) return false;
        return this.id == ((QuadTree)other).id;
    }
}
