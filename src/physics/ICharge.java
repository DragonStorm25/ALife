package src.physics;

public interface ICharge {
    public static final double CHARGE_CONSTANT = 1;

    public int charge = 0;
    public double maxRange = 1;
    public double minRange = 0.1;

    public static double getChargeForce(ICharge c1, ICharge c2, double distance) {
        return (CHARGE_CONSTANT * c1.charge * c2.charge) / (distance * distance);
    }
}