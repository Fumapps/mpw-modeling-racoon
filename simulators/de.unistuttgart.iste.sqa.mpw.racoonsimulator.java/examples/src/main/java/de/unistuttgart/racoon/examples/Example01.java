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
        racoon.turnLeft();
        racoon.turnLeft();
        racoon.turnLeft();
        racoon.turnLeft();
        racoon.move();
        racoon.eat();
        racoon.turnLeft();
        racoon.turnLeft();
        racoon.move();
        racoon.move();
        racoon.turnLeft();
        racoon.turnLeft();
    }

}
