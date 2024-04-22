# Nueral Evolutionary agents 

This project simulates the behavior of animats (artificial life forms) in a grid environment. The animats move around the grid, interact with objects, and learn from their experiences.

## Technologies Used

- Java
- Maven

## Features

- Animats with different roles (teachers and students)
- Animats can move around a grid environment
- Animats can interact with different types of objects in the environment
- Animats can learn from their experiences and improve their performance
- The simulation includes a natural selection process where the best performing animats are selected for reproduction

## Code Structure

The main classes in the project are:

- `ObjectCollection.java`: This class manages the objects in the grid environment. It includes methods for setting and getting the state of objects at specific grid locations.

- `AnimatCollection.java`: This class manages a collection of animats. It includes methods for running the simulation for a specified number of days, selecting the best performing animats, and saving the state of the simulation.

## How to Run

To run the project, you need to have Java and Maven installed on your machine. You can then use the following command to compile and run the project:

```bash
mvn clean install
mvn exec:java
