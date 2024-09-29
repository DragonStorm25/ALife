package src.main;

import src.util.Vector2D;

abstract class Particle {
    private Vector2D pos, vel;
    private int mass;

    public Vector2D getPosition() {
        return this.pos.clone();
    }

    public Vector2D getVelocity() {
        return this.vel.clone();
    }

    public int getMass() {
        return this.mass;
    }
}

abstract class ChargedParticle extends Particle {
    public static final double CHARGE_CONSTANT = 1;

    public int charge = 0;
    public double maxRange = 1;
    public double minRange = 0.1;

    public static double getChargeForce(ChargedParticle c1, ChargedParticle c2) {
        final double distance = c1.pos.plus(c2.pos.scale(-1)).getMagnitude();
        final double mag = (ICharge.CHARGE_CONSTANT * c1.charge * c2.charge) / (distance * distance);
        return ;
    }
}