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
        this.parent = parent;
        this.id = ID_COUNTER++;
    }

    public void makeTopLeft() {
        topLeft = new QuadTree(this, this.minPoint.clone(), this.midPoint.clone());
    }

    public void makeTopRight() {
        topRight = new QuadTree(this, new Vector2D(this.midPoint.getX(), this.minPoint.getY()), new Vector2D(this.maxPoint.getX(), this.midPoint.getY()));
    }

    public void makeBottomLeft() {
        bottomLeft = new QuadTree(this, new Vector2D(this.minPoint.getX(), this.midPoint.getY()), new Vector2D(this.midPoint.getX(), this.maxPoint.getY()));
    }

    public void makeBottomRight() {
        bottomRight = new QuadTree(this, this.midPoint.clone(), this.maxPoint.clone());
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
                        this.makeTopLeft();
                    topLeft.insert(p);
                } else { // Bottom left
                    if (bottomLeft == null)
                        this.makeBottomLeft();
                    bottomLeft.insert(p);
                }
            } else { // Right
                if (p.getPosition().getY() < this.midPoint.getY()) { // Top right
                    if (topRight == null)
                        this.makeTopRight();
                    topRight.insert(p);
                } else { // Bottom right
                    if (bottomRight == null)
                        this.makeBottomRight();
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

        if (this.particles.contains(p)) {
            this.particles.remove(p);
            return;
        }

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

        QuadTree particleQuad = this.searchForParticle(p);
        QuadTree newPosQuad = this.firstEmptyTreeWithPoint(newPos);

        if (particleQuad != null && particleQuad.equals(newPosQuad)) { // No movement to other quad! Easy
            p.setPosition(newPos);
        } else { // Need to move particle to other quad
            particleQuad.remove(p);
            p.setPosition(newPos);
            newPosQuad.insert(p);
            particleQuad.merge();
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
        Profiler.SINGLETON.startTimer("PossibleParticleFinding");
        List<Particle> possibleParticles = this.rectQuery(point.plus(new Vector2D(-distance, -distance)), point.plus(new Vector2D(distance, distance)));
        Profiler.SINGLETON.stopTimer("PossibleParticleFinding");
        Profiler.SINGLETON.startTimer("ActualParticleFinding");
        List<Particle> actualParticles = new ArrayList<>();
        for (Particle p : possibleParticles) {
            final double px = p.getPosition().getX(), py = p.getPosition().getY();
            final double pox = point.getX(), poy = point.getY();
            if ((px - pox) * (px - pox) + (py - poy) * (py - poy) < distance*distance)
                actualParticles.add(p);
        }
        Profiler.SINGLETON.stopTimer("ActualParticleFinding");
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
        if (!this.isFull() && this.inBoundary(pos))
            return this;

        if (pos.getX() < this.midPoint.getX()) { // Left
            if (pos.getY() < this.midPoint.getY()) { // Top left
                if (topLeft == null)
                    this.makeTopLeft();
                return topLeft.firstEmptyTreeWithPoint(pos);
            } else { // Bottom left
                if (bottomLeft == null)
                    this.makeBottomLeft();
                return bottomLeft.firstEmptyTreeWithPoint(pos);
            }
        } else { // Right
            if (pos.getY() < this.midPoint.getY()) { // Top right
                if (topRight == null)
                    this.makeTopRight();
                return topRight.firstEmptyTreeWithPoint(pos);
            } else { // Bottom right
                if (bottomRight == null)
                    this.makeBottomRight();
                return bottomRight.firstEmptyTreeWithPoint(pos);
            }
        }
    }

    public QuadTree searchForParticle(Particle p) {
        if (this.particles.contains(p))
            return this;

        if (p.getPosition().getX() < this.midPoint.getX()) { // Left
            if (p.getPosition().getY() < this.midPoint.getY()) { // Top left
                return topLeft.searchForParticle(p);
            } else { // Bottom left
                return bottomLeft.searchForParticle(p);
            }
        } else { // Right
            if (p.getPosition().getY() < this.midPoint.getY()) { // Top right
                return topRight.searchForParticle(p);
            } else { // Bottom right
                return bottomRight.searchForParticle(p);
            }
        }
    }

    private void removeQuad(QuadTree qt) {
        if (this.topLeft != null && this.topLeft.equals(qt)) this.topLeft = null;
        if (this.topRight != null && this.topRight.equals(qt)) this.topRight = null;
        if (this.bottomLeft != null && this.bottomLeft.equals(qt)) this.bottomLeft = null;
        if (this.bottomRight != null && this.bottomRight.equals(qt)) this.bottomRight = null;
    }

    public void merge() {
        QuadTree[] quads = {this.topLeft, this.topRight, this.bottomLeft, this.bottomRight};
        for (int i = 0; i < quads.length; i++) {
            if (quads[i] == null)
                continue;
            int spaceLeft = this.maxParticlesBeforeSplit - this.particles.size();
            for (int j = 0; j < Math.min(spaceLeft, quads[i].particles.size()); j++) {
                Particle p = quads[i].particles.get(0);
                quads[i].remove(p);
                this.insert(p);
            }
            quads[i].merge();
        }
        this.prune();
    }

    public void prune() {
        if (this.topLeft != null) this.topLeft.prune();
        if (this.topRight != null) this.topRight.prune();
        if (this.bottomLeft != null) this.bottomLeft.prune();
        if (this.bottomRight != null) this.bottomRight.prune();

        if (this.particles.size() == 0 && this.topLeft == null && this.topRight == null && this.bottomLeft == null && this.bottomRight == null && this.parent != null)
            this.parent.removeQuad(this);
    }

    public boolean isFull() {
        return this.particles.size() >= this.maxParticlesBeforeSplit;
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
