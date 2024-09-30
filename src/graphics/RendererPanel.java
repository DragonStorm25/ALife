package src.graphics;

import java.awt.geom.*;
import java.awt.*; 
import java.awt.event.*;
import javax.swing.*;

import src.particles.Particle;
import src.physics.PhysicsEngine;

public class RendererPanel extends JPanel implements MouseWheelListener {
    private PhysicsEngine pe;
    private double zoomFactor = 1;

    public RendererPanel(PhysicsEngine pe) {
        this.pe = pe;
        this.addMouseWheelListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.render(g);
    }

    public void render(Graphics g) {
        AffineTransform at = new AffineTransform();
        at.scale(zoomFactor, zoomFactor);

        Graphics2D g2d = (Graphics2D) g;
        g2d.transform(at);

        g2d.setColor(Color.red);
        for (Particle p : pe.idToParticle.values()) {
            g2d.fillOval((int)p.getPosition().getX(), (int)p.getPosition().getY(), p.getMass(), p.getMass());
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        //Zoom in
        if(e.getWheelRotation() < 0)
            this.zoomFactor *= 1.1;
        //Zoom out
        if(e.getWheelRotation() > 0)
            this.zoomFactor /= 1.1;
    }
}
