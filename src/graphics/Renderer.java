package src.graphics;

import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

import src.physics.PhysicsEngine;

public class Renderer {
    private JFrame frame;
    private JPanel panel;
    private final int FPS = 60;
    private TimerTask renderTask = null;
    private Timer renderTimer = null;

    public Renderer(PhysicsEngine pe) {
        this.frame = new JFrame("ALife");
        this.panel = new RendererPanel(pe);
        this.panel.setSize(1024, 1024);
        this.frame.add(this.panel);
        this.frame.setSize(1024, 1024);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void toggleShow() {
        this.frame.setVisible(!this.frame.isVisible());
        this.startRendering();
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
