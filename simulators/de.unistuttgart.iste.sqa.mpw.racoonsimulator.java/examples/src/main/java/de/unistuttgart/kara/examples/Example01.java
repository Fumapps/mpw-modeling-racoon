package de.unistuttgart.racoon.examples;

import de.unistuttgart.racoon.main.SimpleRacoonGame;

public class Example01 extends SimpleRacoonGame {
    public static void main(final String[] args) {
        createInstance(Example01.class);
    }

    @Override
    protected void run() {
        displayInNewGameWindow();
        racoon.move();
        racoon.turnRight();
        racoon.move();
        racoon.removeLeaf();
        racoon.turnLeft();
        racoon.turnLeft();
        racoon.move();
        racoon.putLeaf();
        racoon.turnLeft();
        racoon.move();
        racoon.turnRight();
        racoon.turnRight();
    }

}
