package src.particles;

import src.util.Vector2D;

public class ChargedParticle extends Particle {
    public static final double CHARGE_CONSTANT = 1;
    public static final double MAX_RANGE = 10;
    public static final double MIN_RANGE = 0.1;

    public int charge = 0;

    public ChargedParticle(int mass, int charge) {
        this(Vector2D.ZERO(), mass, charge);
    }

    public ChargedParticle(Vector2D pos, int mass, int charge) {
        this(pos, Vector2D.ZERO(), mass, charge);
    }

    public ChargedParticle(Vector2D pos, Vector2D vel, int mass, int charge) {
        super(pos, vel, mass);
        this.charge = charge;
        this.interactionDistance = MAX_RANGE * Math.sqrt(charge);
    }

    public static Vector2D getChargeForce(ChargedParticle c1, ChargedParticle c2) {
        final Vector2D vectorBetween = c1.getPosition().plus(c2.getPosition().scale(-1));
        final double distanceSquared = vectorBetween.getMagnitudeSquared();
        if (distanceSquared > MAX_RANGE * MAX_RANGE * c1.charge || distanceSquared < MIN_RANGE * MIN_RANGE) 
            return Vector2D.ZERO();
        final double mag = (CHARGE_CONSTANT * c1.charge * c2.charge) / distanceSquared;
        return vectorBetween.normalized().scale(mag);
    }

    public String toString() {
        return super.toString() + ", Charge: " + this.charge + " (Range: [" + MIN_RANGE + "," + MAX_RANGE + "])";
    }
}