# Handoff-Mobile_Communication_System

A Java-based simulation of a mobile cellular network, demonstrating key concepts like mobile station movement, signal strength calculation, and the handover (handoff) process. The application provides a graphical user interface (GUI) to visualize the network in real-time and a control panel to monitor detailed data for each mobile device and antenna.

Handover or handoff refers to the process of transferring an ongoing call or data connectivity from one Base Station to another Base Station.

When a mobile moves into a different cell while the conversation is in progress then the MSC (Mobile Switching Centre) transfers the call to a new channel belonging to the new Base Station. 

<img width="1025" height="546" alt="image" src="https://github.com/user-attachments/assets/6d36dbb4-a1a1-4934-8e06-4da523766ba4" />

When a mobile user A moves from one cell to another cell then BSC 1 signal strength loses for the mobile User A and the signal strength of BSC 2 increases and thus ongoing calls or data connectivity for mobile users goes on without interrupting.

## Features
* **Real-time Simulation:** Visualize the movement of cars and pedestrians in a 2D map environment.
* **Handoff Process:** Dynamic handoff is simulated as mobile stations move between the coverage areas of different fixed antennas.
* **Signal Strength Analysis:** Calculates and displays real-time signal strength (in dBm) for each mobile station based on its distance from the connected antenna using the Friis transmission equation.
* **Interactive GUI:** A separate control panel allows users to select a specific antenna, car, or pedestrian to view its live data, including position, speed, signal power, and connection status.
* **Status Visualization:** The application provides a simple status (e.g., HIGH, MEDIUM, LOW, DISCONNECTED) based on the received signal power.
* **Multithreaded Design:** Each mobile station (car or pedestrian) runs as a separate thread, allowing for independent, parallel movement and updates.

## How It Works
The simulation is built using the Java Swing framework. Key logic is divided among the following classes:
* **`Main` Class:** The entry point of the application. It initializes all components, manages the simulation's main loop, and updates the UI with real-time data.
* **`MapWindow` Class:** The primary window for the simulation. It extends `JFrame` and handles all the custom drawing logic within its `paint` method. This class is responsible for rendering the map, antennas, and mobile stations.
* **`MobileStation` Class:** Represents a mobile device (car or pedestrian). Each instance is a separate `Thread` that manages its own movement, calculates its signal power from nearby antennas, and decides when to perform a handoff.
* **`Antenna` Class:** Represents a fixed base station. It holds static data such as position, transmitted power, and frequency.

## Visual Demo
A screenshot of the simulation in action:



<img width="392" height="189" alt="image" src="https://github.com/user-attachments/assets/912a015f-b443-48a0-8f0e-c97a8d454eb7" />




<img width="1919" height="1037" alt="image" src="https://github.com/user-attachments/assets/ba52ae87-0f18-438f-a80c-4af5248337bb" />





##Formulas

The simulation's core logic is based on principles of radio propagation. The following formulas are used to model the behavior of the mobile stations and antennas.

### 1. Distance Calculation

The physical distance ($d$) between a mobile station ($MS$) and a fixed antenna is 

$$
d = \sqrt{(x_{MS} - x_{Antenna})^2 + (y_{MS} - y_{Antenna})^2}
$$

* The implementation in the code also includes a scaling factor (0.19) to convert the on-screen pixel coordinates into a more realistic distance unit.

### 2. Received Power (Friis Transmission Equation)

The signal strength received by a mobile station ($P_r$) from an antenna is calculated using the **Friis Transmission Equation**. This formula models how the power of a radio wave diminishes with distance in free space.

The formula is as follows:

$$
P_r = P_t \times G_t \times G_r \times \left(\frac{\lambda}{4\pi d}\right)^2 \times L
$$

Where:
* $P_r$ is the received signal power (in Watts).
* $P_t$ is the transmitted power (in Watts), converted from dBm.
* $G_t$ is the transmitter antenna gain (linear scale).
* $G_r$ is the receiver antenna gain (linear scale).
* $\lambda$ (lambda) is the wavelength of the signal, calculated as $\frac{c}{f}$, where $c$ is the speed of light ($3 \times 10^8$ m/s) and $f$ is the frequency.
* $d$ is the distance between the transmitter and receiver.
* $L$ represents system losses (in the code, this is a fixed value equivalent to 15 dB).


The conversion from dB to a linear ratio is done using the formula:

$$
\text{Linear Value} = 10^{(\text{dB Value} / 10)}
$$



