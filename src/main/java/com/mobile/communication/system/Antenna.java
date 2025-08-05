package com.mobile.communication.system;

import java.awt.Color;
import java.awt.Graphics;
import static java.lang.Math.pow;

public class Antenna {

    public int positionX, positionY; //Coordinates of the antenna (2D map)
    public double receivedPower = -90;       // in dBm
    public double receiverGain = 4;          // in dB
    public double transmittedPower;          // in dBm
    public double transmitterGain;           // in dB
    public double frequency;                 // in Hz


    // create a new antenna using constructor
    public Antenna(int posX, int posY, double txPower, double txGain, double freq) {
        this.positionX = posX;
        this.positionY = posY;
        this.frequency = freq;
        this.transmitterGain = txGain;
        this.transmittedPower = txPower;
    }

    // calculates radius:tells us how far the antenna's signal can go
    public double calculateRadius() {

        //It converts values from dB/dBm → linear values.
        double txPowerLinear = Math.pow(10, this.transmittedPower / 10);   // Convert from dBm to mW
        double rxPowerLinear = Math.pow(10, this.receivedPower / 10);
        double rxGainLinear = Math.pow(10, this.receiverGain / 10);
        double txGainLinear = Math.pow(10, this.transmitterGain / 10);

        // lambda= c / f (speed of light in m/s)
        double wavelength = 300000000 / this.frequency;

        // Equivalent to 15 dB loss → 10^(-15/10)
        // 15 dB of loss in linear format. (15 dB loss = divide power by 31.6)
        double loss = 0.0000316227766;

        //Friis Transmission Equation, used to calculate distance.
        double factor = Math.sqrt((txPowerLinear * txGainLinear * rxGainLinear * loss) / rxPowerLinear);


        double radius = (wavelength * factor) / (4 * Math.PI);
        return radius;
    }
}
