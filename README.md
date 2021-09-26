# Racoon - MPW Simulator

This is the modeled racoon MPW simulator example based on the MPW framework (https://github.com/Fumapps/mpw-modeling-framework).

![modeling approach](documentation/graphics/mdsd-approach-concrete-simulator.svg)

It defines the modeling of the racoon simulator under `/bundles/de.unistuttgart.iste.sqa.mpw.modeling.racoonsimulator`.
After code-generation with Maven `package`, in `/simulators` the both simulators for Java and C++ can be found.

## Java Simulator

![screenshot java racoon simulator](simulators/de.unistuttgart.iste.sqa.mpw.racoonsimulator.java/Screenshot_RacoonSimulator_Java.png)

The Java simulator uses Maven, Java 15 and JavaFX.

##  C++ Simulator

The C++ simulator is based on CMake and SDL2.
