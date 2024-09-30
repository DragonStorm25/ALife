package src.graphics;

import java.awt.*; 
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

import src.particles.Particle;
import src.physics.PhysicsEngine;

public class Renderer {
    private PhysicsEngine physicsEngine;
    private JFrame frame;
    private JPanel panel;
    private final int FPS = 60;
    private TimerTask renderTask = null;
    private Timer renderTimer = null;

    public Renderer(PhysicsEngine pe) {
        this.frame = new JFrame("ALife");
        Renderer self = this;
        this.panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                self.render(g);
            }
        };
        this.frame.add(this.panel);
        this.frame.setSize(800, 800);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.physicsEngine = pe;
    }

    public void toggleShow() {
        this.frame.setVisible(!this.frame.isVisible());
        this.startRendering();
    }

    public void render(Graphics g) {
        g.setColor(Color.red);
        for (Particle p : physicsEngine.idToParticle.values()) {
            g.fillOval((int)p.getPosition().getX(), (int)p.getPosition().getY(), p.getMass(), p.getMass());
        }
    }

    public void startRendering() {
        if (renderTimer != null)
            renderTimer.cancel();

        renderTask = new TimerTask() {

            @Override
            public void run() {
                panel.repaint();
            }
        };

        renderTimer = new Timer("RenderTimer");//create a new Timer
        renderTimer.scheduleAtFixedRate(renderTask, 0, 1000/FPS);
    }
}
