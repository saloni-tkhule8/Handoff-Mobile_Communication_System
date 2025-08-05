package com.mobile.communication.system;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import javax.swing.*;
import javax.swing.border.*;


public class MapWindow extends JFrame{
    public JPanel panel;                       // Main panel to draw on
    private MobileStation []pedestrianThreads; // Threads for pedestrians
    private int numberOfPedestrians;           // Number of moving pedestrians
    private Main  mainRef;                     // Main controller / brain aka main.java where main method exists
    private Color []colorList = new Color[10];  // Color legend

    public MapWindow(Main mainProgram) {

        // collects all the data from the main Main.java class.
        this.mainRef = mainProgram;
        this.numberOfPedestrians = mainRef.numMobilePedestrians;
        this.pedestrianThreads = mainRef.pedestrianThreadArray;


        //Creates a window 1000x700 pixels
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, 1000, 700);
        setTitle("MOBILE COMMUNICATIONS");

        //Make a drawing panel
        panel = new JPanel();
        panel.setBackground(Color.CYAN);
        panel.setLayout(null);
        setContentPane(panel);

        //  Map Image of Mumbai city as background
        String path = "D:\\handoff_mcs\\handoff\\src\\main\\resources\\mumbai.jpg";
        JLabel map = new JLabel(new ImageIcon(path));
        map.setSize(985, 666);
        map.setLocation(0, 0);
        panel.add(map);

        // Fill color list
        //These will color-code the devices by which antenna they’re connected to
        colorList[0] = Color.BLACK;
        colorList[1] = Color.BLUE;
        colorList[2] = Color.CYAN;
        colorList[3] = Color.GREEN;
        colorList[4] = Color.MAGENTA;
        colorList[5] = Color.ORANGE;
        colorList[6] = Color.RED;
        colorList[7] = Color.YELLOW;
        colorList[8] = Color.WHITE;
        colorList[9] = Color.DARK_GRAY;

        //Each car or pedestrian is a Thread, running in parallel, moving independently
        //  Start pedestrian threads
        for (int i = 0; i < numberOfPedestrians; i++) {
            pedestrianThreads[i].start();
        }
        // Start car threads
        for (int i = 0; i < 7; i++) {
            mainRef.carThreadArray[i].start();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);// call superclass's paint method

        //Makes lines dashed and thick
        float[] dashPattern = {10};
        Graphics2D g2d = (Graphics2D) g;

        g2d.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND, 10, dashPattern, 0));

        //Draw Fixed Antennas
        for (int i = 0; i < 8; i++) {
            double radius = mainRef.fixedAntennas[i].calculateRadius();
            g2d.setColor(colorList[i]);
            g2d.fill(new Rectangle2D.Double(
                    mainRef.fixedAntennas[i].positionX,
                    mainRef.fixedAntennas[i].positionY,
                    20, 20));
            g2d.draw(new Ellipse2D.Double(
                    mainRef.fixedAntennas[i].positionX - (radius / 2) + 5,
                    mainRef.fixedAntennas[i].positionY - (radius / 2),
                    radius, radius));
            g2d.drawString(String.valueOf(i),
                    mainRef.fixedAntennas[i].positionX,
                    mainRef.fixedAntennas[i].positionY);
        }

        //Draw Mobile Cars
        for (int i = 0; i < 7; i++) {
            g2d.setColor(colorList[mainRef.carThreadArray[i].currentAntenna]);
            double x = mainRef.carThreadArray[i].getPositionX();
            double y = mainRef.carThreadArray[i].getPositionY();
            g2d.fill(new Rectangle2D.Double(x, y, 30, 20));
            g2d.drawString(String.valueOf(i), (int) x, (int) y);
        }

        // Draw Mobile Pedestrians
        for (int i = 0; i < numberOfPedestrians; i++) {
            g2d.setColor(colorList[pedestrianThreads[i].currentAntenna]);
            double x = pedestrianThreads[i].getPositionX();
            double y = pedestrianThreads[i].getPositionY();
            g2d.fill(new Ellipse2D.Double(x, y, 15, 15));
            g2d.drawString(String.valueOf(i), (int) x, (int) y);
        }
    }
}


