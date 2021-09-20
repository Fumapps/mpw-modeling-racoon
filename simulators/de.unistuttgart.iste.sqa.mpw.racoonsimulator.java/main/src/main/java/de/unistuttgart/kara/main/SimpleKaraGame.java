package de.unistuttgart.racoon.main;

import de.unistuttgart.racoon.facade.TerritoryLoader;
import de.unistuttgart.racoon.facade.Racoon;
import de.unistuttgart.racoon.facade.RacoonGame;
import de.unistuttgart.racoon.ui.JavaFXUI;
import de.unistuttgart.iste.sqa.mpw.framework.exceptions.GameAbortedException;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static de.unistuttgart.iste.sqa.mpw.framework.utils.Preconditions.*;

public abstract class SimpleRacoonGame {
    private static final String DEFAULT_KARA_WORLD = "/territorys/example01.ter";

    protected static void createInstance(Class<? extends SimpleRacoonGame> racoonProgramClass) {
        try {
            var program = racoonProgramClass.getDeclaredConstructor().newInstance();
            program.doRun();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Name of the environment variable used to determine the output interface
     */
    private static final String OUTPUT_INTERFACE_ENVIRONMENT_VARIABLE_NAME = "OUTPUT_INTERFACE";

    /**
     * Variable inherited to child classes containing the default racoon
     * which is named racoon here. Intentionally, no getter or setter is used
     * as they are introduced only after lecture 2.
     */
    protected final Racoon racoon;

    /**
     * The game object of this simple game. Can be used to start, stop, reset,
     * or display the game.
     */
    protected final RacoonGame game = new RacoonGame();

    /**
     * The current SimpleRacoonGame. Can be used to load a territory or to
     * display the game in a new game window.
     */
    protected final SimpleRacoonGame currentGame = this;

    /**
     * A console object to demonstrate IO besides using the read or write methods
     * of racoons.
     */
    protected final Console console = System.console();

    /**
     * Initialized a simple racoon game by loading a default territory
     * and setting protected references to contain racoon and
     * the game.
     */
    public SimpleRacoonGame() {
        initializeGame(getTerritoryFile());
        game.startGamePaused();

        this.racoon = this.game.getTerritory().getRacoon();
    }

    protected String getTerritoryFile() {
        return DEFAULT_KARA_WORLD;
    }

    protected void initializeGame(String fileName) {
        try {
            TerritoryLoader.initializeFor(game).loadFromResourceFile(fileName);
            game.hardReset();
        } catch (IOException e) {
            throw new RuntimeException("failed to load the default territory", e);
        }
    }

    /**
     * Predefined racoon method designed to be overridden in subclass.
     * Put the racoon code into this method. This parent class version
     * is empty, so that the racoon does not do anything by default.
     */
    protected abstract void run();

    /**
     * Method to start the execution of a racoon game and handle any exceptions happening
     * while running.
     */
    public final void doRun() {
        try {
            this.run();
        } catch (final GameAbortedException e) {
            // End this game
        } catch (final RuntimeException e) {
            this.game.confirmAlert(e);
            throw e;
        }
        this.game.stopGame();
    }

    /**
     * Displays the racoon game in a new game window
     * The UI type can be specified in the config file or in the environment variable
     * OUTPUT_INTERFACE. Possible values are JAVA_FX, HTTP and NONE
     * The default is JAVA_FX.
     */
    protected void displayInNewGameWindow() {
        final String mode = UIMode.JAVA_FX;
        switch (mode) {
            case UIMode.JAVA_FX:
                JavaFXUI.displayInNewGameWindow(this.game);
                break;
            case UIMode.NONE:
                // ignore
                break;
            default:
                throw new IllegalStateException("Unknown output interface type, possible values are: " +
                        UIMode.JAVA_FX + " or " + UIMode.NONE);
        }
    }

    /*@
     @ requires true;
     @ ensures game.getCurrentGameMode() == Mode.INITIALIZING;
     @*/
    /**
     * Loads the Territory from a resources file.
     * Only absolute resource paths are allowed. E.g. the fileName "/territory.ter" represents the file
     * territory.ter in the resources directory
     * This resets the game if it was already started. After the territory was loaded, the game is
     * in mode INITIALIZING. To start the game, game.startGame() should be called
     *
     * @param fileName An absolute path to the resource file. Must start with a "/"
     * @throws IllegalArgumentException if fileName is no absolute resource path (does not start with "/")
     *                                  or if the file was not found
     */
    protected final void loadTerritoryFromResourceFile(final String fileName) {
        checkNotNull(fileName);
        checkArgument(fileName.startsWith("/"), "fileName does not start with \"/\"");
        final InputStream territoryFileStream = getClass().getResourceAsStream(fileName);
        checkArgument(territoryFileStream != null, "territory file not found");
        try {
            TerritoryLoader.initializeFor(game).loadFromResourceFile(fileName);
        } catch (IOException e) {
            game.confirmAlert(e);
        }
    }

    /**
     * Loads the UI Mode from the environment variable if possible
     *
     * @return The UI mode if the environment variable was set, otherwise an empty optional
     * @throws IllegalStateException if an illegal value is set
     */
    private static Optional<String> getUIModeFromEnvironmentVariable() {
        final String value = System.getenv(SimpleRacoonGame.OUTPUT_INTERFACE_ENVIRONMENT_VARIABLE_NAME);
        if (value != null) {
            try {
                return Optional.of(value);
            } catch (final IllegalArgumentException e) {
                throw new IllegalStateException("Illegal environmental variable", e);
            }
        } else {
            return Optional.empty();
        }
    }

    /**
     * Different UI types
     * No enum is used because enums use reflection for valueOf which can cause issues if reflection is forbidden
     */
    private static final class UIMode {
        public static final String JAVA_FX = "JAVA_FX";
        public static final String NONE = "NONE";
    }
}
