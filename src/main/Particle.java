package src.main;

import src.physics.*;

abstract class Particle implements IVelocity, IMass {
    
}

abstract class ChargedParticle extends Particle implements ICharge {
    
}