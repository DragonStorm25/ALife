package src.main;

import src.util.Vector2D;

public class ChargedParticle extends Particle {
    public static final double CHARGE_CONSTANT = 1;

    public int charge = 0;
    public double maxRange = 1;
    public double minRange = 0.1;

    public static Vector2D getChargeForce(ChargedParticle c1, ChargedParticle c2) {
        final Vector2D vectorBetween = c1.getPosition().plus(c2.getPosition().scale(-1));
        final double distance = vectorBetween.getMagnitude();
        final double mag = (CHARGE_CONSTANT * c1.charge * c2.charge) / (distance * distance);
        return vectorBetween.normalized().scale(mag);
    }
}