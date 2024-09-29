package src.main;

import src.util.Vector2D;

public class ChargedParticle extends Particle {
    public static final double CHARGE_CONSTANT = 1;

    public int charge = 0;
    public double maxRange = 1;
    public double minRange = 0.1;

    public ChargedParticle(int mass, int charge) {
        this(Vector2D.ZERO(), mass, charge);
    }

    public ChargedParticle(Vector2D pos, int mass, int charge) {
        this(pos, Vector2D.ZERO(), mass, charge);
    }

    public ChargedParticle(Vector2D pos, Vector2D vel, int mass, int charge) {
        super(pos, vel, mass);
        this.charge = charge;
    }

    public static Vector2D getChargeForce(ChargedParticle c1, ChargedParticle c2) {
        final Vector2D vectorBetween = c1.getPosition().plus(c2.getPosition().scale(-1));
        final double distance = vectorBetween.getMagnitude();
        final double mag = (CHARGE_CONSTANT * c1.charge * c2.charge) / (distance * distance);
        return vectorBetween.normalized().scale(mag);
    }
}