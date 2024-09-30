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
    private double prevZoomFactor = 1;
    private double xOffset, yOffset;

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

        double xRel = MouseInfo.getPointerInfo().getLocation().getX() - getLocationOnScreen().getX();
        double yRel = MouseInfo.getPointerInfo().getLocation().getY() - getLocationOnScreen().getY();

        double zoomDiv = zoomFactor / prevZoomFactor;

        xOffset = (zoomDiv) * (xOffset) + (1 - zoomDiv) * xRel;
        yOffset = (zoomDiv) * (yOffset) + (1 - zoomDiv) * yRel;

        at.translate(xOffset, yOffset);
        at.scale(zoomFactor, zoomFactor);

        prevZoomFactor = zoomFactor;

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
