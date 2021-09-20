package de.unistuttgart.racoon.examples;

import de.unistuttgart.racoon.main.SimpleRacoonGame;

public class Example02 extends SimpleRacoonGame {
    public static void main(final String[] args) {
        createInstance(Example02.class);
    }

    @Override
    protected String getTerritoryFile() {
        return "/territorys/example02.ter";
    }

    /**
     * Another racoon program.
     */
    @Override
    protected void run() {
        displayInNewGameWindow();

        racoon.turnRight();

        for (int i = 0; i < 3; i++) {
            racoon.move();
            racoon.putLeaf();
        }

        rotate180Degrees();

        for (int i = 0; i < 3; i++) {
            racoon.removeLeaf();
            racoon.move();
        }
    }

    private void rotate180Degrees() {
        racoon.turnLeft();
        racoon.turnLeft();
    }
}
