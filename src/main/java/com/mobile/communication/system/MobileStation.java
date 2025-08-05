package com.mobile.communication.system;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import java.util.Random;

public class MobileStation extends Thread {

    public int positionX, positionY;  // positionX, positionY – coordinates of the mobile station
    public String type;       // type – maybe MS type (e.g., moving/static)
    public String number;         // number – ID or identifier of the MS
    public int speed;        // speed – movement speed of MS
    private int waitingTime;             // delay or pause before MS moves
    public Antenna[] antennasArray;    // antennaVector – array of available antennas
    public int currentAntenna = 0;       // currentAntenna – index of currently connected antenna
    public double power;
    private int path, scenario, counter;
    private boolean firstTime = false;

    public MobileStation(String base, String type, int speed) {
        this.type = type;
        this.number = base;
        this.speed = speed;
        this.calculateTime();
        Random rnd = new Random();
        this.positionX = (int) (rnd.nextDouble() * 870 + 30);
        this.positionY = (int) (rnd.nextDouble() * 670 + 30);
        this.path = (int) (rnd.nextDouble() * 40);
        this.scenario = (int) (rnd.nextDouble() * 4);
    }

    public void calculateTime() {
        this.waitingTime = (int) (1000 / this.speed);
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public void setAntennasArray(Antenna[] Antennas) {
        this.antennasArray = Antennas;
    }

    public void validateBorders() {
        if (this.type.equals("peaton")) {
            if (this.positionX > 980 || this.positionY > 700) {
                Random rnd = new Random();
                this.positionX = (int) (rnd.nextDouble() * 870 + 30);
                this.positionY = (int) (rnd.nextDouble() * 670 + 30);
                counter = 0;
            }
        }
        if (this.type.equals("carro")) {
            if (this.positionX > 980) {
                this.positionX = 0;
                counter = 0;
            }
        }
    }

    public double receivedPower(double alpha, int position) {
        double distance, poten;
        distance = sqrt(pow((this.antennasArray[position].positionX - this.positionX), 2) + pow((this.antennasArray[position].positionY - this.positionY), 2)) * 0.19;

        double potTxAd = Math.pow(10, this.antennasArray[position].transmittedPower / 10);
        double ganRxAd = Math.pow(10, this.antennasArray[position].receiverGain / 10);
        double ganTxAd = Math.pow(10, this.antennasArray[position].transmitterGain / 10);
        double wavelength = 300000000 / this.antennasArray[position].frequency;
        double losses = 0.0000316227766; // 15 decibels of loss
        double aux = Math.pow((wavelength / (4 * Math.PI * distance)), 2);
        poten = potTxAd * ganRxAd * ganTxAd * losses * aux;
        return poten;
    }


    public void run() {
        while (!interrupted()) {
            switch (this.type) {
                case "carro":
                    this.positionX++;
                    break;
                case "peaton":
                    if (path > 0) {
                        path--;
                        switch (scenario) {
                            case 1:
                                this.positionX++;
                                this.positionX++;
                                break;
                            case 2:
                                this.positionY++;
                                this.positionY++;
                                break;
                            case 3:
                                this.positionX++;
                                this.positionY++;
                                break;
                            case 4:
                                this.positionY--;
                                this.positionX++;
                                break;
                        }
                    } else {
                        Random rnd = new Random();
                        this.path = (int) (rnd.nextDouble() * 40);
                        this.scenario = (int) (rnd.nextDouble() * 4);
                    }
                    break;
            }

            this.validateBorders();
            if (currentAntenna < 8) {
                this.power = this.receivedPower(1, this.currentAntenna);
            }

            for (int k = 0; k < 8; k++) {
                double pot = this.receivedPower(0.7, k);
                if (power < pot) {
                    this.currentAntenna = k;
                }
                if (10 * Math.log(power * 1000) < -90 && firstTime) {
                    this.currentAntenna = 8;
                }
            }
            if (counter < 10) {
                counter++;
                firstTime = false;
            } else {
                firstTime = true;
            }

            try {
                Thread.sleep(this.waitingTime);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }





}
