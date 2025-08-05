package com.mobile.communication.system;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class Main implements ActionListener{

    public static MapWindow mapWindow;
    public MobileStation thread;
    static JFrame controlWindow;
    private static  JTextArea carTextArea, antennaTextArea, pedestrianTextArea;
    private JComboBox<String> carStationNumber, fixedStationNumber, pedestrianStationNumber;
    private static int carStationDataNumber, pedestrianStationDataNumber, antennaStationDataNumber;
    private static String[] colorNames = new String[10];
    private  String tempString;
    public static Antenna[] fixedAntennas;

    public static MobileStation[] pedestrianThreadArray;
    public static MobileStation[] carThreadArray;

    public int numAntennas, numMobilePedestrians;
    public static Main mainProgram;

    private static String title = "";
    private static String mobileStationNumber = "";
    private static String speed = "";
    private static String positionX = "";
    private static String positionY = "";
    private static String power = "";
    private static String antenna = "";
    private static String status = "";

    public static void main(String[] args) {
        mainProgram = new Main();

        mapWindow = new MapWindow(mainProgram);

        mapWindow.setVisible(true);

        while (true) {
            try {
                Thread.sleep(100);
                mapWindow.repaint();

                // ANTENNA DATA
                title = "           FIXED STATION (ANTENNA) DATA\n";
                mobileStationNumber = "  Antenna Number: " + String.valueOf(antennaStationDataNumber);
                positionX = "  Position X: " + String.valueOf(fixedAntennas[antennaStationDataNumber].positionX);
                positionY = "  Position Y: " + String.valueOf(fixedAntennas[antennaStationDataNumber].positionY);
                power = "  Power: " + String.valueOf(fixedAntennas[antennaStationDataNumber].transmittedPower) + " Dbm";
                antenna = "  Antenna Color: " + colorNames[antennaStationDataNumber];

                antennaTextArea.setText(title + "\n" + mobileStationNumber + "\n" + positionX + "\n" + positionY + "\n" + power + "\n" + antenna);

                // PEDESTRIAN DATA
                title = "           MOBILE STATION (PEDESTRIAN) DATA\n";
                mobileStationNumber = "  Mobile Station Number: " + String.valueOf(pedestrianStationDataNumber);
                speed = "  Speed: " + String.valueOf(pedestrianThreadArray[pedestrianStationDataNumber].speed) + " Km/h";
                positionX = "  Position X: " + String.valueOf(pedestrianThreadArray[pedestrianStationDataNumber].positionX);
                positionY = "  Position Y: " + String.valueOf(pedestrianThreadArray[pedestrianStationDataNumber].positionY);
                power = "  Power: " + String.valueOf(10 * Math.log(pedestrianThreadArray[pedestrianStationDataNumber].power * 1000)) + " Dbm";
                antenna = "  Provider Antenna: " + String.valueOf(pedestrianThreadArray[pedestrianStationDataNumber].currentAntenna) + "  (" + colorNames[pedestrianThreadArray[pedestrianStationDataNumber].currentAntenna] + ")";

                double help = 10 * Math.log(pedestrianThreadArray[pedestrianStationDataNumber].power * 1000);
                if (help < -90) {
                    status = "  Status: DISCONNECTED";
                } else if (help > -90 && help < -85) {
                    status = "  Status: LOW";
                } else if (help > -85 && help < -70) {
                    status = "  Status: MEDIUM";
                } else {
                    status = "  Status: HIGH";
                }

                pedestrianTextArea.setText(title + "\n" + mobileStationNumber + "\n" + speed + "\n" + positionX + "\n" + positionY + "\n" + power + "\n" + antenna + "\n" + status);

                // CAR DATA
                title = "           MOBILE STATION (CAR) DATA\n";
                mobileStationNumber = "  Mobile Station Number: " + String.valueOf(carStationDataNumber);
                speed = "  Speed: " + String.valueOf(carThreadArray[carStationDataNumber].speed) + " Km/h";
                positionX = "  Position X: " + String.valueOf(carThreadArray[carStationDataNumber].positionX);
                positionY = "  Position Y: " + String.valueOf(carThreadArray[carStationDataNumber].positionY);
                power = "  Power: " + String.valueOf(10 * Math.log(carThreadArray[carStationDataNumber].power * 1000)) + " Dbm";
                antenna = "  Provider Antenna: " + String.valueOf(carThreadArray[carStationDataNumber].currentAntenna) + "  (" + colorNames[carThreadArray[carStationDataNumber].currentAntenna] + ")";
                help = 10 * Math.log(carThreadArray[carStationDataNumber].power * 1000);
                if (help < -90) {
                    status = "  Status: DISCONNECTED";
                } else if (help > -90 && help < -85) {
                    status = "  Status: LOW";
                } else if (help > -85 && help < -70) {
                    status = "  Status: MEDIUM";
                } else {
                    status = "  Status: HIGH";
                }
                carTextArea.setText(title + "\n" + mobileStationNumber + "\n" + speed + "\n" + positionX + "\n" + positionY + "\n" + power + "\n" + antenna + "\n" + status);
            } catch (InterruptedException ex) {}
        }
    }

    public Main() {
        // PROMPT FOR NUMBER OF PEDESTRIANS
        tempString = JOptionPane.showInputDialog("Enter the number of pedestrians:");
        this.numMobilePedestrians = Integer.parseInt(tempString);

        // FILL COLOR LIST
        colorNames[0] = "BLACK";
        colorNames[1] = "BLUE";
        colorNames[2] = "CYAN";
        colorNames[3] = "GREEN";
        colorNames[4] = "MAGENTA";
        colorNames[5] = "ORANGE";
        colorNames[6] = "RED";
        colorNames[7] = "YELLOW";
        colorNames[8] = "WHITE";
        colorNames[9] = "GRAY";

        // CONTROL WINDOW
        controlWindow = new JFrame();
        controlWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        controlWindow.setTitle("MOBILE COMMUNICATIONS");
        controlWindow.setBounds(1000, 0, 350, 700);

        // MAIN WINDOW PANEL
        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(Color.LIGHT_GRAY);
        controlPanel.setLayout(null);
        controlWindow.setContentPane(controlPanel);

        // PROJECT TITLE
        JLabel title1 = new JLabel("HANDOVER/HANDOFF SIMULATION");
        title1.setFont(new Font("Verdana", Font.BOLD, 16));
        title1.setSize(400, 20);
        title1.setLocation(20, 10);
        controlPanel.add(title1);

        // ANTENNAS SECTION
        JLabel antennaLabel = new JLabel("------------------ANTENNAS------------------");
        antennaLabel.setFont(new Font("Verdana", Font.BOLD, 14));
        antennaLabel.setLocation(10, 55);
        antennaLabel.setSize(500, 20);
        controlPanel.add(antennaLabel);

        // TEXT AREA TO DISPLAY ANTENNA DATA
        antennaTextArea = new JTextArea();
        antennaTextArea.setSize(300, 150);
        antennaTextArea.setLocation(20, 100);
        controlPanel.add(antennaTextArea);

        // COMBOBOX TO SELECT ANTENNA
        fixedStationNumber = new JComboBox<>();
        fixedStationNumber.setSize(40, 25);
        fixedStationNumber.setLocation(200, 75);
        for (int k = 0; k < 8; k++) {
            this.fixedStationNumber.addItem(Integer.toString(k));
        }
        fixedStationNumber.setActionCommand("comboBoxAntenna");
        fixedStationNumber.addActionListener(this);
        controlPanel.add(fixedStationNumber);

        // LABEL FOR SELECTING ANTENNA
        JLabel selectAntennaLabel = new JLabel("Select Antenna:");
        selectAntennaLabel.setFont(new Font("Verdana", Font.PLAIN, 14));
        selectAntennaLabel.setLocation(50, 75);
        selectAntennaLabel.setSize(500, 20);
        controlPanel.add(selectAntennaLabel);

        // PEDESTRIANS SECTION
        JLabel pedestrianLabel = new JLabel("-----------------PEDESTRIANS-----------------");
        pedestrianLabel.setFont(new Font("Verdana", Font.BOLD, 14));
        pedestrianLabel.setLocation(10, 260);
        pedestrianLabel.setSize(500, 20);
        controlPanel.add(pedestrianLabel);

        // COMBOBOX TO SELECT PEDESTRIAN
        pedestrianStationNumber = new JComboBox<>();
        pedestrianStationNumber.setSize(40, 25);
        pedestrianStationNumber.setLocation(200, 280);
        for (int k = 0; k < this.numMobilePedestrians; k++) {
            this.pedestrianStationNumber.addItem(Integer.toString(k));
        }
        pedestrianStationNumber.setActionCommand("comboBoxPedestrian");
        pedestrianStationNumber.addActionListener(this);
        controlPanel.add(pedestrianStationNumber);

        // TEXT AREA TO DISPLAY PEDESTRIAN DATA
        pedestrianTextArea = new JTextArea();
        pedestrianTextArea.setSize(300, 150);
        pedestrianTextArea.setLocation(20, 305);
        controlPanel.add(pedestrianTextArea);

        // LABEL FOR SELECTING PEDESTRIAN
        JLabel selectPedestrianLabel = new JLabel("Select Pedestrian:");
        selectPedestrianLabel.setFont(new Font("Verdana", Font.PLAIN, 14));
        selectPedestrianLabel.setLocation(50, 280);
        selectPedestrianLabel.setSize(500, 20);
        controlPanel.add(selectPedestrianLabel);

        // CARS SECTION
        JLabel carLabel = new JLabel("------------------CARS------------------");
        carLabel.setFont(new Font("Verdana", Font.BOLD, 14));
        carLabel.setLocation(10, 460);
        carLabel.setSize(500, 20);
        controlPanel.add(carLabel);

        // COMBOBOX TO SELECT CAR
        carStationNumber = new JComboBox<>();
        carStationNumber.setSize(40, 25);
        carStationNumber.setLocation(200, 480);
        for (int k = 0; k < 7; k++) {
            this.carStationNumber.addItem(Integer.toString(k));
        }
        carStationNumber.setActionCommand("comboBoxCar");
        carStationNumber.addActionListener(this);
        controlPanel.add(carStationNumber);

        // LABEL FOR SELECTING CAR
        JLabel selectCarLabel = new JLabel("Select Car:");
        selectCarLabel.setFont(new Font("Verdana", Font.PLAIN, 14));
        selectCarLabel.setLocation(50, 480);
        selectCarLabel.setSize(500, 20);
        controlPanel.add(selectCarLabel);

        // TEXT AREA TO DISPLAY CAR DATA
        carTextArea = new JTextArea();
        carTextArea.setSize(300, 150);
        carTextArea.setLocation(20, 505);
        controlPanel.add(carTextArea);

        controlWindow.setVisible(true);

        this.loadFixedAntennas();
        this.loadCars();

        // CREATE PEDESTRIAN THREADS
        this.pedestrianThreadArray = new MobileStation[numMobilePedestrians];
        for (int k = 0; k < numMobilePedestrians; k++) {
            this.pedestrianThreadArray[k] = new MobileStation(Integer.toString(k), "peaton", 2);
            this.pedestrianThreadArray[k].setAntennasArray(this.fixedAntennas);
        }
    }

    public void loadFixedAntennas() {
        // LOAD ANTENNAS
        this.fixedAntennas = new Antenna[8];
        this.fixedAntennas[0] = new Antenna(720, 20, 17, 12, 850000000);
        this.fixedAntennas[1] = new Antenna(610, 100, 17, 12, 850000000);
        this.fixedAntennas[2] = new Antenna(825, 220, 20.5, 12, 850000000);
        this.fixedAntennas[3] = new Antenna(570, 280, 19, 12, 850000000);
        this.fixedAntennas[4] = new Antenna(500, 470, 19, 12, 850000000);
        this.fixedAntennas[5] = new Antenna(920, 685, 28.5, 12, 850000000);
        this.fixedAntennas[6] = new Antenna(-10, -10, 32, 12, 850000000);
        this.fixedAntennas[7] = new Antenna(195, 890, 29.5, 12, 850000000);
    }

    public void loadCars() {
        // LOAD 7 CARS
        this.carThreadArray = new MobileStation[7];
        this.carThreadArray[0] = new MobileStation(Integer.toString(0), "carro", 10);
        this.carThreadArray[1] = new MobileStation(Integer.toString(1), "carro", 20);
        this.carThreadArray[2] = new MobileStation(Integer.toString(2), "carro", 30);
        this.carThreadArray[3] = new MobileStation(Integer.toString(3), "carro", 40);
        this.carThreadArray[4] = new MobileStation(Integer.toString(4), "carro", 50);
        this.carThreadArray[5] = new MobileStation(Integer.toString(5), "carro", 60);
        this.carThreadArray[6] = new MobileStation(Integer.toString(6), "carro", 70);

        // SET STREET POSITIONS
        this.carThreadArray[0].setPositionY(50);
        this.carThreadArray[0].setPositionY(77);
        this.carThreadArray[1].setPositionY(144);
        this.carThreadArray[2].setPositionY(203);
        this.carThreadArray[3].setPositionY(267);
        this.carThreadArray[4].setPositionY(347);
        this.carThreadArray[5].setPositionY(439);
        this.carThreadArray[6].setPositionY(507);

        // SET STARTING POSITIONS
        this.carThreadArray[0].setPositionX(0);
        this.carThreadArray[0].setPositionX(0);
        this.carThreadArray[1].setPositionX(0);
        this.carThreadArray[2].setPositionX(0);
        this.carThreadArray[3].setPositionX(0);
        this.carThreadArray[4].setPositionX(0);
        this.carThreadArray[5].setPositionX(0);
        this.carThreadArray[6].setPositionX(0);

        // SET ANTENNA ARRAY
        for (MobileStation car : this.carThreadArray) {
            car.setAntennasArray(this.fixedAntennas);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // COMBOBOX ANTENNAS
        if ("comboBoxAntenna".equals(e.getActionCommand())) {
            this.antennaStationDataNumber = fixedStationNumber.getSelectedIndex();
        }
        // COMBOBOX PEDESTRIANS
        if ("comboBoxPedestrian".equals(e.getActionCommand())) {
            this.pedestrianStationDataNumber = pedestrianStationNumber.getSelectedIndex();
        }
        // COMBOBOX CARS
        if ("comboBoxCar".equals(e.getActionCommand())) {
            this.carStationDataNumber = carStationNumber.getSelectedIndex();
        }

    }
}
